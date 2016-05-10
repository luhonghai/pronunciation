package com.cmg.merchant.servlet.tree;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.ListWordAddQuestion;
import com.cmg.merchant.data.dto.WeightPhonemesDTO;
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
import java.util.List;

/**
 * Created by lantb on 2016-02-23.
 */
@WebServlet(name = "TreeAddNodeServlet")
public class TreeAddNodeServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(TreeAddNodeServlet.class
            .getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        String action = (String) StringUtil.isNull(request.getParameter("action"), "").toString();
        String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
        CMTSERVICES cmtservices = new CMTSERVICES();
        cmtservices.unPublishCourse(idCourse);
        if(action.equalsIgnoreCase(Constant.ACTION_ADD_LEVEL)){
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            CourseServices cServices = new CourseServices();
            String text = cServices.addLevelToCourse(idCourse,name,description);
            response.getWriter().println(text);
        }else if(action.equalsIgnoreCase(Constant.ACTION_ADD_OBJ)){
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            LevelServices lvServices = new LevelServices();
            String txt = lvServices.addObjToLv(idLevel,name,description,idCourse);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_ADD_TEST)){
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            Double percent = Double.parseDouble((String)StringUtil.isNull(request.getParameter("percent"), "0"));
            TestServices tServices = new TestServices();
            String txt = tServices.addTest(idLevel,percent);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_ADD_LESSON)){
            String idObjective = (String) StringUtil.isNull(request.getParameter("idObj"), "").toString();
            String lessonName = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String lessonDescription = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            String lessonType = (String) StringUtil.isNull(request.getParameter("type"), "").toString();
            String lessonDetail = (String) StringUtil.isNull(request.getParameter("details"), "").toString();
            LessonServices lessonServices = new LessonServices();
            String txt = lessonServices.addLessonToObj(idObjective, lessonName,lessonDescription,lessonType,lessonDetail);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_ADD_QUESTION)){
            Gson gson=new Gson();
            String listWord = (String) StringUtil.isNull(request.getParameter("listWord"),"");
            String idLesson =(String) StringUtil.isNull( request.getParameter("idLesson"),"");
            ListWordAddQuestion listWords=gson.fromJson(listWord, ListWordAddQuestion.class);
            QuestionServices questionServices=new QuestionServices();
            String txt = questionServices.addQuestionToLesson(listWords,idLesson);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_ADD_QUESTION_TEST)){
            Gson gson=new Gson();
            String listWord = (String) StringUtil.isNull(request.getParameter("listWord"),"");
            String idLesson = (String) StringUtil.isNull(request.getParameter("idLesson"),"");
            String typeOfQuestion = (String) StringUtil.isNull(request.getParameter("type"),"");
            String description = (String) StringUtil.isNull(request.getParameter("description"),"");
            ListWordAddQuestion listWords=gson.fromJson(listWord, ListWordAddQuestion.class);
            QuestionServices questionServices = new QuestionServices();
            String txt = questionServices.addQuestionToTest(listWords, idLesson, typeOfQuestion, description);
            response.getWriter().println(txt);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
