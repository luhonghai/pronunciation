package com.cmg.merchant.servlet.tree;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.services.CourseServices;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lantb on 2016-02-23.
 */
@WebServlet(name = "TreeAddNodeServlet")
public class TreeAddNodeServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(TreeAddNodeServlet.class
            .getName());
    private static String ADD_LEVEL = "addLevel";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String action = (String) StringUtil.isNull(request.getParameter("action"), "").toString();
        System.out.println("action : " + action);
        if(action.equalsIgnoreCase(Constant.ACTION_ADD_LEVEL)){
            String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
            String name = (String) StringUtil.isNull(request.getParameter("name"), "").toString();
            String description = (String) StringUtil.isNull(request.getParameter("description"), "").toString();
            System.out.println("idCourse : " + idCourse + "-" + name + "-" + description);
            CourseServices cServices = new CourseServices();
            String text = cServices.addLevelToCourse(idCourse,name,description);
            response.getWriter().println(text);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
