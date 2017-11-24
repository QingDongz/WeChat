package cn.summerwaves.controller;

import cn.summerwaves.model.AccessToken;
import cn.summerwaves.model.wcUser;
import cn.summerwaves.service.UserService;
import cn.summerwaves.util.CookieUtil;
import cn.summerwaves.util.TokenUtil;
import cn.summerwaves.util.WeChatUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


@Controller
public class WeChatController {
    private static final Logger logger = Logger.getLogger(WeChatController.class);
    public static final String TOKEN = "my_token";
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/token")
    public void weChatTest(String signature, String timestamp, String nonce, String echostr, HttpServletResponse res) throws NoSuchAlgorithmException, IOException {

        logger.info("signature:" + signature + ",timestamp:" + timestamp + ",nonce:" + nonce);
        String[] params = new String[]{TOKEN, timestamp, nonce};
        Arrays.sort(params);
        String clearText = params[0] + params[1] + params[2];
        String algorithm = "SHA-1";
        String sign =new String(org.apache.commons.codec.binary.Hex.encodeHex(MessageDigest.getInstance(algorithm).digest((clearText).getBytes()), true));

        if (signature.equals(sign)) {
            res.getWriter().print(echostr);
        }
    }

    @RequestMapping(value = "/redirect")
    public String redirect() {

        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxaa8cc3e512810e0c&redirect_uri=http://www.summerwaves.cn/oauth?response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect";
    }



    @RequestMapping(value = "/oauth", method = RequestMethod.GET)
    public ModelAndView weixinOAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String openid;

        //得到code
        String CODE = request.getParameter("code");
        logger.info("the code is " + CODE);
        try {
            openid = WeChatUtil.getOpenid(CODE);//todo 这行会报错

            String token = TokenUtil.makeToken(openid);

            response.addCookie(CookieUtil.createCookie("token", token, 60 * 60 * 24));

        } catch (JSONException e) {
            request.getRequestDispatcher("/redirect").forward(request, response);
        }
        return new ModelAndView("redirect:/u/home");
    }

    @RequestMapping(value = "/u/home")
    public ModelAndView toHome(HttpServletRequest request,HttpServletResponse response)  {

        ModelAndView mv = new ModelAndView();
        //从数据库获取access_token
        AccessToken accessTokenObject = userService.selectAccessToken();
        String accessTokenValue = accessTokenObject.getAccessToken();
        Cookie token = CookieUtil.getCookieByName(request, "token");
        String URL;

        try {
            String openid = TokenUtil.getUsernameInToken(token.getValue());
            //检查access_token是否过期，若不过期直接使用，若过期则获取新的access_token并在数据库中更新
            if (WeChatUtil.checkAccessToken(accessTokenObject)) {
                URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN".replace("ACCESS_TOKEN", accessTokenValue).replace("OPENID", openid);
            } else {
                String nextAccessToken = WeChatUtil.getAccessToken();
                URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN".replace("ACCESS_TOKEN", nextAccessToken).replace("OPENID", openid);
                accessTokenObject.setAccessToken(nextAccessToken);
                accessTokenObject.setAcquireTime(System.currentTimeMillis());
                userService.updateAccessToken(accessTokenObject);
            }
            logger.info("acessToken:" + accessTokenObject.getAccessToken());

            wcUser user = WeChatUtil.getUserMessage(openid,URL);
            mv.addObject("user", user);
            mv.setViewName("message");
        } catch (JSONException jsonObject) {
            try {
                request.getRequestDispatcher("/redirect").forward(request, response);
                logger.error("get a JSONException and reload");
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    @RequestMapping(value = "/")
    public String toTest() {
        return "home";
    }
}
