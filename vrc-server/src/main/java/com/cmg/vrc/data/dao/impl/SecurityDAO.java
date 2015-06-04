package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Security;
import com.cmg.vrc.data.jdo.SecurityJDO;
import com.cmg.vrc.data.jdo.UserDevice;

import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class SecurityDAO extends DataAccess<SecurityJDO, Security> {

    public SecurityDAO() {
        super(SecurityJDO.class, Security.class);
    }

    public Security getByAccount(String account) throws Exception {
        List<Security> securities = list("WHERE username == :c", account);
        if (securities != null && securities.size() > 0)
            return securities.get(0);
        return null;
    }
}
