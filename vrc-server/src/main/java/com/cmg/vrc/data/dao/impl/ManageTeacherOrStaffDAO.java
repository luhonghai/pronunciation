package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.StaffMappingCompany;
import com.cmg.vrc.data.jdo.TeacherMappingCompany;
import com.cmg.vrc.data.jdo.TeacherOrStaffList;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class ManageTeacherOrStaffDAO {
    public List<TeacherOrStaffList> listAll(int start, int length,String search,int column,String order,String idCompany) {
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        TypeMetadata metaStaff = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StaffMappingCompany.class.getCanonicalName());
        StringBuffer query = new StringBuffer();
        StringBuffer first = new StringBuffer();
        StringBuffer second = new StringBuffer();
        String firstQuery = "select id,teacherName as username , idCompany, company ,'Teacher' as role  from  " + metaTeacher.getTable() + " where idCompany='"+idCompany+"' and isDeleted=false";
        String secondQuery = "select id,StaffName as username , idCompany, company , 'Staff' as role from  " + metaStaff.getTable() + " where idCompany='"+idCompany+"' and isDeleted=false";
        first.append(firstQuery);
        second.append(secondQuery);
        first.append(" and (teacherName LIKE '%" + search + "%')");
        second.append(" and (StaffName LIKE '%" + search + "%')");
        query.append("select * from ("+ first + " UNION " + second + ") as tmp ");
        if (column == 0 && order.equals("asc")) {
            query.append(" ORDER BY tmp.username ASC");
        } else if (column == 0 && order.equals("desc")) {
            query.append(" ORDER BY tmp.username DESC");
        }
        if (column == 1 && order.equals("asc")) {
            query.append(" ORDER BY tmp.role ASC");
        } else if (column == 1 && order.equals("desc")) {
            query.append(" ORDER BY tmp.role DESC");
        }
        query.append(" limit " + start + "," + length);
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        List<TeacherOrStaffList> teacherOrStaffLists=new ArrayList<>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            for (Object obj : tmp) {
                TeacherOrStaffList teacherOrStaffList = new TeacherOrStaffList();
                Object[] array = (Object[]) obj;
                if (array[0].toString().length() > 0) {
                    teacherOrStaffList.setId(array[0].toString());
                } else {
                    teacherOrStaffList.setId(null);
                }
                if (array[1] != null) {
                    teacherOrStaffList.setUserName(array[1].toString());
                } else {
                    teacherOrStaffList.setUserName(null);
                }
                if (array[2] != null) {
                    teacherOrStaffList.setIdCompany(array[2].toString());
                } else {
                    teacherOrStaffList.setIdCompany(null);
                }
                if (array[3] != null) {
                    teacherOrStaffList.setCompany(array[3].toString());
                } else {
                    teacherOrStaffList.setCompany(null);
                }
                if (array[4] != null) {
                    teacherOrStaffList.setRole(array[4].toString());
                } else {
                    teacherOrStaffList.setRole(null);
                }

                teacherOrStaffLists.add(teacherOrStaffList);
            }

            return teacherOrStaffLists;
        } catch (Exception e) {
            return null;
        } finally {

            q.closeAll();
            pm.close();
        }
    }


    public List<TeacherOrStaffList> listAll(String search,int column,String order,String idCompany) {
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        TypeMetadata metaStaff = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StaffMappingCompany.class.getCanonicalName());
        StringBuffer query = new StringBuffer();
        StringBuffer first = new StringBuffer();
        StringBuffer second = new StringBuffer();
        String firstQuery = "select id,teacherName as username, idCompany, company ,'Teacher' as role  from  " + metaTeacher.getTable() + " where idCompany='"+idCompany+"' and isDeleted=false ";
        String secondQuery = "select id,StaffName as username, idCompany, company , 'Staff' as role from  " + metaStaff.getTable() + " where idCompany='"+idCompany+"' and isDeleted=false";
        first.append(firstQuery);
        second.append(secondQuery);
        first.append(" and (teacherName LIKE '%" + search + "%')");
        second.append(" and (StaffName LIKE '%" + search + "%')");
        query.append("select * from ("+ first + " UNION " + second + ") as tmp ");
        if (column == 0 && order.equals("asc")) {
            query.append(" ORDER BY tmp.username ASC");
        } else if (column == 0 && order.equals("desc")) {
            query.append(" ORDER BY tmp.username DESC");
        }
        if (column == 1 && order.equals("asc")) {
            query.append(" ORDER BY tmp.role ASC");
        } else if (column == 1 && order.equals("desc")) {
            query.append(" ORDER BY tmp.role DESC");
        }
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        List<TeacherOrStaffList> teacherOrStaffLists=new ArrayList<>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            for (Object obj : tmp) {
                TeacherOrStaffList teacherOrStaffList = new TeacherOrStaffList();
                Object[] array = (Object[]) obj;
                if (array[0].toString().length() > 0) {
                    teacherOrStaffList.setId(array[0].toString());
                } else {
                    teacherOrStaffList.setId(null);
                }
                if (array[1] != null) {
                    teacherOrStaffList.setUserName(array[1].toString());
                } else {
                    teacherOrStaffList.setUserName(null);
                }
                if (array[2] != null) {
                    teacherOrStaffList.setIdCompany(array[2].toString());
                } else {
                    teacherOrStaffList.setIdCompany(null);
                }
                if (array[3] != null) {
                    teacherOrStaffList.setCompany(array[3].toString());
                } else {
                    teacherOrStaffList.setCompany(null);
                }
                if (array[4] != null) {
                    teacherOrStaffList.setRole(array[4].toString());
                } else {
                    teacherOrStaffList.setRole(null);
                }

                teacherOrStaffLists.add(teacherOrStaffList);
            }

            return teacherOrStaffLists;
        } catch (Exception e) {
            return null;
        } finally {

            q.closeAll();
            pm.close();
        }
    }



}
