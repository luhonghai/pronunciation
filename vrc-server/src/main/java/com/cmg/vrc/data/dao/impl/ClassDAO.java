package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.ClassJDO;
import com.cmg.vrc.data.jdo.ClassMappingTeacher;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

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

    public List<ClassJDO> listAll(String teacherName) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();

        StringBuffer query = new StringBuffer();
        TypeMetadata metaClassJDO = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ClassJDO.class.getCanonicalName());
        TypeMetadata metaClassMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ClassMappingTeacher.class.getCanonicalName());
        String firstQuery = "select class.id, class.className , class.definition, class.createdDate from  " + metaClassJDO.getTable()
                + " class inner join " + metaClassMappingTeacher.getTable()
                + " mapping on mapping.idClass=class.id where teacherName='"+teacherName+"'";
        query.append(firstQuery);
        query.append(" ORDER BY class.createdDate DESC");
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());

        List<ClassJDO> list = new ArrayList<ClassJDO>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            for (Object obj : tmp) {
                ClassJDO classJDO = new ClassJDO();
                Object[] array = (Object[]) obj;
                if (array[0].toString().length() > 0) {
                    classJDO.setId(array[0].toString());
                } else {
                    classJDO.setId(null);
                }
                if (array[1] != null) {
                    classJDO.setClassName(array[1].toString());
                } else {
                    classJDO.setClassName(null);
                }
                if (array[2] != null) {
                    classJDO.setDefinition(array[2].toString());
                } else {
                    classJDO.setDefinition(null);
                }
                if (array[3] != null) {
                    classJDO.setCreatedDate((Date) array[3]);
                } else {
                    classJDO.setCreatedDate(null);
                }
                list.add(classJDO);

            }

            return list;
        } catch (Exception e) {
            throw e;
        } finally {

            q.closeAll();
            pm.close();
        }


    }

    public List<ClassJDO> getCountSearch(String search,int column,String order,String username,String classNames,Date dateFrom,Date dateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();

        StringBuffer query = new StringBuffer();
        TypeMetadata metaClassJDO = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ClassJDO.class.getCanonicalName());
        TypeMetadata metaClassMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ClassMappingTeacher.class.getCanonicalName());
        String firstQuery = "select class.id, class.className , class.definition, class.createdDate from  " + metaClassJDO.getTable()
                + " class inner join " + metaClassMappingTeacher.getTable()
                + " mapping on mapping.idClass=class.id where ";
        query.append(firstQuery);
        query.append(" (class.className LIKE '%" + search + "%')");
        query.append(" and mapping.teacherName ='"+username+"'");
        query.append(" and mapping.isDeleted =false");
        query.append(" and class.isDeleted =false");
        if (classNames.length() > 0) {
            query.append(" and class.className LIKE '%" + classNames + "%'");
        }
        if (dateFrom!=null && dateTo==null) {
            query.append(" and class.createdDate >= '" + dateFrom + "'");
        }
        if (dateFrom==null && dateTo!=null) {
            query.append(" and  class.createdDate <= '" + dateTo + "'");
        }

        if (dateFrom!=null && dateTo!=null) {
            query.append(" and  class.createdDate >= '" + dateFrom + "' and  class.createdDate <= '" + dateTo + "'");
        }


        if (column == 0 && order.equals("asc")) {
            query.append(" ORDER BY class.className ASC");
        } else if (column == 0 && order.equals("desc")) {
            query.append(" ORDER BY class.className DESC");
        }
        if (column == 2 && order.equals("asc")) {
            query.append(" ORDER BY class.createdDate ASC");
        } else if (column == 2 && order.equals("desc")) {
            query.append(" ORDER BY class.createdDate DESC");
        }
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());

        List<ClassJDO> list = new ArrayList<ClassJDO>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            for (Object obj : tmp) {
                ClassJDO classJDO = new ClassJDO();
                Object[] array = (Object[]) obj;
                if (array[0].toString().length() > 0) {
                    classJDO.setId(array[0].toString());
                } else {
                    classJDO.setId(null);
                }
                if (array[1] != null) {
                    classJDO.setClassName(array[1].toString());
                } else {
                    classJDO.setClassName(null);
                }
                if (array[2] != null) {
                    classJDO.setDefinition(array[2].toString());
                } else {
                    classJDO.setDefinition(null);
                }
                if (array[3] != null) {
                    classJDO.setCreatedDate((Date) array[3]);
                } else {
                    classJDO.setCreatedDate(null);
                }
                list.add(classJDO);

            }

            return list;
        } catch (Exception e) {
            throw e;
        } finally {

            q.closeAll();
            pm.close();
        }
    }
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT MAX(version) FROM " + ClassJDO.class.getCanonicalName());
        try {
            if (getCount()!=0 && q != null) {
                version = (int) q.execute();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return version;
    }

    public double getCount() throws  Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + ClassJDO.class.getCanonicalName());
        try {
            count = (Long) q.execute();
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }



}
