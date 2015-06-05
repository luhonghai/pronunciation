package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.FeedbackDAO;
import com.cmg.vrc.data.dao.impl.LicenseCodeDAO;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CMGT400 on 6/4/2015.
 */
public class Dashboard extends HttpServlet {
    class getcouse{
        double x,y,z,t;
    }
    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FeedbackDAO feedbackDAO=new FeedbackDAO();
        UserDAO userDAO=new UserDAO();
        UserVoiceModelDAO userVoiceModelDAO=new UserVoiceModelDAO();
        LicenseCodeDAO licenseCodeDAO=new LicenseCodeDAO();
        Dashboard.getcouse getcouse=new getcouse();
        if(request.getParameter("list")!=null){
            try {
                getcouse.x=feedbackDAO.getCount();
                getcouse.y=userDAO.getCount();
                getcouse.z=userVoiceModelDAO.getCount();
                getcouse.t=licenseCodeDAO.getCount();
                Gson gson = new Gson();
                String couse = gson.toJson(getcouse);
                response.getWriter().write(couse);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
