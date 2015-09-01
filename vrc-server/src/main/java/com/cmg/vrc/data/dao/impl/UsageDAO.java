package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Usage;
import com.cmg.vrc.data.jdo.UsageJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.*;

/**
 * Created by luhonghai on 4/13/15.
 */
public class UsageDAO extends DataAccess<UsageJDO, Usage> {

    public UsageDAO() {
        super(UsageJDO.class, Usage.class);
    }
    public List<Usage> listAll(int start, int length,String search,int column,String order,String name) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
//        Transaction tx = pm.currentTransaction();
        List<Usage> list = new ArrayList<Usage>();
        Query q = pm.newQuery("SELECT FROM " + UsageJDO.class.getCanonicalName());
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
//            tx.begin();
            List<UsageJDO> tmp = (List<UsageJDO>)q.execute(name,search);
            Iterator<UsageJDO> iter = tmp.iterator();
            while (iter.hasNext()) {
                list.add(to(iter.next()));
            }
//            tx.commit();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
            q.closeAll();
            pm.close();
        }
    }

    public double getCountSearch(String search, String name) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
//        Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + UsageJDO.class.getCanonicalName());
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
//            tx.begin();
            count = (Long) q.execute(search,name);
//            tx.commit();
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
            q.closeAll();
            pm.close();
        }
    }
}
