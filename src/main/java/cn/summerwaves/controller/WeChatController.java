package cn.summerwaves.controller;

import cn.summerwaves.model.wcUser;
import cn.summerwaves.service.UserService;
import cn.summerwaves.util.CookieUtil;
import cn.summerwaves.util.TokenUtil;
import cn.summerwaves.util.WeChatUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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


        //得到code
        String CODE = request.getParameter("code");
        String openid = WeChatUtil.getOpenid(CODE);

        String token = TokenUtil.makeToken(openid);


        response.addCookie(CookieUtil.createCookie("token", token, 60 * 60 * 24));

        Cookie cookie =CookieUtil.getCookieByName(request, "token");

        if (cookie == null) {
            logger.info("cookie is null");

        } else {
            logger.info("cookie is not null");
        }



        return new ModelAndView("redirect:/u/home");
    }

    @RequestMapping(value = "/u/home")
    public ModelAndView toHome(HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView();
        Cookie token = CookieUtil.getCookieByName(request, "token");
        String  openid = TokenUtil.getUsernameInToken(token.getValue());
        wcUser user = WeChatUtil.getUserMessage(openid);
        mv.addObject("user", user);
        mv.setViewName("message");


        return mv;
    }

    @RequestMapping(value = "/")
    public String toTest() {
        return "home";
    }
}
