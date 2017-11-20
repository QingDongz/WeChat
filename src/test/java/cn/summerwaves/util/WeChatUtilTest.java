package cn.summerwaves.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeChatUtilTest {
    @Test
    public void getOpenid() throws Exception {

    }

    @Test
    public void getMenuStr() throws Exception {
        System.out.println(WeChatUtil.getMenuStr());

    }

    @Test
    public void setMenu() throws Exception {
        System.out.println(WeChatUtil.setMenu());
    }

    @Test
    public void getAccessToken() throws Exception {
        System.out.println(WeChatUtil.getAccessToken());
    }

    @Test
    public void checkToken() throws Exception {
        System.out.println(WeChatUtil.checkToken("1234", "1234", "123"));
    }


}