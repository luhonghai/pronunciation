package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.AppDetail;
import com.cmg.vrc.data.jdo.TeacherMappingCompany;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class TeacherMappingCompanyDAO extends DataAccess<TeacherMappingCompany> {

    public TeacherMappingCompanyDAO() {
        super(TeacherMappingCompany.class);
    }
    public List<TeacherMappingCompany> getByIdCompany(String idCompany) throws Exception {
        List<TeacherMappingCompany> userList = list("WHERE idCompany == :1", idCompany);
        return userList;
    }
    public List<TeacherMappingCompany> getByCompany(String company) throws Exception {
        List<TeacherMappingCompany> userList = list("WHERE company == :1", company);
        return userList;
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
    public TeacherMappingCompany getByCompanyAndTeacher(String idCompany, String teacherName) throws Exception{
        List<TeacherMappingCompany> userList = list("WHERE idCompany == :1 && teacherName == :2", idCompany, teacherName);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public void updateEdit(String username) {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        TypeMetadata metadata = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","UPDATE " +metadata.getTable()+ " SET isDeleted=true WHERE teacherName='"+username+"' ");
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
