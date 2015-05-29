package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.service.MailService;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class RegisterHandler extends BaseServlet {
    private static final Logger logger = Logger.getLogger(RegisterHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_VERSION_CODE = "version_code";
    private static String PARA_LANG_PREFIX = "lang_prefix";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ResponseData<UserProfile> responseData = new ResponseData<UserProfile>();
        responseData.setStatus(false);
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setPrettyPrinting().create();
        try {

            String profile = getParameter(request, PARA_PROFILE);
            String versionCode = getParameter(request, PARA_VERSION_CODE);
            String langPrefix = getParameter(request, PARA_LANG_PREFIX);
            if (versionCode == null || versionCode.length() == 0) {
                versionCode = "000";
            }
            if (langPrefix == null || langPrefix.length() == 0)
                langPrefix = "BE";
            if (profile != null && profile.length() > 0) {
                UserProfile user = gson.fromJson(profile, UserProfile.class);
                String message = "";
                if (user != null) {
                    UserDAO userDAO = new UserDAO();
                    User cUser = userDAO.getUserByEmail(user.getUsername());
                    if (cUser != null) {
                        message = "Email is already exist";
                    } else {
                        cUser = new User();
                        cUser.setPassword(StringUtil.md5(user.getPassword()));
                        cUser.setCountry(user.getCountry());
                        cUser.setUsername(user.getUsername());
                        cUser.setLastName(user.getLastName());
                        cUser.setFirstName(user.getFirstName());
                        cUser.setEnglishProficiency(user.getEnglishProficiency());
                        cUser.setDob(user.getDob());
                        cUser.setGender(user.isGender());
                        //cUser.setRawJsonData(profile);
                        cUser.setLoginType(user.getLoginType());
                        cUser.setName(user.getName());
                        cUser.setProfileImage(user.getProfileImage());
                        Random random = new Random();
                        cUser.setActivationCode(StringUtil.md5(user.getUsername()).substring(0,2).toUpperCase()
                                + random.nextInt(99999)
                                + langPrefix.toUpperCase() + versionCode);
                        responseData.setStatus(true);
                        responseData.setData(user);
                        if (user.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
                            cUser.setActivated(false);
                            userDAO.put(cUser);
                            // Send mail with code
                            final User eUser = cUser;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MailService mailService = new MailService();
                                    try {
                                        mailService.sendActivationEmail(eUser.getUsername(), eUser.getActivationCode());
                                    } catch (MessagingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        } else {
                            cUser.setActivated(true);
                            userDAO.put(cUser);
                        }
                    }
                }
                responseData.setMessage(message);
            } else {

                responseData.setMessage("No parameter found");
            }
        } catch (Exception e) {
            logger.error("Error when login. Message:: " + e.getMessage(),e);
            responseData.setMessage("An error occurred while register");
        }
        out.print(gson.toJson(responseData));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
