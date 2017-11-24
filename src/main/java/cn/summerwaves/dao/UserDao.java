package cn.summerwaves.dao;

import cn.summerwaves.model.AccessToken;
import cn.summerwaves.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    void insertUser(User user);

    List<User> selectAllUser();

    AccessToken selectAccessToken();

    void updateAccessToken(AccessToken accessToken);
}
