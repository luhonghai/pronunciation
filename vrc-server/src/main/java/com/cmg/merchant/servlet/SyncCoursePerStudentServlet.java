package com.cmg.merchant.servlet;

import com.cmg.merchant.data.jdo.TeacherCourseHistory;
import com.cmg.merchant.services.Sync.CourseSyncService;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.servlet.ResponseData;
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
        String profile = (String) StringUtil.isNull(request.getParameter("profile"),"");
        Gson gson = new Gson();
        ResponseData<ArrayList<TeacherCourseHistory>> responseData = new ResponseData<>();
        try {
            CourseSyncService service = new CourseSyncService();
            if (action.equalsIgnoreCase(ACTION_LIST_ALL_COURSE) && profile.length() > 0) {
                UserProfile user = gson.fromJson(profile, UserProfile.class);

                String username = user.getUsername();
                logger.info("load course of user " + username);
                ArrayList<TeacherCourseHistory> list = service.listCourseByUser(username);
                logger.info("course size " + list.size());
                responseData.setData(list);
                responseData.setStatus(true);
                responseData.setMessage("success");
            } else if (action.equalsIgnoreCase("demo")) {
                TeacherCourseHistory tmp = service.courseCMG(true);
                if (tmp != null) {
                    responseData.setStatus(true);
                    responseData.setMessage("Generate new CMG course version " + tmp.getVersion());
                } else {
                    responseData.setStatus(false);
                    responseData.setMessage("No CMG course found");
                }

            } else {
                responseData.setStatus(false);
                responseData.setMessage("invalid parameter");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus(false);
            responseData.setMessage(e.getMessage());
        }
        response.getWriter().println(gson.toJson(responseData));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
