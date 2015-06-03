package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
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
    public List<UserDevice> listAll(int start, int length,String search,int column,String order,String emei,String model,String osVersion,String osApiLevel,String deviceName, String emeisearch,Date dateFrom,Date dateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<UserDevice> list = new ArrayList<UserDevice>();
        Query q = pm.newQuery("SELECT FROM " + UserDeviceJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((model.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(osVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(osApiLevel.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(deviceName.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(emei.toLowerCase().indexOf(search.toLowerCase()) != -1))&& ";
        String b="((model == null || model.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(osVersion == null || osVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(osApiLevel == null || osApiLevel.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(deviceName == null || deviceName.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(emei == null || emei.toLowerCase().indexOf(search.toLowerCase()) != -1))&&";


        if(model.length()>0){
            string.append("(model.toLowerCase().indexOf(model.toLowerCase()) != -1) &&");
        }
        if(osVersion.length()>0){
            string.append("(osVersion.toLowerCase().indexOf(osVersion.toLowerCase()) != -1) &&");
        }
        if(osApiLevel.length()>0){
            string.append("(osApiLevel.toLowerCase().indexOf(osApiLevel.toLowerCase()) != -1) &&");
        }
        if(deviceName.length()>0){
            string.append("(deviceName.toLowerCase().indexOf(deviceName.toLowerCase()) != -1) &&");
        }
        if(emeisearch.length()>0){
            string.append("(emei.toLowerCase().indexOf(emeisearch.toLowerCase()) != -1) &&");
        }

        if(dateFrom!=null&&dateTo!=null){
            string.append("(attachedDate >= dateFrom && attachedDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        if(emei.length()>0){
            string.append("(emei==emei)");
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String emei,String model,String osVersion,String osApiLevel,String deviceName, String emeisearch,java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("emei", emei);
        params.put("model", model);
        params.put("osVersion", osVersion);
        params.put("osApiLevel", osApiLevel);
        params.put("deviceName", deviceName);
        params.put("emeisearch", emeisearch);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("model asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("model desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("osVersion asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("osVersion desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("osApiLevel asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("osApiLevel desc");
        }
        if (column==3 && order.equals("asc")) {
            q.setOrdering("deviceName asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("deviceName desc");
        }
        if (column==4 && order.equals("asc")) {
            q.setOrdering("emei asc");
        }else if(column==4 && order.equals("desc")) {
            q.setOrdering("emei desc");
        }
        if (column==5 && order.equals("asc")) {
            q.setOrdering("attachedDate asc");
        }else if(column==5 && order.equals("desc")) {
            q.setOrdering("attachedDate desc");
        }

        q.setRange(start, start + length);

        try {
            tx.begin();
            List<UserDeviceJDO> tmp = (List<UserDeviceJDO>)q.executeWithMap(params);
            Iterator<UserDeviceJDO> iter = tmp.iterator();
            while (iter.hasNext()) {
                list.add(to(iter.next()));
            }
            tx.commit();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            q.closeAll();
            pm.close();
        }
    }

}
