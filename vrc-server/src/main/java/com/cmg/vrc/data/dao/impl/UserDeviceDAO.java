package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.UserDevice;

import java.util.List;


public class UserDeviceDAO extends DataAccess<UserDevice> {

    public UserDeviceDAO() {
        super(UserDevice.class);
    }


    public UserDevice getDeviceByIMEI(String imei) throws Exception {
        List<UserDevice> devices = list("WHERE imei == :c", imei);
        if (devices != null && devices.size() > 0)
            return devices.get(0);
        return null;
    }

}
