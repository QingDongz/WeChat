package cn.summerwaves.service;

import cn.summerwaves.dao.UserDao;
import cn.summerwaves.model.AccessToken;
import cn.summerwaves.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<User> selectAllUser();

    AccessToken selectAccessToken();

    void updateAccessToken(AccessToken accessToken);
}
