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

    /**
     *
     * @param imei
     * @return
     * @throws Exception
     */
    public UserDevice getDeviceByIMEI(String imei) throws Exception {
        List<UserDevice> devices = list("WHERE imei == :c", imei);
        if (devices != null && devices.size() > 0)
            return devices.get(0);
        return null;
    }

    /**
     *
     * @param username
     * @return
     * @throws Exception
     */
    public List<String> getListGCMID(String username) throws Exception {
        List<String> lstListGCM = new ArrayList<>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        /*Query q = pm.newQuery("javax.jdo.query.SQL","SELECT DISTINCT USER_DEVICE.GCMID " +
                "FROM USER_DEVICE INNER JOIN (SELECT DISTINCT `USAGE`.EMEI FROM  `USAGE` WHERE USERNAME = ? ) AS USAGETMP " +
                "ON USER_DEVICE.EMEI = USAGETMP.EMEI WHERE USER_DEVICE.GCMID IS NOT NULL AND USER_DEVICE.GCMID!=''");*/
        String query = "SELECT USER_DEVICE.GCMID FROM USER_DEVICE WHERE userLastLogin = ?";
        Query q = pm.newQuery("javax.jdo.query.SQL",query);
        try {
            List<String> tmp = (List<String>) q.execute(username);
            if(tmp!=null && tmp.size() > 0){
                for(String obj : tmp){
                    lstListGCM.add(obj);
                }
            }
        }catch (Exception e){
        }finally {
            q.closeAll();
            pm.close();
        }
        return  lstListGCM;
    }

}
