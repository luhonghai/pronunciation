package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.ClassJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CMGT400 on 6/8/2015.
 */
public class ClassDAO extends DataAccess<ClassJDO> {
    public ClassDAO(){
        super(ClassJDO.class);
    }

    public ClassJDO getUserByEmailPassword(String email, String password) throws Exception{
        List<ClassJDO> userList = list("WHERE userName == :1 && password == :2", email, password);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }
    public ClassJDO getUserByEmail(String email) throws Exception {
        List<ClassJDO> userList = list("WHERE userName == :1", email);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public List<ClassJDO> listAll(int start, int length,String search,int column,String order,String classNames,Date dateFrom,Date dateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Admin.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((className.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((className == null || className.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(classNames.length()>0){
            string.append("(className.toLowerCase().indexOf(classNames.toLowerCase()) != -1) &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append("(createdDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append("(createdDate <= dateTo) &&");
        }


        if(dateFrom!=null&&dateTo!=null){
            string.append("(createdDate >= dateFrom && createdDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String classNames, java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("classNames", classNames);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("classNames asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("classNames desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("createdDate asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("createdDate desc");
        }
        q.setRange(start, start + length);
        try {
            return detachCopyAllList(pm, q.executeWithMap(params));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public double getCountSearch(String search,String classNames,Date dateFrom,Date dateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Admin.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((className.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((className == null || className.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(classNames.length()>0){
            string.append("(className.toLowerCase().indexOf(classNames.toLowerCase()) != -1) &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append("(createdDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append("(createdDate <= dateTo) &&");
        }


        if(dateFrom!=null&&dateTo!=null){
            string.append("(createdDate >= dateFrom && createdDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String classNames, java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("classNames", classNames);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);
        try {
            count = (Long) q.executeWithMap(params);
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }


}
