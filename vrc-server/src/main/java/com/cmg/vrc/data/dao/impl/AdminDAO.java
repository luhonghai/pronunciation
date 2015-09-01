package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.AdminJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.*;

/**
 * Created by CMGT400 on 6/8/2015.
 */
public class AdminDAO extends DataAccess<AdminJDO,Admin> {
    public AdminDAO(){
        super(AdminJDO.class,Admin.class);
    }

    public Admin getUserByEmailPassword(String email, String password) throws Exception{
        List<Admin> userList = list("WHERE userName == :1 && password == :2", email, password);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }
    public Admin getUserByEmail(String email) throws Exception {
        List<Admin> userList = list("WHERE userName == :1", email);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public List<Admin> listAll(int start, int length,String search,int column,String order,String user,String fisrt,String last) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        //Transaction tx = pm.currentTransaction();
        List<Admin> list = new ArrayList<Admin>();
        Query q = pm.newQuery("SELECT FROM " + AdminJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((userName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(firstName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(lastName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((userName == null || userName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(firstName == null || firstName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(lastName == null || lastName.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(user.length()>0){
            string.append("(userName.toLowerCase().indexOf(user.toLowerCase()) != -1) &&");
        }
        if(fisrt.length()>0){
            string.append("(firstName.toLowerCase().indexOf(fisrt.toLowerCase()) != -1) &&");
        }
        if(last.length()>0){
            string.append("(lastName.toLowerCase().indexOf(last.toLowerCase()) != -1) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String user, String fisrt, String last");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("user", user);
        params.put("fisrt", fisrt);
        params.put("last", last);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("userName asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("userName desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("firstName asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("firstName desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("lastName asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("lastName desc");
        }
        if (column==3 && order.equals("asc")) {
            q.setOrdering("role asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("role desc");
        }

        q.setRange(start, start + length);

        try {
            //tx.begin();
            List<AdminJDO> tmp = (List<AdminJDO>)q.executeWithMap(params);
            Iterator<AdminJDO> iter = tmp.iterator();
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

    public double getCountSearch(String search,String user,String fisrt,String last) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        //Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + AdminJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((userName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(firstName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(lastName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((userName == null || userName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(firstName == null || firstName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(lastName == null || lastName.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(user.length()>0){
            string.append("(userName.toLowerCase().indexOf(user.toLowerCase()) != -1) &&");
        }
        if(fisrt.length()>0){
            string.append("(firstName.toLowerCase().indexOf(fisrt.toLowerCase()) != -1) &&");
        }
        if(last.length()>0){
            string.append("(lastName.toLowerCase().indexOf(last.toLowerCase()) != -1) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String user, String fisrt, String last");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("user", user);
        params.put("fisrt", fisrt);
        params.put("last", last);
        try {
            //tx.begin();
            count = (Long) q.executeWithMap(params);
            //tx.commit();
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
