package cn.summerwaves.dao;

import cn.summerwaves.model.AccessToken;
import cn.summerwaves.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest {
    @Test
    public void updateAccessToken() throws Exception {
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken("54321");
        accessToken.setAcquireTime(987654);
        userDao.updateAccessToken(accessToken);
    }

    @Test
    public void selectToken() throws Exception {
        System.out.println(userDao.selectAccessToken());
    }

    @Autowired
    private UserDao userDao;
    @Test
    public void insertUser() throws Exception {
        User user = new User();
        user.setUsername("test1");
        user.setPassword("test1");
        userDao.insertUser(user);

    }

    @Test
    public void selectAllUser() throws Exception {
        List<User> users = userDao.selectAllUser();
        for (User user : users) {
            System.out.println(user);

        }
    }

}