package com.cmg.merchant.servlet;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.lesson.services.test.TestService;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.services.*;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lantb on 2016-03-30.
 */
@WebServlet(name = "PreviewServlet")
public class PreviewServlet extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (String) StringUtil.isNull(request.getParameter("action"), "");
        String id = (String) StringUtil.isNull(request.getParameter("id"), "");
        Gson gson = new Gson();
        if(action.equalsIgnoreCase(Constant.TARGET_LOAD_COURSE)){
            LevelServices services = new LevelServices();
            ArrayList<Level>  list = services.getAllByCourseId(id);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }else if(action.equalsIgnoreCase(Constant.TARGET_LOAD_LEVEL)){
            Level lv = new Level();
            OServices services = new OServices();
            TestServices tServices = new TestServices();
            ArrayList<Objective> list = services.getAllByLevelId(id);
            lv.setList(list);
            Test test = tServices.getTestByLevelId(id);
            lv.setTest(test);
            String json = gson.toJson(lv);
            response.getWriter().println(json);
        }else if(action.equalsIgnoreCase(Constant.TARGET_LOAD_OBJECTIVE)){
            LessonServices services = new LessonServices();
            ArrayList<LessonCollection> list = services.getAllByObjId(id);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }else if(action.equalsIgnoreCase(Constant.TARGET_LOAD_TEST)){
            QuestionServices services = new QuestionServices();
            ArrayList<Question> list =  services.getQuestionByTest(id);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }else if(action.equalsIgnoreCase(Constant.TARGET_LOAD_LESSONS)){
            QuestionServices services = new QuestionServices();
            ArrayList<Question> list =  services.getQuestionByIdLessonPreview(id);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
