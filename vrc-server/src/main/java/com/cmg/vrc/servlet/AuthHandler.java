package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.job.SummaryReportJob;
import com.cmg.vrc.processor.AudioCleaner;
import com.cmg.vrc.processor.SoXCleaner;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class AuthHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AuthHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String profile = request.getParameter(PARA_PROFILE);
            if (profile != null && profile.length() > 0) {
                Gson gson = new Gson();
                UserProfile user = gson.fromJson(profile, UserProfile.class);
                String message = "success";
                if (user != null) {

                    UserDAO userDAO = new UserDAO();
                    if (user.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
                        User u = userDAO.getUserByEmailPassword(user.getUsername(), StringUtil.md5(user.getPassword()));
                        if (u != null) {
                            if (!u.isActivated()) {
                                message = "account " + u.getUsername() + " is not activated. please contact support@accenteasy.com";
                            }
                        } else {
                            message = "invalid email address or password";
                        }
                    } else {
                        User u = userDAO.getUserByEmail(user.getUsername());
                        if (u != null && !u.isActivated()) {
                            message = "account " + u.getUsername() + " is not activated. please contact support@accenteasy.com";
                        }
                    }

                    try {
                        SecurityDAO securityDAO = new SecurityDAO();
                        Security security = securityDAO.getByAccount(user.getUsername());
                        if (security == null)
                            security = new Security();
                        security.setUsername(user.getUsername());
                        UserProfile.DeviceInfo deviceInfo = user.getDeviceInfo();
                        if (deviceInfo != null) {
                            security.setAppVersion(deviceInfo.getAppVersion());
                        }
                        security.setLoginType(user.getLoginType());
                        security.setPassword(StringUtil.md5(user.getPassword()));
                        if (security.getFirstAccess() == null)
                            security.setFirstAccess(new Date(System.currentTimeMillis()));
                        securityDAO.put(security);
                    } catch (Exception e) {
                        logger.error("Error when gather user security info. Message:: " + e.getMessage(),e);
                    }

                    try {
                        UsageDAO usageDAO = new UsageDAO();
                        Usage usage = new Usage();
                        UserDeviceDAO userDeviceDAO = new UserDeviceDAO();
                        UserProfile.DeviceInfo deviceInfo = user.getDeviceInfo();
                        usage.setUsername(user.getUsername());
                        if (deviceInfo != null) {
                            com.cmg.vrc.data.jdo.UserDevice userDevice= userDeviceDAO.getDeviceByIMEI(deviceInfo.getEmei());
                            if (userDevice == null)
                                userDevice = new UserDevice();
                            userDevice.setEmei(deviceInfo.getEmei());
                            userDevice.setDeviceName(deviceInfo.getDeviceName());
                            userDevice.setModel(deviceInfo.getModel());
                            userDevice.setOsApiLevel(deviceInfo.getOsApiLevel());
                            userDevice.setOsVersion(deviceInfo.getOsVersion());
                            if (userDevice.getAttachedDate() == null) {
                                userDevice.setAttachedDate(new Date(System.currentTimeMillis()));
                            }
                            userDeviceDAO.put(userDevice);

                            usage.setAppVersion(deviceInfo.getAppVersion());
                            usage.setEmei(deviceInfo.getEmei());
                        }
                        UserProfile.UserLocation location = user.getLocation();
                        if (location != null) {
                            usage.setLatitude(location.getLatitude());
                            usage.setLongitude(location.getLongitude());
                        }
                        usage.setTime(new Date(System.currentTimeMillis()));
                        usageDAO.put(usage);
                    } catch (Exception e) {
                        logger.error("Error when gather user data. Message:: " + e.getMessage(),e);
                    }
                }
                out.print(message);
            } else {
                out.print("No parameter found");
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
