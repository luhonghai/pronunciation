package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.data.jdo.UserDeviceJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.*;


public class UserDeviceDAO extends DataAccess<UserDeviceJDO, UserDevice> {

    public UserDeviceDAO() {
        super(UserDeviceJDO.class, UserDevice.class);
    }


    public UserDevice getDeviceByIMEI(String imei) throws Exception {
        List<UserDevice> devices = list("WHERE imei == :c", imei);
        if (devices != null && devices.size() > 0)
            return devices.get(0);
        return null;
    }
}
