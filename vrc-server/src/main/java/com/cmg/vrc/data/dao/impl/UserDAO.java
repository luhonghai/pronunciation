package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.data.jdo.UserJDO;

/**
 * Created by luhonghai on 4/13/15.
 */
public class UserDAO extends DataAccess<UserJDO, User> {

    public UserDAO() {
        super(UserJDO.class, User.class);
    }
}
