package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LoginToken;
import com.cmg.vrc.data.jdo.Security;

import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class LoginTokenDAO extends DataAccess<LoginToken> {

    public LoginTokenDAO() {
        super(LoginToken.class);
    }

    public LoginToken getByAccountAndDevice(String account, String device) throws Exception {
        List<LoginToken> loginTokens = list("WHERE username == :1 && deviceName == :2", account, device);
        if (loginTokens != null && loginTokens.size() > 0)
            return loginTokens.get(0);
        return null;
    }
}
