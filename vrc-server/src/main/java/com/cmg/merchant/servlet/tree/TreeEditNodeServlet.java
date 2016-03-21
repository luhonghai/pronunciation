package com.cmg.merchant.servlet.tree;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.ListWordAddQuestion;
import com.cmg.merchant.services.*;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lantb on 2016-02-24.
 */
@WebServlet(name = "TreeEditNodeServlet")
public class TreeEditNodeServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(TreeEditNodeServlet.class
            .getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String action = (String) StringUtil.isNull(request.getParameter("action"), "").toString();
        if(action.equalsIgnoreCase(Constant.ACTION_EDIT_COURSE)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            CourseServices services = new CourseServices();
            String txt = services.updateCourse(idCourse,name,description);
            response.getWriter().println(txt);
        }else
        if(action.equalsIgnoreCase(Constant.ACTION_EDIT_LEVEL)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            LevelServices lvServices = new LevelServices();
            String txt = lvServices.updateLevel(idCourse, idLevel, name, description);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_EDIT_OBJ)){
            String idObj = (String) StringUtil.isNull(request.getParameter("idObj"), "").toString();
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            OServices oServices = new OServices();
            String txt = oServices.updateObj(idLevel, idObj, name, description);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_EDIT_TEST)){
            String idTest = (String) StringUtil.isNull(request.getParameter("idTest"), "").toString();
            Double percent = Double.parseDouble((String)StringUtil.isNull(request.getParameter("percent"), "0"));
            TestServices tServices = new TestServices();
            String txt = tServices.updateTest(idTest, percent);
            response.getWriter().println(txt);

        }else if(action.equalsIgnoreCase(Constant.ACTION_EDIT_LESSON)){
            String idObjective = (String) StringUtil.isNull(request.getParameter("idObj"), "").toString();
            String idLesson = (String) StringUtil.isNull(request.getParameter("idLesson"), "").toString();
            String lessonName = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String lessonDescription = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            String lessonType = (String) StringUtil.isNull(request.getParameter("type"), "").toString();
            String lessonDetail = (String) StringUtil.isNull(request.getParameter("details"), "").toString();
            LessonServices lessonServices = new LessonServices();
            String txt = lessonServices.updateLesson(idObjective,idLesson, lessonName,lessonDescription,lessonType,lessonDetail);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_EDIT_QUESTION)){
            Gson gson=new Gson();
            String listWord =(String) StringUtil.isNull(request.getParameter("listWord"), "").toString();
            String idQuestion=(String) StringUtil.isNull(request.getParameter("idQuestion"), "").toString();
            ListWordAddQuestion listWords=gson.fromJson(listWord, ListWordAddQuestion.class);
            QuestionServices questionServices=new QuestionServices();
            String txt = questionServices.updateWordToQuestion(listWords,idQuestion);
            response.getWriter().println(txt);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
