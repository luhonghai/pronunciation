package com.cmg.merchant.servlet;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.services.DragDropServices;
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
 * Created by lantb on 2016-03-28.
 */
@WebServlet(name = "DragDropServlet")
public class DragDropServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(DragDropServlet.class.getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (String) StringUtil.isNull(request.getParameter("action"),"");
        DragDropServices services = new DragDropServices();
        if(action.equalsIgnoreCase(Constant.TARGET_LOAD_LEVEL)){
            String idCourse =  (String) StringUtil.isNull(request.getParameter("parentId"),"");
            String idLevel = (String) StringUtil.isNull(request.getParameter("childId"),"");
            int index = Integer.parseInt((String)StringUtil.isNull(request.getParameter("index"),"1"));
            String move = (String) StringUtil.isNull(request.getParameter("move"),"");
            services.dragDropLevel(idCourse,idLevel,index,move);
            response.getWriter().println("success");
        }else if(action.equalsIgnoreCase(Constant.TARGET_LOAD_OBJECTIVE)){
            String idLevel =  (String) StringUtil.isNull(request.getParameter("parentId"),"");
            String idObj = (String) StringUtil.isNull(request.getParameter("childId"),"");
            int index = Integer.parseInt((String)StringUtil.isNull(request.getParameter("index"),"1"));
            String move = (String) StringUtil.isNull(request.getParameter("move"),"");
            services.dragDropObj(idLevel, idObj, index,move);
            response.getWriter().println("success");
        }else if(action.equalsIgnoreCase(Constant.TARGET_LOAD_LESSONS)){
            String idObj =  (String) StringUtil.isNull(request.getParameter("parentId"),"");
            String idLesson = (String) StringUtil.isNull(request.getParameter("childId"),"");
            int index = Integer.parseInt((String)StringUtil.isNull(request.getParameter("index"),"1"));
            String move = (String) StringUtil.isNull(request.getParameter("move"),"");
            services.dragDropLesson(idObj, idLesson, index,move);
            response.getWriter().println("success");
        }else if(action.equalsIgnoreCase(Constant.TARGET_LOAD_QUESTION)){
            String idLesson =  (String) StringUtil.isNull(request.getParameter("parentId"),"");
            String idQuestion = (String) StringUtil.isNull(request.getParameter("childId"),"");
            int index = Integer.parseInt((String)StringUtil.isNull(request.getParameter("index"),"1"));
            String move = (String) StringUtil.isNull(request.getParameter("move"),"");
            services.dragDropQuestion(idLesson, idQuestion, index,move);
            response.getWriter().println("success");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
