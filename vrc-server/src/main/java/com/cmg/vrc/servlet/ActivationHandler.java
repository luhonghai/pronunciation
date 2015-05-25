package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.service.MailService;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class ActivationHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ActivationHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String VERSION_CODE = "version_code";
    private static String PARA_ACC = "acc";
    private static String PARA_LANG_PREFIX = "lang_prefix";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            UserDAO userDAO = new UserDAO();
            String acc = request.getParameter(PARA_ACC);
            if (acc != null && acc.length() > 0) {
                User u = userDAO.getUserByValidationCode(acc);
                if (u != null) {
                    u.setActivated(true);
                    userDAO.put(u);
                    out.print("success");
                } else {
                    out.print("Invalid activation code");
                }
            } else {
                String profile = request.getParameter(PARA_PROFILE);
                String versionCode = request.getParameter(VERSION_CODE);
                if (versionCode == null || versionCode.length() == 0) {
                    versionCode = "000";
                }
                String langPrefix = request.getParameter(PARA_LANG_PREFIX);
                if (langPrefix == null || langPrefix.length() == 0)
                    langPrefix = "BE";
                if (profile != null && profile.length() > 0) {
                    Gson gson = new Gson();
                    UserProfile user = gson.fromJson(profile, UserProfile.class);
                    String message = "success";
                    if (user != null) {
                        if (user.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
                            User u = userDAO.getUserByEmail(user.getUsername());
                            if (u != null) {
                                Random random = new Random();
                                u.setActivationCode(StringUtil.md5(user.getUsername()).substring(0,2).toUpperCase()
                                        + random.nextInt(99999)
                                        + langPrefix.toUpperCase() + versionCode);
                                MailService mailService = new MailService();
                                mailService.sendActivationEmail(user.getUsername(), u.getActivationCode());
                                userDAO.put(u);
                            } else {
                                message = "No email found";
                            }
                        } else {

                        }
                    }
                    out.print(message);
                } else {
                    out.print("No parameter found");
                }
            }
        } catch (Exception e) {
            logger.error("Error when login. Message:: " + e.getMessage(),e);
            out.print("Error when login. Message:: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
