package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.NumberDateDAO;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.LoginToken;
import com.cmg.vrc.data.jdo.User;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by CMGT400 on 11/13/2015.
 */
@WebServlet(name = "CheckNumberDate")
public class CheckNumberDate extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AuthHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private class ResponseDataExt extends ResponseData<LoginToken> {
        public LoginToken loginTokens;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        User user1=new User();
        UserDAO userDAO=new UserDAO();
        NumberDateDAO numberDateDAO=new NumberDateDAO();
        try {
            DateTime date=new DateTime(System.currentTimeMillis());
            String profile = request.getParameter(PARA_PROFILE);
            final ResponseDataExt responseData = new ResponseDataExt();
            int number=numberDateDAO.numberDate().getNumberDate();
            if (profile != null && profile.length() > 0) {
                Gson gson = new Gson();
                final UserProfile user = gson.fromJson(profile, UserProfile.class);
                if(user!=null){
                    user1=userDAO.getUserByEmail(user.getUsername());
                    int x= Days.daysBetween(date, user1.getCreatedDate()).getDays();
                    if(x>number){
                        responseData.setMessage("out of date");
                        responseData.setStatus(false);
                    }else {
                        responseData.setMessage("success");
                        responseData.setStatus(true);
                    }
                }else {
                    responseData.setMessage("error");
                    responseData.setStatus(false);
                }
                out.print(gson.toJson(responseData));
            }
        }catch (Exception e) {
            logger.error("Error Message:: " + e.getMessage(),e);
            out.print("Error Message:: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
