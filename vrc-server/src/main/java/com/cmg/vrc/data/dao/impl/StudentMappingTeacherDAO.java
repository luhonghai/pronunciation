package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class StudentMappingTeacherDAO extends DataAccess<StudentMappingTeacher> {

    public StudentMappingTeacherDAO() {
        super(StudentMappingTeacher.class);
    }

    public List<StudentMappingTeacher> listAll() throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + StudentMappingTeacher.class.getCanonicalName());

        try {
            return detachCopyAllList(pm, q.execute());
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public List<StudentMappingTeacher> getByTeacherName(String teacherName) throws Exception {
        List<StudentMappingTeacher> listStudent = list("WHERE teacherName == :1 && status == :2", teacherName, "accept");
        return listStudent;
    }

    public StudentMappingTeacher getByStudentAndTeacher(String student,String teacherName) throws Exception {
        List<StudentMappingTeacher> listStudent = list("WHERE studentName == :1 && teacherName == :2 && isDeleted == :3",student, teacherName,false);
        if (listStudent != null && listStudent.size() > 0)
            return listStudent.get(0);
        return null;
    }
    public List<StudentMappingTeacher> getByStudent(String studentName) throws Exception {
        List<StudentMappingTeacher> listStudent = list("WHERE studentName == :1", studentName);
        return listStudent;
    }
    public List<StudentMappingTeacher> getStudentByTeacherName(String idClass, String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        TypeMetadata metaStudentMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingClass.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","SELECT id, studentName, teacherName FROM " + metaStudentMappingTeacher.getTable() + " WHERE studentName not IN (select studentName FROM " + metaStudentMappingClass.getTable() + " WHERE idClass='"+idClass+"' and isDeleted = false) and teacherName='"+teacherName+"' and isDeleted = false and status='accept'");
        try {
            List<StudentMappingTeacher> studentMappingTeachers = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacher studentMappingTeacher = new StudentMappingTeacher();
                studentMappingTeacher.setId(data[0].toString());
                studentMappingTeacher.setStudentName(data[1].toString());
                studentMappingTeacher.setTeacherName(data[2].toString());
                studentMappingTeachers.add(studentMappingTeacher);
            }
            return studentMappingTeachers;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<StudentMappingTeacherClient> getStudentMappingTeaccher(String studentName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        TypeMetadata metaTeacherMappingCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","select student.id,student.studentName,student.teacherName,student.firstTeacherName,student.lastTeacherName,student.status,company.company from  " + metaStudentMappingTeacher.getTable()
                + " student inner join " + metaTeacherMappingCompany.getTable()
                + " company on student.teacherName=company.username where student.studentName='"+studentName+"'" + " and company.isDeleted=false and student.isDeleted=false");
        try {
            List<StudentMappingTeacherClient> studentMappingTeacherClients = new ArrayList<StudentMappingTeacherClient>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacherClient studentMappingTeacherClient = new StudentMappingTeacherClient();
                studentMappingTeacherClient.setId(data[0].toString());
                studentMappingTeacherClient.setStudentName(data[1].toString());
                studentMappingTeacherClient.setTeacherName(data[2].toString());
                studentMappingTeacherClient.setFirstNameTeacher(data[3].toString());
                studentMappingTeacherClient.setLastNameTeacher(data[4].toString());
                studentMappingTeacherClient.setStatus(data[5].toString());
                studentMappingTeacherClient.setCompany(data[6].toString());
                studentMappingTeacherClients.add(studentMappingTeacherClient);
            }
            return studentMappingTeacherClients;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<StudentMappingTeacherClient> getByPending(String studentName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        TypeMetadata metaTeacherMappingCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","select student.id,student.studentName,student.teacherName,student.firstNameTeacher,student.lastNameTeacher,student.status,company.company from  " + metaStudentMappingTeacher.getTable()
                + " student inner join " + metaTeacherMappingCompany.getTable()
                + " company on mapping.CODE=code.CODE where studentName='"+studentName+"' and status='accept' and mappingBy='teacher'");
        try {
            List<StudentMappingTeacherClient> studentMappingTeacherClients = new ArrayList<StudentMappingTeacherClient>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacherClient studentMappingTeacherClient = new StudentMappingTeacherClient();
                studentMappingTeacherClient.setId(data[0].toString());
                studentMappingTeacherClient.setStudentName(data[1].toString());
                studentMappingTeacherClient.setTeacherName(data[2].toString());
                studentMappingTeacherClient.setFirstNameTeacher(data[3].toString());
                studentMappingTeacherClient.setLastNameTeacher(data[4].toString());
                studentMappingTeacherClient.setStatus(data[5].toString());
                studentMappingTeacherClient.setCompany(data[6].toString());
                studentMappingTeacherClients.add(studentMappingTeacherClient);
            }
            return studentMappingTeacherClients;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public List<StudentMappingTeacher> getStudentHaveLicence(String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        StringBuffer query = new StringBuffer();
        String firstQuery = "select id, studentName, teacherName, status, licence from  " + metaStudentMappingTeacher.getTable() + " where teacherName='"+teacherName+"' and status='pending' and licence=true";
        query.append(firstQuery);
        query.append(" ORDER BY studentName ASC");
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        try {
            List<StudentMappingTeacher> studentMappingTeachers = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacher studentMappingTeacher = new StudentMappingTeacher();
                studentMappingTeacher.setId(data[0].toString());
                studentMappingTeacher.setStudentName(data[1].toString());
                studentMappingTeacher.setTeacherName(data[2].toString());
                studentMappingTeacher.setStatus(data[3].toString());
                studentMappingTeacher.setLicence(Boolean.parseBoolean(data[4].toString()));
                studentMappingTeachers.add(studentMappingTeacher);
            }
            return studentMappingTeachers;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<StudentMappingTeacher> getMyStudents(String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        //StringBuffer first=new StringBuffer();
        //StringBuffer second=new StringBuffer();
        StringBuffer query = new StringBuffer();
        query.append("select id, studentName, status, licence, mappingBy  from  " + metaStudentMappingTeacher.getTable() + " where teacherName='"+teacherName+"' and  isDeleted=false");
       // String secondQuery = "select id, studentName, status, licence, mappingBy from  " + metaStudentMappingTeacher.getTable() + " where teacherName='"+teacherName+"' and licence=false and isDeleted=false";
        //first.append(firstQuery);
       // second.append(secondQuery);
        //query.append("select * from ("+ first + " UNION " + second + ") as tmp ");
        query.append(" ORDER BY studentName");
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        try {
            List<StudentMappingTeacher> studentMappingTeachers = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacher studentMappingTeacher = new StudentMappingTeacher();
                if (data[0] != null) {
                    studentMappingTeacher.setId(data[0].toString());
                }else {
                    studentMappingTeacher.setId(null);
                }
                if (data[1] != null) {
                    studentMappingTeacher.setStudentName(data[1].toString());
                }else{
                    studentMappingTeacher.setStudentName(null);
                }
                if (data[2] != null) {
                    studentMappingTeacher.setStatus(data[2].toString());
                }else {
                    studentMappingTeacher.setStatus(null);
                }
                if (data[3] != null) {
                    studentMappingTeacher.setLicence(Boolean.parseBoolean(data[3].toString()));
                }else{
                    studentMappingTeacher.setLicence(false);
                }
                if (data[4] != null) {
                    studentMappingTeacher.setMappingBy(data[4].toString());
                }else{
                    studentMappingTeacher.setMappingBy(null);
                }
                studentMappingTeachers.add(studentMappingTeacher);
            }
            return studentMappingTeachers;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public List<StudentMappingTeacher> getListStudentForClass(String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        StringBuffer first=new StringBuffer();
        String firstQuery = "select id, studentName, status, licence, mappingBy  from  " + metaStudentMappingTeacher.getTable() + " where teacherName='"+teacherName+"' and status='accept' and isDeleted=false";
        first.append(firstQuery);
        first.append(" ORDER BY studentName ASC");
        Query q = pm.newQuery("javax.jdo.query.SQL", first.toString());
        try {
            List<StudentMappingTeacher> studentMappingTeachers = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacher studentMappingTeacher = new StudentMappingTeacher();
                if (data[0] != null) {
                    studentMappingTeacher.setId(data[0].toString());
                }else {
                    studentMappingTeacher.setId(null);
                }
                if (data[1] != null) {
                    studentMappingTeacher.setStudentName(data[1].toString());
                }else{
                    studentMappingTeacher.setStudentName(null);
                }
                if (data[2] != null) {
                    studentMappingTeacher.setStatus(data[2].toString());
                }else {
                    studentMappingTeacher.setStatus(null);
                }
                if (data[3] != null) {
                    studentMappingTeacher.setLicence(Boolean.parseBoolean(data[3].toString()));
                }else{
                    studentMappingTeacher.setLicence(false);
                }
                if (data[4] != null) {
                    studentMappingTeacher.setMappingBy(data[4].toString());
                }else{
                    studentMappingTeacher.setMappingBy(null);
                }
                studentMappingTeachers.add(studentMappingTeacher);
            }
            return studentMappingTeachers;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }


    public List<StudentMappingTeacher> notificationAccept(String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        StringBuffer query = new StringBuffer();
        String firstQuery = "select id, studentName from  " + metaStudentMappingTeacher.getTable() + " where teacherName='"+teacherName+"' and status='accept' and isView=false and mappingBy='teacher' and isDeleted=false";
        query.append(firstQuery);
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        try {
            List<StudentMappingTeacher> studentMappingTeachers = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacher studentMappingTeacher = new StudentMappingTeacher();
                studentMappingTeacher.setId(data[0].toString());
                studentMappingTeacher.setStudentName(data[1].toString());
                studentMappingTeachers.add(studentMappingTeacher);
            }
            return studentMappingTeachers;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<StudentMappingTeacher> notificationReject(String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        StringBuffer query = new StringBuffer();
        String firstQuery = "select id, studentName from  " + metaStudentMappingTeacher.getTable() + " where teacherName='"+teacherName+"' and status='reject' and isView=false and mappingBy='teacher' and isDeleted=false";
        query.append(firstQuery);
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        try {
            List<StudentMappingTeacher> studentMappingTeachers = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacher studentMappingTeacher = new StudentMappingTeacher();
                studentMappingTeacher.setId(data[0].toString());
                studentMappingTeacher.setStudentName(data[1].toString());
                studentMappingTeachers.add(studentMappingTeacher);
            }
            return studentMappingTeachers;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<StudentMappingTeacher> notificationInvitation(String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        StringBuffer query = new StringBuffer();
        String firstQuery = "select id, studentName from  " + metaStudentMappingTeacher.getTable() + " where teacherName='"+teacherName+"' and status='pending' and isView=false and mappingBy='student' and isDeleted=false";
        query.append(firstQuery);
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        try {
            List<StudentMappingTeacher> studentMappingTeachers = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingTeacher studentMappingTeacher = new StudentMappingTeacher();
                studentMappingTeacher.setId(data[0].toString());
                studentMappingTeacher.setStudentName(data[1].toString());
                studentMappingTeachers.add(studentMappingTeacher);
            }
            return studentMappingTeachers;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }



}
