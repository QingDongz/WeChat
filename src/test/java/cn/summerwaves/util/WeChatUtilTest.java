package cn.summerwaves.util;

import cn.summerwaves.dao.UserDao;
import cn.summerwaves.model.AccessToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class WeChatUtilTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void checkAccessToken() throws Exception {
        AccessToken accessTokenObject = new AccessToken();
        accessTokenObject.setAccessToken(WeChatUtil.getAccessToken());
        accessTokenObject.setAcquireTime(System.currentTimeMillis());
        userDao.updateAccessToken(accessTokenObject);
    }

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