package com.cmg.merchant.servlet.tree;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.services.LessonServices;
import com.cmg.merchant.services.LevelServices;
import com.cmg.merchant.services.OServices;
import com.cmg.merchant.services.TestServices;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lantb on 2016-02-24.
 */
@WebServlet(name = "TreeDeleteNodeServlet")
public class TreeDeleteNodeServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(TreeDeleteNodeServlet.class
            .getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String action = (String) StringUtil.isNull(request.getParameter("action"), "").toString();
        if(action.equalsIgnoreCase(Constant.ACTION_DELETE_LEVEL)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            LevelServices lvService = new LevelServices();
            String txt = lvService.deleteLevel(idCourse,idLevel);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_DELETE_OBJ)){
            String idObj = (String) StringUtil.isNull(request.getParameter("idObj"), "").toString();
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            OServices oServices = new OServices();
            String txt = oServices.deleteObj(idLevel,idObj);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_DELETE_TEST)){
            String idLevel = (String) StringUtil.isNull(request.getParameter("idLevel"), "").toString();
            String idTest = (String) StringUtil.isNull(request.getParameter("idTest"), "").toString();
            TestServices tServices = new TestServices();
            String txt = tServices.deleteTest(idTest,idLevel);
            response.getWriter().println(txt);
        }else if(action.equalsIgnoreCase(Constant.ACTION_DELETE_LESSON)){
            String idObj = (String) StringUtil.isNull(request.getParameter("idObj"), "").toString();
            String idLesson = (String) StringUtil.isNull(request.getParameter("idLesson"), "").toString();
            LessonServices lessonServices = new LessonServices();
            String txt = lessonServices.deleteLesson(idObj, idLesson);
            response.getWriter().println(txt);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
