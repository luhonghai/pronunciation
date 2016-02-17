package com.cmg.merchant.servlet;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.merchant.services.treeview.GenerateTreeNode;
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
 * Created by lantb on 2016-02-16.
 */
@WebServlet(name = "ReviewServlet")
public class ReviewServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(ReviewServlet.class
            .getName());
    public GenerateTreeNode treeView = new GenerateTreeNode();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String target = (String) StringUtil.isNull(request.getParameter("target"), "").toString();
        String id = (String) StringUtil.isNull(request.getParameter("idTarget"), "").toString();
        Gson gson = new Gson();
        if(target.equalsIgnoreCase(Constant.TARGET_LOAD_COURSE)){
            ArrayList<TreeNode> node = treeView.getCourseNode(id,false);
            String json = gson.toJson(node);
            response.getWriter().print(json);
        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_LEVEL)){
            ArrayList<TreeNode> node = treeView.getChildInLevel(id, false);
            String json = gson.toJson(node);
            response.getWriter().print(json);
        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_OBJECTIVE)){

        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_TEST)){

        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_LESSONS)){

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
