package com.cmg.merchant.servlet.Report;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.services.Report.ReportLessonService;
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
    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReportLessonService service=new ReportLessonService();
        Gson gson=new Gson();
        String action=request.getParameter("action");
        String teacherName=request.getSession().getAttribute("username").toString();
        String teacherID=request.getSession().getAttribute("id").toString();
        if (action.equalsIgnoreCase("listStudent")) {
            String result=service.listStudent(teacherName);
            response.getWriter().write(result);
        }else if (action.equalsIgnoreCase("listCourse")) {
            String studentName=request.getParameter("studentName");
            String result=service.listCourse(teacherName,studentName);
            response.getWriter().write(result);
        }else if(action.equalsIgnoreCase("listLevel")){
            String idCourse=request.getParameter("idCourse");
            String result=service.listLevel(idCourse);
            response.getWriter().write(result);
        }else if(action.equalsIgnoreCase("listObjective")){
            String idLevel=request.getParameter("idLevel");
            String result=service.listOBJ(idLevel);
            response.getWriter().write(result);
        }else if(action.equalsIgnoreCase("loadInfo")){
            String idOBJ=request.getParameter("idObj");
            String result=service.listLesson(idOBJ);
            response.getWriter().write(result);
        }else if(action.equalsIgnoreCase("drawChart")){
            String idLesson=request.getParameter("idLesson");
            String student=request.getParameter("student");
            String result=service.draw(idLesson,student);
            response.getWriter().write(result);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
