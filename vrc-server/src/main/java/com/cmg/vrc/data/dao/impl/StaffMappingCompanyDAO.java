package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.AppDetail;

import com.cmg.vrc.data.jdo.TeacherMappingCompany;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 4/13/15.
 */
public class StaffMappingCompanyDAO extends DataAccess<TeacherMappingCompany> {

    public StaffMappingCompanyDAO() {
        super(TeacherMappingCompany.class);
    }

    public List<TeacherMappingCompany> listAll() throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + TeacherMappingCompany.class.getCanonicalName());

        try {
            return detachCopyAllList(pm, q.execute());
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public List<TeacherMappingCompany> getByStaff(String staff) throws Exception {
        List<TeacherMappingCompany> userList = list("WHERE StaffName == :1, idDelete == :2", staff, false);
        return userList;
    }
    public List<TeacherMappingCompany> getByIdCompany(String idCompany) throws Exception {
        List<TeacherMappingCompany> userList = list("WHERE idCompany == :1", idCompany);
        return userList;
    }

    public double getCountSearch(String search,String user) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + TeacherMappingCompany.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((company.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((company == null || company.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(user.length()>0){
            string.append(" StaffName==user &&");
        }
        string.append("(isDeleted==false) &&");
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String user");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("user", user);
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
    public List<TeacherMappingCompany> listAll(int start, int length,String search,int column,String order,String user) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + TeacherMappingCompany.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((company.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((company == null || company.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(user.length()>0){
            string.append(" StaffName == user &&");
        }
        string.append(" (isDeleted==false) &&");
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String user");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("user", user);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("company asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("company desc");
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
    public void updateEdit(String username) {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        TypeMetadata metadata = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","UPDATE " +metadata.getTable()+ " SET isDeleted=true WHERE StaffName='"+username+"' ");
        try {
            tx.begin();
            q.execute();
            tx.commit();
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
    public void deleteStaff(String username) {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        TypeMetadata metadata = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","DELETE FROM " +metadata.getTable()+ " WHERE StaffName='"+username+"' ");
        try {
            tx.begin();
            q.execute();
            tx.commit();
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
