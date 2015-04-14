package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Usage;
import com.cmg.vrc.data.jdo.UsageJDO;

/**
 * Created by luhonghai on 4/13/15.
 */
public class UsageDAO extends DataAccess<UsageJDO, Usage> {

    public UsageDAO() {
        super(UsageJDO.class, Usage.class);
    }
}
