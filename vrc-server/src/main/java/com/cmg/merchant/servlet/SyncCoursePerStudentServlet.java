package com.cmg.merchant.servlet;

import com.cmg.merchant.data.jdo.TeacherCourseHistory;
import com.cmg.merchant.services.Sync.CourseSyncService;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lantb on 2016-04-21.
 */
@WebServlet(name = "SyncCoursePerStudentServlet")
public class SyncCoursePerStudentServlet extends BaseServlet {
    private static String ACTION_LIST_ALL_COURSE = "listAllCourse";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (String) StringUtil.isNull(request.getParameter("action"),"");
        if(action.equalsIgnoreCase(ACTION_LIST_ALL_COURSE)){
            CourseSyncService service = new CourseSyncService();
            Gson gson = new Gson();
            String username = (String) StringUtil.isNull(request.getParameter("username"),"");
            ArrayList<TeacherCourseHistory> list = service.listCourseByUser(username);
            String result = gson.toJson(list);
            response.getWriter().println(result);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
