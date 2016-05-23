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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-03.
 */
@WebServlet(name = "MyCourseServlet")
public class    MyCourseServlet extends BaseServlet {
    private static String ACTION_ADD_COURSE = "addcourse";
    private static String ACTION_LIST_ALL = "listall";
    private static String ACTION_SEARCH_HEADER = "searchHeader";
    private static String ACTION_SEARCH_DETAIL = "searchDetail";
    private static final Logger logger = Logger.getLogger(MyCourseServlet.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        String action = (String) StringUtil.isNull(request.getParameter("action"), 0).toString();
        SessionUtil util = new SessionUtil();
        Gson gson = new Gson();
        if(action.equalsIgnoreCase(ACTION_ADD_COURSE) && util.checkSessionValid(request)){
            String name = (String) StringUtil.isNull(request.getParameter("name"), 0).toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), 0).toString();
            String share = (String) StringUtil.isNull(request.getParameter("share"), 0).toString();
            CourseServices services = new CourseServices();
            String result = services.addCourse(name,description,share,request);
            if(result.indexOf(services.ERROR)!=-1){
                response.getWriter().print(result);
            }else{
                request.getSession().setAttribute(SessionUtil.ATT_COURSE_ID,result);
                response.getWriter().print(result);
            }
        }else if (action.equalsIgnoreCase(ACTION_LIST_ALL) && util.checkSessionValid(request)){
            CMTSERVICES services = new CMTSERVICES();
            ArrayList<CourseDTO> list = services.getCoursesForMyCourses(util.getCpId(request),
                    util.getTid(request), util.getTeacherName(request));
            String json = gson.toJson(list);
            response.getWriter().print(json);
        }else if(action.equalsIgnoreCase(ACTION_SEARCH_HEADER) && util.checkSessionValid(request)){
            CMTSERVICES services = new CMTSERVICES();
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            ArrayList<CourseDTO> list = services.searchHeaderMyCourse(util.getCpId(request), util.getTid(request), name.toLowerCase(),util.getTeacherName(request));
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }else if(action.equalsIgnoreCase(ACTION_SEARCH_DETAIL) && util.checkSessionValid(request)){
            CMTSERVICES services = new CMTSERVICES();
            String cpName = (String) StringUtil.isNull(request.getParameter("cpName"), "").toString();
            String cName = (String) StringUtil.isNull(request.getParameter("cName"), "").toString();
            String dateFrom = (String) StringUtil.isNull(request.getParameter("dateFrom"), "1999-01-01").toString();
            String dateTo = (String) StringUtil.isNull(request.getParameter("dateTo"), "2100-01-01").toString();
            ArrayList<CourseDTO> list = services.searchCourseDetailMyCourse(cpName.toLowerCase(),cName.toLowerCase(),dateFrom,dateTo,util.getCpId(request),util.getTid(request),util.getTeacherName(request));
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
