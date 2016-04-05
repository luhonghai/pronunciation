package com.cmg.merchant.services.Report;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.dao.report.ReportLessonDAO;
import com.cmg.vrc.data.dao.impl.StudentMappingTeacherDAO;
import com.cmg.vrc.data.jdo.ClassJDO;
import com.cmg.vrc.data.jdo.Reports;
import com.cmg.vrc.data.jdo.StudentMappingClass;
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
    class ListClass{
        private String message;
        private List<ClassJDO> list;
    }
    class ListStudent{
        private String message;
        private List<StudentMappingClass> listSMC;
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
    class ListLesson{
        private String message;
        private List<LessonCollection> listLesson;
    }
    class Report{
        private String message;
        private Reports reports;
    }
    /**
     *
     * @param teacherName
     * @return
     */
    public String listClass(String teacherName){
        ListClass obj = new ListClass();
        try {
            List<ClassJDO> temp = reportLessonDAO.getClassByTeacher(teacherName);
            obj.list = temp;
            obj.message = "success";
        }catch (Exception e){
            obj.message = "error";
            obj.list = null;
        }
        return gson.toJson(obj);
    }

    /**
     *
     * @param idClass
     * @param teacherName
     * @return
     */
    public String listStudent(String idClass,String teacherName){
        ListStudent listStudent=new ListStudent();
        String list=null;
        try {
            listStudent.message="success";
            listStudent.listSMC=reportLessonDAO.getListStudentForClass(idClass,teacherName);
            list = gson.toJson(listStudent);
        } catch (Exception e) {
            listStudent.message="error";
            listStudent.listSMC=new ArrayList<>();
            list = gson.toJson(listStudent);
        }
        return list;
    }



    public String listCourse(String teacherName,String student){
        ListCourse listCourse=new ListCourse();
        String list=null;
        try {
            listCourse.message="success";
            //listCourse.listStudent=reportLessonDAO.getListStudentForClass(teacherName);
            //listCourse.listCourse=reportLessonDAO.getListCourse(teacherName, student);
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
    public String listLesson(String idObj){
        ListLesson listLesson=new ListLesson();
        String list=null;
        try {
            listLesson.message="success";
            listLesson.listLesson=reportLessonDAO.getListLesson(idObj);
            list = gson.toJson(listLesson);
        } catch (Exception e) {
            listLesson.message="error";
            listLesson.listLesson=new ArrayList<>();
            list = gson.toJson(listLesson);
        }
        return list;
    }
    public String draw(String idLesson,String student){
        Report report=new Report();
        String list=null;
        try {
            report.message="success";
            report.reports=getReport(idLesson,student);
        } catch (Exception e) {

        }
        return list;
    }
    public Reports getReport(String idLesson,String student){
        Reports reports=new Reports();
        try {
            reports.setStudentScoreLesson(reportLessonDAO.getStudentScoreLesson(idLesson,student));
            reports.setClassAvgScoreLesson(reportLessonDAO.getClassAvgScoreLesson(idLesson,student));
            reports.setWord(reportLessonDAO.getListWordLesson(idLesson, student));
            reports.setPhonemes(reportLessonDAO.getListPhonemeLesson(idLesson,student));
            reports.setWordStudentScore(reportLessonDAO.getWordStudentScore(idLesson,student));
            reports.setWordClassScore(reportLessonDAO.getWordClassScore(idLesson,student));
            reports.setPhonemesClassScore(reportLessonDAO.getPhonemesClassScore(idLesson,student));
            reports.setPhonemesStudentScore(reportLessonDAO.getPhonemesStudentScore(idLesson,student));
        }catch (Exception e){
            e.getStackTrace();
        }

        return reports;
    }
}
