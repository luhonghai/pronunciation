package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Security;
import com.cmg.vrc.data.jdo.SecurityJDO;

/**
 * Created by luhonghai on 4/13/15.
 */
public class SecurityDAO extends DataAccess<SecurityJDO, Security> {

    public SecurityDAO() {
        super(SecurityJDO.class, Security.class);
    }
}
