package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
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


    public List<String> getListGCMID(String username) throws Exception {
        List<String> lstListGCM = new ArrayList<>();
        /*SELECT DISTINCT USER_DEVICE.GCMID
FROM SAT.USER_DEVICE INNER JOIN (SELECT DISTINCT SAT.USAGE.EMEI FROM  SAT.USAGE WHERE USERNAME = 'pablo.dropbox01@gmail.com') AS USAGETMP
ON SAT.USER_DEVICE.EMEI = USAGETMP.EMEI*/
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL","SELECT DISTINCT USER_DEVICE.GCMID\n" +
                "FROM USER_DEVICE INNER JOIN (SELECT DISTINCT `USAGE`.EMEI FROM  `USAGE` WHERE USERNAME = ? ) AS USAGETMP\n" +
                "ON USER_DEVICE.EMEI = USAGETMP.EMEI WHERE USER_DEVICE.GCMID IS NOT NULL AND USER_DEVICE.GCMID!=''");
        List<String> tmp = (List<String>) q.execute(username);
        if(tmp!=null && tmp.size() > 0){
            for(String obj : tmp){
                lstListGCM.add(obj);
            }
        }
        return  lstListGCM;
    }

}
