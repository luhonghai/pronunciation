package com.cmg.vrc.data.dao.impl;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LicenseCode;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

/**
 * Created by luhonghai on 4/13/15.
 */
public class UserDAO extends DataAccess<User> {

    public UserDAO() {
        super(User.class);
    }

    public User getUserByResetPasswordCode(String code) throws Exception {
        List<User> userList = list("WHERE resetPasswordCode == :1", code);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public User getUserByEmail(String email) throws Exception {
        List<User> userList = list("WHERE username == :1", email);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public User getUserByEmailPassword(String email, String password) throws Exception{
        List<User> userList = list("WHERE username == :1 && password == :2", email, password);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public User getUserByValidationCode(String code) throws Exception {
        List<User> userList = list("WHERE activationCode == :c", code);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public List<User> users(){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaUser = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(User.class.getCanonicalName());
        TypeMetadata metaLicenseCode = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LicenseCode.class.getCanonicalName());
        List<User> users=new ArrayList<User>();
        Query q = pm.newQuery("javax.jdo.query.SQL","SELECT id, username, createdDate FROM " + metaUser + "WHERE USERNAME not IN (select DISTINCT ACCOUNT AS username FROM" + metaLicenseCode + "where ACCOUNT is not null)");
        try {
            users=(List<User>)q.execute();
            return users;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }


    public List<User> listAll(int start, int length,String search,int column,String order,String user,String fullname, String gender,String country,String acti,Date dateFrom,Date dateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + User.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((username.toLowerCase().indexOf(search.toLowerCase()) != -1)||(name.toLowerCase().indexOf(search.toLowerCase()) != -1)||(country.toLowerCase().indexOf(search.toLowerCase()) != -1)||(activationCode.toLowerCase().indexOf(search.toLowerCase()) != -1))";


        String b="((username == null || username.toLowerCase().indexOf(search.toLowerCase()) != -1)||(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)||(country == null || country.toLowerCase().indexOf(search.toLowerCase()) != -1)||(activationCode == null || activationCode.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(user.length()>0){
            string.append("(username.toLowerCase().indexOf(user.toLowerCase()) != -1) &&");
        }
        if(fullname.length()>0){
            string.append("(name.toLowerCase().indexOf(fullname.toLowerCase()) != -1) &&");
        }
        if(gender.equals("Man")){
            string.append("gender==true &&");
        }
        if(gender.equals("WoMan")){
            string.append("gender==false &&");
        }
        if(country.length()>0){
            string.append("(country.toLowerCase().indexOf(country.toLowerCase()) != -1) &&");
        }
        if(acti.equals("Yes")){
            string.append("isActivated==true &&");
        }
        if(acti.equals("No")){
            string.append("isActivated==false &&");
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
        q.declareParameters("String search, String user,String fullname,String country,java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("user", user);
        params.put("fullname", fullname);
        params.put("country", country);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("username asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("username desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("name asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("name desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("loginType asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("loginType desc");
        }
        if (column==3 && order.equals("asc")) {
            q.setOrdering("gender asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("gender desc");
        }
        if (column==4 && order.equals("asc")) {
            q.setOrdering("dob asc");
        }else if(column==4 && order.equals("desc")) {
            q.setOrdering("dob desc");
        }
        if (column==5 && order.equals("asc")) {
            q.setOrdering("createdDate asc");
        }else if(column==5 && order.equals("desc")) {
            q.setOrdering("createdDate desc");
        }
        if (column==6 && order.equals("asc")) {
            q.setOrdering("country asc");
        }else if(column==6 && order.equals("desc")) {
            q.setOrdering("country desc");
        }
        if (column==7 && order.equals("asc")) {
            q.setOrdering("activationCode asc");
        }else if(column==7 && order.equals("desc")) {
            q.setOrdering("activationCode desc");
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


    public double getCountSearch(String search,String user,String fullname, String gender,String country,String acti,Date dateFrom,Date dateTo ) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + User.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((username.toLowerCase().indexOf(search.toLowerCase()) != -1)||(name.toLowerCase().indexOf(search.toLowerCase()) != -1)||(country.toLowerCase().indexOf(search.toLowerCase()) != -1)||(activationCode.toLowerCase().indexOf(search.toLowerCase()) != -1))";


        String b="((username == null || username.toLowerCase().indexOf(search.toLowerCase()) != -1)||(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)||(country == null || country.toLowerCase().indexOf(search.toLowerCase()) != -1)||(activationCode == null || activationCode.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(user.length()>0){
            string.append("(username.toLowerCase().indexOf(user.toLowerCase()) != -1) &&");
        }
        if(fullname.length()>0){
            string.append("(name.toLowerCase().indexOf(fullname.toLowerCase()) != -1) &&");
        }
        if(gender.equals("Man")){
            string.append("gender==true &&");
        }
        if(gender.equals("WoMan")){
            string.append("gender==false &&");
        }
        if(country.length()>0){
            string.append("(country.toLowerCase().indexOf(country.toLowerCase()) != -1) &&");
        }
        if(acti.equals("Yes")){
            string.append("isActivated==true &&");
        }
        if(acti.equals("No")){
            string.append("isActivated==false &&");
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
        q.declareParameters("String search, String user,String fullname,String country,java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("user", user);
        params.put("fullname", fullname);
        params.put("country", country);
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
