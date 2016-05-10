package com.cmg.merchant.servlet.tree;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.merchant.services.treeview.NodeServices;
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
 * Created by lantb on 2016-02-22.
 */
@WebServlet(name = "TreeLoadServlet")
public class TreeLoadServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(TreeLoadServlet.class
            .getName());
    private static NodeServices nService = new NodeServices();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        Gson gson = new Gson();
        String idCourse = (String) StringUtil.isNull(request.getParameter("idCourse"), "").toString();
        String target = (String) StringUtil.isNull(request.getParameter("target"), "").toString();
        String idTarget = (String) StringUtil.isNull(request.getParameter("idTarget"), "").toString();
        Boolean showBtnAction =  (Boolean) StringUtil.switchBoolean(request.getParameter("showBtnAction"), false);
        if(target.equalsIgnoreCase(Constant.TARGET_LOAD_COURSE)){
            Boolean firstLoad =  (Boolean) StringUtil.switchBoolean(request.getParameter("firstLoad"), false);
            ArrayList<TreeNode> list = nService.loadRoot(idTarget,firstLoad,showBtnAction);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_LEVEL)){
            ArrayList<TreeNode> list = nService.loadLevel(idTarget,showBtnAction,idCourse);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_OBJECTIVE)){
            ArrayList<TreeNode> list = nService.loadObjective(idTarget,showBtnAction,idCourse);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_TEST)){
            ArrayList<TreeNode> list = nService.loadTest(idTarget,showBtnAction,idCourse);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_LESSONS)){
            ArrayList<TreeNode> list = nService.loadLesson(idTarget, showBtnAction,idCourse);
            String json = gson.toJson(list);
            response.getWriter().println(json);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
