package com.cmg.vrc.data.dao.impl;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.*;
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
    public ClassJDO getClassName(String classname) throws Exception {
        List<ClassJDO> userList = list("WHERE className == :1 && isDeleted== :2", classname,false);
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
                + " mapping on mapping.idClass=class.id where mapping.teacherName='"+teacherName+"' and class.isDeleted=false and mapping.isDeleted=false ";
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

    public List<StudentMappingTeacher> getStudentByTeacherNameOnClass(String idClass, String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        TypeMetadata metaStudentMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingClass.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "SELECT teacher.id, teacher.studentName, teacher.teacherName FROM " + metaStudentMappingTeacher.getTable() + " teacher inner join "+metaStudentMappingClass.getTable()+" class on teacher.studentName=class.studentName WHERE teacher.teacherName='"+teacherName+"' and class.idClass='"+idClass+"' and class.isDeleted = false and teacher.isDeleted = false and teacher.status='accept'");
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

    public List<Course> getMyCourses(String idClass, String teacherID,String idCompany){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaCourse = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Course.class.getCanonicalName());
        TypeMetadata metaCourseMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingTeacher.class.getCanonicalName());
        TypeMetadata metaCourseMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingClass.class.getCanonicalName());
        //Query q = pm.newQuery("javax.jdo.query.SQL", "SELECT id, studentName, teacherName FROM " + metaStudentMappingTeacher.getTable() + " WHERE studentName not IN (select studentName FROM " + metaStudentMappingClass.getTable() + " WHERE idClass='" + idClass + "' and isDeleted = false) and teacherName='" + teacherName + "' and isDeleted = false and status='accept'");
        StringBuffer query = new StringBuffer();
        String firstQuery = "select course.id, course.name,course.description from  " + metaCourse.getTable() + " course inner join " + metaCourseMappingTeacher.getTable()+ " mapping on course.id=mapping.cID inner join CLIENTCODE as company on mapping.cpID = company.id  where course.id not IN (select idCourse FROM " + metaCourseMappingClass.getTable() + " WHERE idClass='" + idClass + "' and isDeleted = false) and mapping.tID='"+teacherID+"' and company.id='"+idCompany+"' and course.isDeleted = false and mapping.isDeleted = false and company.isDeleted=false";
        query.append(firstQuery);
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        try {
            List<Course> courses = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Course course = new Course();
                if (data[0] != null) {
                    course.setId(data[0].toString());
                }else{
                    course.setId(null);
                }
                if (data[1] != null) {
                    course.setName(data[1].toString());
                }else{
                    course.setName(null);
                }
                if (data[2] != null) {
                    course.setDescription(data[2].toString());
                }else{
                    course.setDescription(null);
                }
                courses.add(course);
            }
            return courses;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public List<Course> getMyCoursesOnClass(String idClass, String teacherID,String status){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaCourse = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Course.class.getCanonicalName());
        TypeMetadata metaCourseMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingTeacher.class.getCanonicalName());
        TypeMetadata metaCourseMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingClass.class.getCanonicalName());
        StringBuffer query = new StringBuffer();
        String firstQuery = "select DISTINCT course.id, course.name,course.description from  " + metaCourse.getTable() + " course inner join " + metaCourseMappingTeacher.getTable()+ " mapping on course.id=mapping.cID inner join "+metaCourseMappingClass.getTable()+" class on course.id=class.idCourse where class.idClass='"+idClass+"'  and mapping.tID='"+teacherID+"' and mapping.status='"+status+"' and course.isDeleted=false and mapping.isDeleted=false and class.isDeleted=false";
        query.append(firstQuery);
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        try {
            List<Course> courses = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Course course = new Course();
                if (data[0] != null) {
                    course.setId(data[0].toString());
                }else{
                    course.setId(null);
                }
                if (data[1] != null) {
                    course.setName(data[1].toString());
                }else{
                    course.setName(null);
                }
                if (data[2] != null) {
                    course.setDescription(data[2].toString());
                }else{
                    course.setDescription(null);
                }
                courses.add(course);
            }
            return courses;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public void updateCourseMappingClassDelete(String idClass) throws Exception{

        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaCourseMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingClass.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaCourseMappingClass.getTable() + " SET isDeleted= ? WHERE idClass=?");
        try {
            q.execute(true,idClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    public void updateStudentMappingClassDelete(String idClass) throws Exception{

        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingClass.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaStudentMappingClass.getTable() + " SET isDeleted= ? WHERE idClass=?");
        try {
            q.execute(true,idClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    public void updateCourseMappingClassEdit(String idClass) throws Exception{

        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaCourseMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingClass.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "DELETE FROM " + metaCourseMappingClass.getTable() + " WHERE idClass=?");
        try {
            q.execute(idClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    public void updateStudentMappingClassEdit(String idClass) throws Exception{

        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingClass.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "DELETE FROM " + metaStudentMappingClass.getTable() + " WHERE idClass=?");
        try {
            q.execute(idClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    public void updateClassDelete(String idClass) throws Exception{

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE CLASSJDO as class inner join CLASSMAPPINGTEACHER as cmt on class.id = cmt.idClass inner join COURSEMAPPINGCLASS as cmc on class.id = cmc.idClass inner join STUDENTMAPPINGCLASS as smc on class.id = smc.idClass set class.isDeleted=true,cmt.isDeleted=true,cmc.isDeleted=true,smc.isDeleted=true where class.id = '"+idClass+"'");
        try {
            q.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

}
