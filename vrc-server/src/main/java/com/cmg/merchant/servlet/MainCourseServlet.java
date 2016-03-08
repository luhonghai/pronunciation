package com.cmg.merchant.servlet;

import com.cmg.merchant.services.CourseServices;
import com.cmg.merchant.util.SessionUtil;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lantb on 2016-02-03.
 */
@WebServlet(name = "MainCourseServlet")
public class MainCourseServlet extends BaseServlet {
    private static String ACTION_ADD_COURSE = "addcourse";
    private static String ACTION_LIST_ALL = "listall";
    private static final Logger logger = Logger.getLogger(MainCourseServlet.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String action = (String) StringUtil.isNull(request.getParameter("action"), 0).toString();
        SessionUtil util = new SessionUtil();
        if(action.equalsIgnoreCase(ACTION_ADD_COURSE) && util.checkSessionValid(request)){
            String name = (String) StringUtil.isNull(request.getParameter("name"), 0).toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), 0).toString();
            String share = (String) StringUtil.isNull(request.getParameter("share"), 0).toString();
            CourseServices services = new CourseServices();
            String result = services.addCourse(name,description,share,request);
            response.getWriter().print(result);
        }else if(action.equalsIgnoreCase(ACTION_LIST_ALL) && util.checkSessionValid(request)){
            CourseServices services = new CourseServices();

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
