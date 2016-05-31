package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.TeacherMappingCompany;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CMGT400 on 6/8/2015.
 */
public class AdminDAO extends DataAccess<Admin> {
    public AdminDAO(){
        super(Admin.class);
    }

    public Admin getUserByEmailPassword(String email, String password) throws Exception{
        List<Admin> userList = list("WHERE userName.toLowerCase() == :1 && password == :2", email, password);
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
    public Admin getUserByTeacher(String email) throws Exception {
        List<Admin> userList = list("WHERE userName == :1 && role == :2", email,4);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    /**
     *
     * @param tmp
     * @return
     */
    public List<Admin> addedCompanyToUser(List<Admin> tmp){
        try {
            if(tmp!=null && tmp.size() > 0){
                TeacherMappingCompanyDAO tmcDao = new TeacherMappingCompanyDAO();
                for(Admin ad : tmp){
                    if(ad.getRole() == Constant.ROLE_TEACHER){
                        TeacherMappingCompany tcm = tmcDao.getCompanyByTeacherName(ad.getUserName());
                        if(tcm!=null) ad.setIdCompany(tcm.getIdCompany());
                    }else if(ad.getRole() == Constant.ROLE_STAFF){
                        TeacherMappingCompany tcm = tmcDao.getCompanyByTeacherName(ad.getUserName());
                        if(tcm!=null) ad.setIdCompany(tcm.getIdCompany());
                    }else{
                        ad.setIdCompany("not set");
                    }
                }
            }
        }catch (Exception e){
        }
        return tmp;
    }
    public List<Admin> listAll(int start, int length,String search,int column,String order,String user,String fisrt,String last) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Admin.class.getCanonicalName());
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
            return detachCopyAllList(pm, q.executeWithMap(params));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public double getCountSearch(String search,String user,String fisrt,String last) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Admin.class.getCanonicalName());
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
