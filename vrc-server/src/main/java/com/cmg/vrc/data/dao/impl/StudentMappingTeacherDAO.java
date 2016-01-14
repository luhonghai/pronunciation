package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.ClassMappingTeacher;
import com.cmg.vrc.data.jdo.StudentMappingClass;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
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

    public List<StudentMappingTeacher> getStudentByTeacherName(String teacherName) throws Exception {
        List<StudentMappingTeacher> listStudent = list("WHERE teacherName == :1", teacherName);
        return listStudent;
    }
    public List<StudentMappingTeacher> getStudentByTeacherName(String idClass, String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStudentMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingTeacher.class.getCanonicalName());
        TypeMetadata metaStudentMappingClass = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingClass.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","SELECT id, studentName, teacherName FROM " + metaStudentMappingTeacher.getTable() + " WHERE studentName not IN (select studentName FROM " + metaStudentMappingClass.getTable() + " WHERE idClass='"+idClass+"' and isDeleted = false) and teacherName='"+teacherName+"'");
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
}
