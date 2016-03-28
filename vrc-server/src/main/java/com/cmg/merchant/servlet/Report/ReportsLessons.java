package com.cmg.merchant.servlet.Report;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.servlet.FeedbackHandler;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class ReportsLessons extends HttpServlet {
    class ListStudent{
        private String message;
        private List<StudentMappingTeacher> listStudent;
    }
    class ListCourse{
        private String message;
        private List<Course> listCourse;
    }
    class ListLevel{
        private String message;
        private List<Level> listLevel;
    }
    class ListObjective{
        private String message;
        private List<Objective> listObj;
    }



    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
        CourseDAO courseDAO=new CourseDAO();
        LevelDAO levelDAO=new LevelDAO();
        ObjectiveDAO objectiveDAO=new ObjectiveDAO();
        ListStudent listStudent = new ListStudent();
        ListCourse listCourse=new ListCourse();
        ListLevel listLevel=new ListLevel();
        ListObjective listObjective=new ListObjective();
        Gson gson=new Gson();
        String action=request.getParameter("action");
        String teacherName=request.getSession().getAttribute("username").toString();
        String teacherID=request.getSession().getAttribute("id").toString();
        if (action.equalsIgnoreCase("listStudent")) {
            try {
                listStudent.message="success";
                listStudent.listStudent=studentMappingTeacherDAO.getListStudentForClass(teacherName);
                String list=gson.toJson(listStudent);
                response.getWriter().write(list);
            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }else if (action.equalsIgnoreCase("listCourse")) {
            String studentName=request.getParameter("studentName");
            try {

                listCourse.message="success";
                listCourse.listCourse=courseDAO.listAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("listLevel")){
            try{

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("listObjective")){

            try{

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("loadInfo")){
            String studentName=request.getParameter("studentName");

            try{

            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
