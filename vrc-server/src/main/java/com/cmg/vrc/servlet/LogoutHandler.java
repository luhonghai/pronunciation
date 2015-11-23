package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.LoginTokenDAO;
import com.cmg.vrc.data.jdo.LoginToken;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by CMGT400 on 11/10/2015.
 */
@WebServlet(name = "LogoutHandler")
public class LogoutHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AuthHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_TOKEN = "token";
    private class ResponseDataExt extends ResponseData<LoginToken> {
        public LoginToken loginTokens;

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            LoginTokenDAO loginTokenDAO = new LoginTokenDAO();
            String profile = request.getParameter(PARA_PROFILE);
            String token = request.getParameter(PARA_TOKEN);
            final ResponseDataExt responseData = new ResponseDataExt();
            if (profile != null && profile.length() > 0) {
                Gson gson = new Gson();
                final UserProfile user = gson.fromJson(profile, UserProfile.class);
                if (user != null) {
                   if(token!=null){
                       String username=user.getUsername();
                       LoginToken loginToken=loginTokenDAO.getByAccountAndToken(username, token);
                       if (loginToken != null) {
                           loginTokenDAO.delete(loginToken);
                       }
                       responseData.setMessage("success");
                       responseData.setStatus(true);
                   }else {
                       responseData.setMessage("error");
                       responseData.setStatus(false);
                       logger.error("Token null.");
                   }
                }else {
                    responseData.setMessage("error");
                    responseData.setStatus(false);
                    logger.error("user null.");
                }
                out.print(gson.toJson(responseData));
            } else {
                out.print("No parameter found");
        }
        } catch (Exception e) {
            logger.error("Error when logout. Message:: " + e.getMessage(),e);
            out.print("Error when logout. Message:: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
