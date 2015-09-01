package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Security;

import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class SecurityDAO extends DataAccess<Security> {

    public SecurityDAO() {
        super(Security.class);
    }

    public Security getByAccount(String account) throws Exception {
        List<Security> securities = list("WHERE username == :c", account);
        if (securities != null && securities.size() > 0)
            return securities.get(0);
        return null;
    }
}
