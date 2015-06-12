package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.service.MailService;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
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
public class ResetPasswordHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ResetPasswordHandler.class
            .getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {
            if (!StringUtils.isEmpty(action)) {
                UserDAO userDAO = new UserDAO();
                if (action.equalsIgnoreCase("reset")) {
                    String code = request.getParameter("code");
                    String password = request.getParameter("password");
                    User user = userDAO.getUserByResetPasswordCode(code);
                    if (user != null) {
                        user.setPassword(StringUtil.md5(password));
                        user.setResetPasswordCode("");
                        userDAO.put(user);
                        out.print("success");
                    } else {
                        out.print("Invalid request code");
                    }
                } else if (action.equalsIgnoreCase("request")) {
                    String acc = request.getParameter("acc");
                    User user = userDAO.getUserByEmail(acc);
                    if (user != null) {
                        String code = UUIDGenerator.generateUUID();
                        user.setResetPasswordCode(code);
                        userDAO.put(user);
                        MailService mailService = new MailService();
                        mailService.sendResetPasswordEmail(acc, code);
                        out.print("success");
                    } else {
                        out.print("Account " + acc + " does not exist");
                    }
                }
            } else {
                out.print("empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("Could not complete request");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
