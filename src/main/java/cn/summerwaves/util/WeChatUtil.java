package cn.summerwaves.util;

import cn.summerwaves.model.AccessToken;
import cn.summerwaves.model.WCUser;
import org.apache.log4j.Logger;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class WeChatUtil {
    private static final Logger logger = Logger.getLogger(WeChatUtil.class);
    private static final String APPID = "wxaa8cc3e512810e0c";
    private static final String SECRET = "f526453358bf5ea725985d9bdfd38b5d";


    public static boolean checkToken(String signature, String timestamp, String nonce) {
        String token = "testToken";
        boolean flag = false;
        try {
            String[] strs = new String[]{token, timestamp, nonce};
            Arrays.sort(strs);

            StringBuilder appendStr = new StringBuilder();
            for (String str : strs) {
                appendStr.append(str);
            }

            StringBuilder checkSignature = new StringBuilder();
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = digest.digest(appendStr.toString().getBytes());

            for (int i =0;i<bytes.length;i++) {
                String shaHex = Integer.toHexString(bytes[i] & 0xFF);
                if (shaHex.length() < 2) {
                    checkSignature.append(0);
                }
                checkSignature.append(shaHex);
            }

            if (signature.equals(checkSignature.toString())) {
                flag = true;
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String getAccessToken() throws IOException {
        //处理微信接口URL并访问
        String accessTokenUrl = ("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=APPID&secret=SECRET").replace("APPID",APPID).replace("SECRET",SECRET);
        URL url = new URL(accessTokenUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        //获取返回的JSON信息
        InputStream inputStream = connection.getInputStream();
        int size = inputStream.available();
        byte[] bs = new byte[size];
        
        inputStream.read(bs);
        String message = new String(bs, "UTF-8");

        JSONObject jsonObject = new JSONObject(message);

        return jsonObject.getString("access_token");

    }

    public static Boolean checkAccessToken(AccessToken accessToken) {

        long acquireTime = accessToken.getAcquireTime();
        long nowTime = System.currentTimeMillis();
        long timeGap = nowTime - acquireTime;
        if (timeGap < 3600000) {
            return true;
        } else {
            return false;
        }
    }

    public static String setMenu() {
        JSONObject firstLevelMenu = new JSONObject();
        JSONArray firstLevelArray = new JSONArray();

        //一级菜单
        JSONObject firstLevelMenuContext1 = new JSONObject();
        firstLevelMenuContext1.put("type", "view");
        firstLevelMenuContext1.put("name", "跳转登录");
        firstLevelMenuContext1.put("url", "http://www.summerwaves.cn/redirect");

        firstLevelArray.put(firstLevelMenuContext1);

        firstLevelMenu.put("button", firstLevelArray);

        return firstLevelMenu.toString();

    }

    public static String getMenuStr() {
        JSONObject firstLevelMenu = new JSONObject();//一级菜单
        JSONArray firstLevelMenuArray = new JSONArray();//一级菜单列表


        //一级菜单内容1
        JSONObject firstLevelMenuContext1 = new JSONObject();
        firstLevelMenuContext1.put("type", "click");
        firstLevelMenuContext1.put("name", "歌曲");
        firstLevelMenuContext1.put("key", "V1001_TODAY_MUSIC");

        //一级菜单内容2
        JSONObject firstLevelMenuContext2 = new JSONObject();
        //一级菜单内容2的二级菜单列表
        JSONArray firstLevelMenuContext2Array = new JSONArray();
        //一级菜单内容2的二级菜单内容1
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("type", "click");
        jsonObject1.put("name", "歌曲");
        jsonObject1.put("key", "V1001_TODAY_MUSIC");
        //一级菜单内容2的二级菜单内容2
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("type", "view");
        jsonObject2.put("name", "视频");
        jsonObject2.put("url", "http://www.randzh.cn");
        firstLevelMenuContext2Array.put(jsonObject1);
        firstLevelMenuContext2Array.put(jsonObject2);
        firstLevelMenuContext2.put("name", "菜单");
        firstLevelMenuContext2.put("sub_button", firstLevelMenuContext2Array);

        //一级菜单内容3
        JSONObject firstLevelMenuContext3 = new JSONObject();
        firstLevelMenuContext3.put("type", "click");
        firstLevelMenuContext3.put("name", "视频");
        firstLevelMenuContext3.put("key", "V1001_TODAY_MOVIE");


        firstLevelMenuArray.put(firstLevelMenuContext1);
        firstLevelMenuArray.put(firstLevelMenuContext2);
        firstLevelMenuArray.put(firstLevelMenuContext1);


        firstLevelMenu.put("button", firstLevelMenuArray);

        return firstLevelMenu.toString();
    }

    public static String getOpenid(String code) throws IOException {

        //换取access_token 其中包含了openid
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code".replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", code);

        java.net.URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        InputStream inputStream = connection.getInputStream();
        int size = inputStream.available();
        byte[] bs = new byte[size];
        inputStream.read(bs);
        String jsonStr = new String(bs, "UTF-8");

        JSONObject jsonObj = new JSONObject(jsonStr);

        //有了用户的opendi就可以的到用户的信息了
        //地址为https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
        //得到用户信息之后返回到一个页面
        return jsonObj.get("openid").toString();
    }

    public static WCUser getUserMessage(String URL) throws IOException {

        java.net.URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        InputStream inputStream = connection.getInputStream();
        int size = inputStream.available();
        byte[] bs = new byte[size];
        inputStream.read(bs);
        String jsonStr = new String(bs, "UTF-8");
        //System.out.println(jsonStr);
        //out.print(jsonStr);
        JSONObject jsonObj = new JSONObject(jsonStr);

        WCUser WCUser = new WCUser();
        WCUser.setNickname(jsonObj.getString("nickname"));
        WCUser.setAvatar(jsonObj.getString("headimgurl"));
        logger.info("nickname:" + WCUser.getNickname() + ",avatar:" + WCUser.getAvatar());
        return WCUser;
    }
}
