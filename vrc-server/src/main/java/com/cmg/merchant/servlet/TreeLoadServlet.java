package com.cmg.merchant.servlet;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.vrc.util.StringUtil;
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
public class TreeLoadServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ReviewServlet.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String target = (String) StringUtil.isNull(request.getParameter("target"), "").toString();
        String idTarget = (String) StringUtil.isNull(request.getParameter("idTarget"), "").toString();
        Boolean showBtnAction =  (Boolean) StringUtil.isNull(request.getParameter("showBtnAction"), false);
        if(target.equalsIgnoreCase(Constant.TARGET_LOAD_COURSE)){
            Boolean firstLoad =  (Boolean) StringUtil.isNull(request.getParameter("firstLoad"), false);
        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_LEVEL)){

        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_OBJECTIVE)){

        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_TEST)){

        }else if(target.equalsIgnoreCase(Constant.TARGET_LOAD_LESSONS)){

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
