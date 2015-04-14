package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.data.jdo.UserDeviceJDO;

/**
 * Created by luhonghai on 4/13/15.
 */
public class UserDeviceDAO extends DataAccess<UserDeviceJDO, UserDevice> {

    public UserDeviceDAO() {
        super(UserDeviceJDO.class, UserDevice.class);
    }
}
