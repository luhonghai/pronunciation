package com.cmg.merchant.services.Report;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.dao.report.ReportLessonDAO;
import com.cmg.vrc.data.dao.impl.StudentMappingTeacherDAO;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMGT400 on 3/28/2016.
 */
public class ReportLessonService {
    ReportLessonDAO reportLessonDAO=new ReportLessonDAO();
    CourseDAO courseDAO=new CourseDAO();
    LevelDAO levelDAO=new LevelDAO();
    ObjectiveDAO objectiveDAO=new ObjectiveDAO();
    Gson gson=new Gson();
    class ListStudent{
        private String message;
        private List<StudentMappingTeacher> listStudent;
    }
    class ListCourse{
        private String message;
        private List<Course> listCourse;
        private List<StudentMappingTeacher> listStudent;
    }
    class ListLevel{
        private String message;
        private List<Level> listLevel;
    }
    class ListObjective{
        private String message;
        private List<Objective> listObj;
    }
    public String listStudent(String teacherName){
        ListStudent listStudent=new ListStudent();
        String list=null;
        try {
            listStudent.message="success";
            listStudent.listStudent=reportLessonDAO.getListStudentForClass(teacherName);
            list = gson.toJson(listStudent);
        } catch (Exception e) {
            listStudent.message="error";
            listStudent.listStudent=new ArrayList<>();
            list = gson.toJson(listStudent);
        }
        return list;
    }
    public String listCourse(String teacherName,String student){
        ListCourse listCourse=new ListCourse();
        String list=null;
        try {
            listCourse.message="success";
            listCourse.listStudent=reportLessonDAO.getListStudentForClass(teacherName);
            listCourse.listCourse=reportLessonDAO.getListCourse(teacherName, student);
            list = gson.toJson(listCourse);
        } catch (Exception e) {
            listCourse.message="error";
            listCourse.listCourse=new ArrayList<>();
            listCourse.listStudent=new ArrayList<>();
            list = gson.toJson(listCourse);
        }
        return list;
    }
    public String listLevel(String idCourse){
        ListLevel listLevel=new ListLevel();
        String list=null;
        try {
            listLevel.message="success";
            listLevel.listLevel=reportLessonDAO.getListLevel(idCourse);
            list = gson.toJson(listLevel);
        } catch (Exception e) {
            listLevel.message="error";
            listLevel.listLevel=new ArrayList<>();
            list = gson.toJson(listLevel);
        }
        return list;
    }
    public String listOBJ(String idLevel){
        ListObjective listObjective=new ListObjective();
        String list=null;
        try {
            listObjective.message="success";
            listObjective.listObj=reportLessonDAO.getListOBJ(idLevel);
            list = gson.toJson(listObjective);
        } catch (Exception e) {
            listObjective.message="error";
            listObjective.listObj=new ArrayList<>();
            list = gson.toJson(listObjective);
        }
        return list;
    }
}
