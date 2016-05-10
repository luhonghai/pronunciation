package com.cmg.merchant.servlet;

import com.cmg.merchant.services.CopyService;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lantb on 2016-03-21.
 */
@WebServlet(name = "CopyServlet")
public class CopyServlet extends BaseServlet {
    CopyService service = new CopyService();
    private static String ACTION_COPY_COURSE = "cpCourse";
    private static String ACTION_COPY_LEVEL = "cpLevel";
    private static String ACTION_COPY_OBJECTIVE = "cpObj";
    private static String ACTION_COPY_TEST = "cpTest";
    private static String ACTION_COPY_LESSON = "cpLesson";
    private static String ACTION_COPY_QUESTION = "cpQuestion";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String action = (String) StringUtil.isNull(request.getParameter("action"), "").toString();
        if(action.equalsIgnoreCase(ACTION_COPY_COURSE)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            String text = service.copyAllDataCourse(idCourse,request);
            response.getWriter().print(text);
        }else if(action.equalsIgnoreCase(ACTION_COPY_LEVEL)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            String text = service.copyLevelToCourse(idCourse,idLevel);
            response.getWriter().print(text);
        }else if(action.equalsIgnoreCase(ACTION_COPY_OBJECTIVE)){
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            String idObj = (String) StringUtil.isNull(request.getParameter("idObj"), "").toString();
            String text = service.copyObjToLevel(idLevel, idObj);
            response.getWriter().print(text);
        }else if(action.equalsIgnoreCase(ACTION_COPY_TEST)){
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            String idTest = (String) StringUtil.isNull(request.getParameter("idTest"), "").toString();
            String text = service.copyTestToLevel(idLevel, idTest,true);
            response.getWriter().print(text);
        }else if(action.equalsIgnoreCase(ACTION_COPY_LESSON)){
            String idObj = (String) StringUtil.isNull(request.getParameter("idObj"), "").toString();
            String idLesson = (String) StringUtil.isNull(request.getParameter("idLesson"), "").toString();
            String text = service.CopyLessonToObj(idObj, idLesson);
            response.getWriter().print(text);
        }else if(action.equalsIgnoreCase(ACTION_COPY_QUESTION)){
            String idLesson = (String) StringUtil.isNull(request.getParameter("idLesson"), "").toString();
            String idQuestion = (String) StringUtil.isNull(request.getParameter("idQuestion"), "").toString();
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String text = service.copyQuestionToLessons(idLesson, idQuestion,true,name);
            response.getWriter().print(text);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
