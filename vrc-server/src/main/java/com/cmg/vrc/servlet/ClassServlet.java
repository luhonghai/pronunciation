package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.AdminDAO;
import com.cmg.vrc.data.dao.impl.ClassDAO;
import com.cmg.vrc.data.dao.impl.ClassMappingTeacherDAO;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.ClassJDO;
import com.cmg.vrc.data.jdo.ClassMappingTeacher;
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
    class ListClass{
        private String message;
        private List<ClassJDO> listclass;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClassDAO classDAO=new ClassDAO();
        ClassMappingTeacherDAO classMappingTeacherDAO=new ClassMappingTeacherDAO();
        ListClass listClass=new ListClass();
        String action=request.getParameter("action");
        String teacher=request.getSession().getAttribute("username").toString();
        if (action.equalsIgnoreCase("listMyClass")) {
            try {
                listClass.message="success";
                listClass.listclass=classDAO.listAll(teacher);
                Gson gson=new Gson();
                String list=gson.toJson(listClass);
                response.getWriter().write(list);
            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("add")!=null){
            String classname = request.getParameter("classname");
            String definition = request.getParameter("definition");
            String userName = request.getSession().getAttribute("username").toString();
            int version=0;
            String uuid="";

            try{
                uuid= UUIDGenerator.generateUUID();
                version=classDAO.getLatestVersion() +1;
                ClassJDO classJDO=new ClassJDO();
                ClassMappingTeacher classMappingTeacher=new ClassMappingTeacher();
                classJDO.setId(uuid);
                classJDO.setClassName(classname);
                classJDO.setDefinition(definition);
                classJDO.setCreatedDate(new Date(System.currentTimeMillis()));
                classJDO.setIsDeleted(false);
                classJDO.setVersion(version);
                classDAO.put(classJDO);
                classMappingTeacher.setIdClass(uuid);
                classMappingTeacher.setTeacherName(userName);
                classMappingTeacher.setIsDeleted(false);
                classMappingTeacherDAO.put(classMappingTeacher);
                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }

        }
        if(request.getParameter("edit")!=null){
            String id=request.getParameter("id");
            String difinition = request.getParameter("difinition");

            try{
                ClassJDO classJDO=new ClassJDO();
                classJDO=classDAO.getById(id);
                if(classJDO!=null) {
                    classJDO.setDefinition(difinition);
                    classDAO.put(classJDO);
                    response.getWriter().write("success");
                }else{
                    response.getWriter().write("null");
                }
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }

        }

        if(request.getParameter("delete")!=null){

            String id=request.getParameter("id");
            try {
                ClassJDO classJDO=new ClassJDO();
                classJDO=classDAO.getById(id);
                if(classJDO!=null) {
                    classJDO.setIsDeleted(true);
                    classDAO.put(classJDO);
                    classMappingTeacherDAO.updateEdit(id);
                    response.getWriter().write("success");
                }else {
                    response.getWriter().write("null");
                }
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
