package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Usage;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class UsageDAO extends DataAccess<Usage> {

    public UsageDAO() {
        super(Usage.class);
    }
    public List<Usage> listAll(int start, int length,String search,int column,String order,String name) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Usage.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((username.toLowerCase().indexOf(search.toLowerCase()) != -1)||(imei.toLowerCase().indexOf(search.toLowerCase()) != -1) ||(appVersion.toLowerCase().indexOf(search.toLowerCase()) != -1))&&";
        String b="((username == null || username.toLowerCase().indexOf(search.toLowerCase()) != -1)||(imei == null || imei.toLowerCase().indexOf(search.toLowerCase()) != -1) ||(appVersion == null || appVersion.toLowerCase().indexOf(search.toLowerCase()) != -1))&&";
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        if(name.length() > 0) {
            string.append("(username==name)");
        }
        q.setFilter(string.toString());
        q.declareParameters("String name,String search");
        if (column==0 && order.equals("asc")) {
            q.setOrdering("username asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("username desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("imei asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("imei desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("appVersion asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("appVersion desc");
        }

        try {
            List<Usage> tmp = (List<Usage>)q.execute(name,search);
            pm.detachCopyAll(tmp);
            return tmp;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public double getCountSearch(String search, String name) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Usage.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((username.toLowerCase().indexOf(search.toLowerCase()) != -1)||(imei.toLowerCase().indexOf(search.toLowerCase()) != -1) ||(appVersion.toLowerCase().indexOf(search.toLowerCase()) != -1))&&";
        String b="((username == null || username.toLowerCase().indexOf(search.toLowerCase()) != -1)||(imei == null || imei.toLowerCase().indexOf(search.toLowerCase()) != -1) ||(appVersion == null || appVersion.toLowerCase().indexOf(search.toLowerCase()) != -1))&&";
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        if(name.length() > 0) {
            string.append("(username==name)");
        }
        q.setFilter(string.toString());
        q.declareParameters("String search ,String name ");

        try {
            count = (Long) q.execute(search,name);
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
}
