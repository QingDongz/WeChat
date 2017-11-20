package cn.summerwaves.service.impl;

import cn.summerwaves.dao.UserDao;
import cn.summerwaves.model.User;
import cn.summerwaves.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    public List<User> selectAllUser() {
        return userDao.selectAllUser();
    }
}
