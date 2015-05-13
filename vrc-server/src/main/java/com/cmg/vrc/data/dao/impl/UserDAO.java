package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.data.jdo.UserJDO;

import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class UserDAO extends DataAccess<UserJDO, User> {

    public UserDAO() {
        super(UserJDO.class, User.class);
    }

    public User getUserByEmail(String email) throws Exception {
        List<User> userList = list("WHERE username == :1", email);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public User getUserByEmailPassword(String email, String password) throws Exception{
        List<User> userList = list("WHERE username == :1 && password == :2", email, password);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public User getUserByValidationCode(String code) throws Exception {
        List<User> userList = list("WHERE activationCode == :c", code);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }
}
