package com.cmg.merchant.dao.report;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.common.SQL;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMGT400 on 3/28/2016.
 */
public class ReportLessonDAO {
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
    public List<Course> getListCourse(String teacherName,String student){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        String query=sql.getSqlCourseForStudent(teacherName,student);
        Query q = pm.newQuery("javax.jdo.query.SQL",query);
        try {
            List<Course> courses = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Course course = new Course();
                if (data[0] != null) {
                    course.setId(data[0].toString());
                }else {
                    course.setId(null);
                }
                if (data[1] != null) {
                    course.setName(data[1].toString());
                }else{
                    course.setName(null);
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
    public List<Level> getListLevel(String idCourse){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        String query=sql.getSqlLevelFromCourse(idCourse);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Level> levels = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Level level = new Level();
                if (data[0] != null) {
                    level.setId(data[0].toString());
                }else {
                    level.setId(null);
                }
                if (data[1] != null) {
                    level.setName(data[1].toString());
                }else{
                    level.setName(null);
                }
                levels.add(level);
            }
            return levels;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public List<Objective> getListOBJ(String idLevel){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        String query=sql.getSqlOBJFromLevel(idLevel);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Objective> objectives = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Objective objective = new Objective();
                if (data[0] != null) {
                    objective.setId(data[0].toString());
                }else {
                    objective.setId(null);
                }
                if (data[1] != null) {
                    objective.setName(data[1].toString());
                }else{
                    objective.setName(null);
                }
                objectives.add(objective);
            }
            return objectives;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }


}
