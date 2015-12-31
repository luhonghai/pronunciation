package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.NumberDateDAO;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.LoginToken;
import com.cmg.vrc.data.jdo.User;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

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
        User user1;
        UserDAO userDAO=new UserDAO();
        NumberDateDAO numberDateDAO=new NumberDateDAO();
        try {
            Date date=new Date(System.currentTimeMillis());
            String profile = request.getParameter(PARA_PROFILE);
            final ResponseDataExt responseData = new ResponseDataExt();
            int number= numberDateDAO.numberDate().getNumberDate();
            if (profile != null && profile.length() > 0) {
                Gson gson = new Gson();
                final UserProfile user = gson.fromJson(profile, UserProfile.class);
                if (number > 0) {
                    if (user != null) {
                        user1 = userDAO.getUserByEmail(user.getUsername());
                        if (user1 != null) {
                            user1.setIsActivatedLicence(user.isActivatedLicence());
                            user1.setIsSubscription(user.isSubscription());
                            userDAO.update(user1);
                            Calendar cal1 = Calendar.getInstance();
                            cal1.setTime(date);
                            cal1.set(Calendar.MILLISECOND, 0);
                            cal1.set(Calendar.SECOND,0);
                            cal1.set(Calendar.MINUTE, 0);
                            cal1.set(Calendar.HOUR, 0);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTime(user1.getCreatedDate());
                            cal2.set(Calendar.MILLISECOND, 0);
                            cal2.set(Calendar.SECOND,0);
                            cal2.set(Calendar.MINUTE,0);
                            cal2.set(Calendar.HOUR, 0);
                            cal2.add(Calendar.DATE, number);
                            if (cal2.before(cal1)) {
                                responseData.setMessage("out of date");
                                responseData.setStatus(false);
                            } else {
                                responseData.setMessage("success");
                                responseData.setStatus(true);
                            }
                        } else {
                            responseData.setMessage("no account found " + user.getUsername());
                            responseData.setStatus(false);
                        }
                    } else {
                        responseData.setMessage("error");
                        responseData.setStatus(false);
                    }
                } else {
                    responseData.setMessage("success");
                    responseData.setStatus(true);
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
