package com.cmg.merchant.servlet;

import com.cmg.merchant.services.CMTSERVICES;
import com.cmg.merchant.services.CourseServices;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lantb on 2016-03-15.
 */
@WebServlet(name = "PublishCourseServlet")
public class PublishCourseServlet extends HttpServlet {
    public String ACTION_PUBLISH_COURSE = "publish";
    public String ACTION_PUBLISH_COURSE_COPY = "publishCourseCp";
    public String ACTION_UPDATE_STATE_COPY = "updateState";
    public String ACTION_ENABLE_BUTTON = "checkButton";
    public String ACTION_ENABLE_BUTTON_ADD_LV = "checkButtonAddLv";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        String action = (String) StringUtil.isNull(request.getParameter("action"), "").toString();
        if(action.equalsIgnoreCase(ACTION_PUBLISH_COURSE)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            boolean checkData = Boolean.parseBoolean(StringUtil.isNull(request.getParameter("checkData"), "").toString());
            CMTSERVICES services = new CMTSERVICES();
            String text = services.publishCourse(idCourse,checkData);
            response.getWriter().println(text);
        }else if(action.equalsIgnoreCase(ACTION_PUBLISH_COURSE_COPY)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            String state = (String) StringUtil.isNull(request.getParameter("state"), "").toString();
            CMTSERVICES services = new CMTSERVICES();
            String text = services.publishCourseCopy(idCourse, state);
            response.getWriter().println(text);
        }else if(action.equalsIgnoreCase(ACTION_UPDATE_STATE_COPY)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            String state = (String) StringUtil.isNull(request.getParameter("state"), "").toString();
            CMTSERVICES services = new CMTSERVICES();
            String text = services.UpdateStateCourseCopy(idCourse, state);
            response.getWriter().println(text);
        }else if(action.equalsIgnoreCase(ACTION_ENABLE_BUTTON)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            CourseServices services = new CourseServices();
            String text = services.enablePublishButton(idCourse);
            response.getWriter().println(text);
        }else if(action.equalsIgnoreCase(ACTION_ENABLE_BUTTON_ADD_LV)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            CourseServices services = new CourseServices();
            String text = services.enableAddLvButton(idCourse);
            response.getWriter().println(text);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
