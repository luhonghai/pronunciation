package com.cmg.vrc.servlet;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
import com.cmg.merchant.util.SessionUtil;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.service.ClassService;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class ClassServlet extends HttpServlet {


    private static final Logger logger = Logger.getLogger(ClassServlet.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action=request.getParameter("action");
        String teacherName=request.getSession().getAttribute("username").toString();
        String teacherID=request.getSession().getAttribute("id").toString();
        ClassService classService=new ClassService();
        SessionUtil util = new SessionUtil();
        if (action.equalsIgnoreCase("listMyClass")) {
            String list=classService.listClass(teacherName);
            response.getWriter().write(list);
        }else if(action.equalsIgnoreCase("openAdd")){
            String list =classService.openAddClass(teacherName,util.getTid(request),util.getCpId(request));
            response.getWriter().write(list);
        }else if(action.equalsIgnoreCase("addClass")){
            String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
            String className=request.getParameter("classname");
            String definition=request.getParameter("definition");
            String list=classService.addClassToDb(teacherName, className, definition, jsonClient);
            response.getWriter().write(list);
        }else if(action.equalsIgnoreCase("openEdit")){
            String idClass=request.getParameter("id");
            String list = classService.openEditClass(teacherName,idClass,util.getTid(request),util.getCpId(request));
            response.getWriter().write(list);
        }else if(action.equalsIgnoreCase("editClass")){
            String idClass=request.getParameter("id");
            String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
            String definition= (String) StringUtil.isNull(request.getParameter("difinition"),"");
            String name = (String) StringUtil.isNull(request.getParameter("classname"), "");
            String list = classService.editClassToDb(request,teacherName,idClass, name, definition, jsonClient);
            response.getWriter().write(list);
        }else if(action.equalsIgnoreCase("deleteClass")){
            String idClass=request.getParameter("id");
            String list = classService.deleteClass(idClass);
            response.getWriter().write(list);

        }else{
            response.getWriter().write("error");
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
