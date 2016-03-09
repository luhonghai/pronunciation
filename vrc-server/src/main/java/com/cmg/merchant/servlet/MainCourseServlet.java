package com.cmg.merchant.servlet;

import com.cmg.merchant.data.dto.CourseDTO;
import com.cmg.merchant.services.CMTSERVICES;
import com.cmg.merchant.services.CourseServices;
import com.cmg.merchant.util.SessionUtil;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-03.
 */
@WebServlet(name = "MainCourseServlet")
public class MainCourseServlet extends BaseServlet {
    private static String ACTION_ADD_COURSE = "addcourse";
    private static String ACTION_LIST_ALL = "listall";
    private static String ACTION_SEARCH_HEADER = "searchHeader";
    private static String ACTION_SEARCH_DETAIL = "searchDetail";
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
            if(result.indexOf(services.SUCCESS)!=-1){
                request.getSession().setAttribute(SessionUtil.ATT_COURSE_ID,result);
            }
            response.getWriter().print(result);
        }else if(action.equalsIgnoreCase(ACTION_LIST_ALL) && util.checkSessionValid(request)){
            CMTSERVICES services = new CMTSERVICES();
            ArrayList<CourseDTO> list = services.getCoursesForMainPage(util.getCpId(request),util.getTid(request));
            Gson gson = new Gson();
            String json = gson.toJson(list);
            response.getWriter().print(json);
        }else if(action.equalsIgnoreCase(ACTION_SEARCH_HEADER) && util.checkSessionValid(request)){
            String name = (String) StringUtil.isNull(request.getParameter("name"), 0).toString();
        }else if(action.equalsIgnoreCase(ACTION_SEARCH_DETAIL) && util.checkSessionValid(request)){

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
