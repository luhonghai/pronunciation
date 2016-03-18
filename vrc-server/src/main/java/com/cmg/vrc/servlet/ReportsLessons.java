package com.cmg.vrc.servlet;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
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
    class Info{
        private String courseName;
        private String levelName;
        private String objectiveName;
        private String lessonName;
        private String completionDate;
        private int studentScore;
        private int classAvgScore;
        Map<String,Integer> phonemesStudent = new HashMap<String,Integer>();
        Map<String,Integer> phonemesClass = new HashMap<String,Integer>();
        Map<String,Integer> wordStudent = new HashMap<String,Integer>();
        Map<String,Integer> wordClass = new HashMap<String,Integer>();

    }



    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
        ListStudent listStudent = new ListStudent();
        Info info=new Info();
        List<Info> infos=new ArrayList<Info>();
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