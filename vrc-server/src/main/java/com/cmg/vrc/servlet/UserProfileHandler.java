package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.SecurityDAO;
import com.cmg.vrc.data.dao.impl.UsageDAO;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.jdo.Security;
import com.cmg.vrc.data.jdo.Usage;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class UserProfileHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UserProfileHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_ACTION = "action";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ResponseData<User> responseData = new ResponseData<User>();
        Gson gson = new Gson();
        try {

            UserDAO userDAO = new UserDAO();
            String profile = request.getParameter(PARA_PROFILE);
            String action = request.getParameter(PARA_ACTION);
            if (StringUtils.isEmpty(action) || StringUtils.isEmpty(profile)) {
                responseData.setStatus(false);
                responseData.setMessage("no parameter found");
            } else if (action.equalsIgnoreCase("get")) {
                final UserProfile user = gson.fromJson(profile, UserProfile.class);
                User mUser = userDAO.getUserByEmail(user.getUsername());
                if (mUser != null) {
                    responseData.setStatus(true);
                    responseData.setData(mUser);
                    responseData.setMessage("success");
                } else {
                    responseData.setStatus(false);
                    responseData.setMessage("user data not found");
                }
            } else if (action.equalsIgnoreCase("update")) {
                final UserProfile user = gson.fromJson(profile, UserProfile.class);
                User mUser = userDAO.getUserByEmail(user.getUsername());
                if (mUser != null) {
                    mUser.setLoginType(user.getLoginType());
                    mUser.setCountry(user.getCountry());
                    mUser.setDob(user.getDob());
                    mUser.setFirstName(user.getFirstName());
                    mUser.setLastName(user.getLastName());
                    mUser.setEnglishProficiency(user.getEnglishProficiency());
                    mUser.setGender(user.isGender());
                    mUser.setName(user.getName());
                    mUser.setNativeEnglish(user.isNativeEnglish());
                    if (user.getProfileImage().length() > 0)
                        mUser.setProfileImage(user.getProfileImage());
                    userDAO.put(mUser);
                    responseData.setStatus(true);
                    responseData.setData(mUser);
                    responseData.setMessage("success");
                } else {
                    responseData.setStatus(false);
                    responseData.setMessage("invalid user email");
                }
            } else {
                responseData.setStatus(false);
                responseData.setMessage("invalid request");
            }
        } catch (Exception e) {
            logger.error("Error when login. Message:: " + e.getMessage(),e);
            responseData.setStatus(false);
            responseData.setMessage("could not fetch user data");
        }
        out.print(gson.toJson(responseData));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
