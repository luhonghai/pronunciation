package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.AppDetail;
import com.cmg.vrc.data.jdo.AppDetailJDO;

/**
 * Created by luhonghai on 4/13/15.
 */
public class AppDetailDAO extends DataAccess<AppDetailJDO, AppDetail> {

    public AppDetailDAO() {
        super(AppDetailJDO.class, AppDetail.class);
    }
}
