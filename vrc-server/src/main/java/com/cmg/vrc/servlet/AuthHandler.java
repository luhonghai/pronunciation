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
import org.apache.commons.lang.StringUtils;
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
import java.util.*;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class AuthHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AuthHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_TYPE = "type";
    private class ResponseDataExt extends ResponseData<LoginToken> {
        public LoginToken loginTokens;

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String profile = request.getParameter(PARA_PROFILE);
            final String uuid=UUIDGenerator.generateUUID();
            final ResponseDataExt responseData = new ResponseDataExt();
            if (profile != null && profile.length() > 0) {
                Gson gson = new Gson();
                final UserProfile user = gson.fromJson(profile, UserProfile.class);
                String message = "success";
                responseData.setMessage("success");
                responseData.setStatus(true);
                String type = request.getParameter(PARA_TYPE);
                if (user != null) {
                    if (StringUtils.isEmpty(type)) {

                        UserDAO userDAO = new UserDAO();
                        if (user.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
                            User u = userDAO.getUserByEmailPassword(user.getUsername(), StringUtil.md5(user.getPassword()));
                            if (u != null) {
                                if (!u.isActivated()) {
                                    responseData.setMessage("account " + u.getUsername() + " is not activated. please contact support@accenteasy.com");
                                    responseData.setStatus(false);
                                }
                            } else {
                                responseData.setMessage("invalid email address or password");
                                responseData.setStatus(false);
                            }
                        } else {
                            User u = userDAO.getUserByEmail(user.getUsername());
                            if (u != null && !u.isActivated()) {
                                responseData.setMessage("account " + u.getUsername() + " is not activated. please contact support@accenteasy.com");
                                responseData.setStatus(false);
                            }
                        }
                        if(responseData.isStatus()){
                            LoginTokenDAO loginTokenDAO = new LoginTokenDAO();
                            LoginToken loginToken=new LoginToken();
                            UserProfile.DeviceInfo deviceInfo = user.getDeviceInfo();
                            try {
                                loginToken.setUserName(user.getUsername());
                                loginToken.setToken(uuid);
                                //loginToken.setAppName(deviceInfo.);
                                loginToken.setAppVersion(Float.parseFloat(deviceInfo.getAppVersion()));
                                loginToken.setDeviceName(deviceInfo.getDeviceName());
                                loginToken.setCreatedDate(new Date(System.currentTimeMillis()));
                                loginToken.setAccessDate(new Date(System.currentTimeMillis()));
                                loginTokenDAO.put(loginToken);
                            } catch (Exception e) {
                                logger.error("Error when gather user loginToken info. Message:: " + e.getMessage(), e);
                            }
                            responseData.loginTokens=loginTokenDAO.getByAccountAndDevice(user.getUsername(),deviceInfo.getDeviceName());
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
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
                                    logger.error("Error when gather user security info. Message:: " + e.getMessage(), e);
                                }


                                try {
                                    UsageDAO usageDAO = new UsageDAO();
                                    Usage usage = new Usage();
                                    UserDeviceDAO userDeviceDAO = new UserDeviceDAO();
                                    UserProfile.DeviceInfo deviceInfo = user.getDeviceInfo();
                                    usage.setUsername(user.getUsername());
                                    if (deviceInfo != null) {
                                        com.cmg.vrc.data.jdo.UserDevice userDevice = userDeviceDAO.getDeviceByIMEI(deviceInfo.getEmei());
                                        if (userDevice == null)
                                            userDevice = new UserDevice();
                                        userDevice.setImei(deviceInfo.getEmei());
                                        userDevice.setDeviceName(deviceInfo.getDeviceName());
                                        userDevice.setModel(deviceInfo.getModel());
                                        userDevice.setOsApiLevel(deviceInfo.getOsApiLevel());
                                        userDevice.setOsVersion(deviceInfo.getOsVersion());
                                        if (userDevice.getAttachedDate() == null) {
                                            userDevice.setAttachedDate(new Date(System.currentTimeMillis()));
                                        }
                                        userDeviceDAO.put(userDevice);

                                        usage.setAppVersion(deviceInfo.getAppVersion());
                                        usage.setImei(deviceInfo.getEmei());
                                    }
                                    UserProfile.UserLocation location = user.getLocation();
                                    if (location != null) {
                                        usage.setLatitude((long)location.getLatitude());
                                        usage.setLongitude((long)location.getLongitude());
                                    }
                                    usage.setTime(new Date(System.currentTimeMillis()));
                                    usageDAO.put(usage);
                                } catch (Exception e) {
                                    logger.error("Error when gather user data. Message:: " + e.getMessage(), e);
                                }
                            }
                        }).start();
                    } else if (type.equalsIgnoreCase("staff")) {
                        AdminDAO adminDAO = new AdminDAO();
                        Admin admin = adminDAO.getUserByEmailPassword(user.getUsername(), StringUtil.md5(user.getPassword()));
                        if (admin != null) {
                            message = "success";
                        } else {
                            message = "invalid email address or password";
                        }

                    } else {
                        message = "Invalid type";
                    }
                } else {
                    message = "No data found";
                    responseData.setMessage("No data found");
                    responseData.setStatus(false);
                }
                if(StringUtils.isEmpty(type)) {
                    out.print(gson.toJson(responseData));
                }else{
                    out.print(message);
                }
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
//private static final Logger logger = Logger.getLogger(AuthHandler.class
//        .getName());
//    private static String PARA_PROFILE = "profile";
//    private static String PARA_TYPE = "type";
//
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
//        try {
//            String profile = request.getParameter(PARA_PROFILE);
//            if (profile != null && profile.length() > 0) {
//                Gson gson = new Gson();
//                final UserProfile user = gson.fromJson(profile, UserProfile.class);
//                String message = "success";
//                if (user != null) {
//                    String type = request.getParameter(PARA_TYPE);
//                    if (StringUtils.isEmpty(type)) {
//                        UserDAO userDAO = new UserDAO();
//                        if (user.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
//                            User u = userDAO.getUserByEmailPassword(user.getUsername(), StringUtil.md5(user.getPassword()));
//                            if (u != null) {
//                                if (!u.isActivated()) {
//                                    message = "account " + u.getUsername() + " is not activated. please contact support@accenteasy.com";
//                                }
//                            } else {
//                                message = "invalid email address or password";
//                            }
//                        } else {
//                            User u = userDAO.getUserByEmail(user.getUsername());
//                            if (u != null && !u.isActivated()) {
//                                message = "account " + u.getUsername() + " is not activated. please contact support@accenteasy.com";
//                            }
//                        }
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    SecurityDAO securityDAO = new SecurityDAO();
//                                    Security security = securityDAO.getByAccount(user.getUsername());
//                                    if (security == null)
//                                        security = new Security();
//                                    security.setUsername(user.getUsername());
//                                    UserProfile.DeviceInfo deviceInfo = user.getDeviceInfo();
//                                    if (deviceInfo != null) {
//                                        security.setAppVersion(deviceInfo.getAppVersion());
//                                    }
//                                    security.setLoginType(user.getLoginType());
//                                    security.setPassword(StringUtil.md5(user.getPassword()));
//                                    if (security.getFirstAccess() == null)
//                                        security.setFirstAccess(new Date(System.currentTimeMillis()));
//                                    securityDAO.put(security);
//                                } catch (Exception e) {
//                                    logger.error("Error when gather user security info. Message:: " + e.getMessage(), e);
//                                }
//
//                                try {
//                                    UsageDAO usageDAO = new UsageDAO();
//                                    Usage usage = new Usage();
//                                    UserDeviceDAO userDeviceDAO = new UserDeviceDAO();
//                                    UserProfile.DeviceInfo deviceInfo = user.getDeviceInfo();
//                                    usage.setUsername(user.getUsername());
//                                    if (deviceInfo != null) {
//                                        com.cmg.vrc.data.jdo.UserDevice userDevice = userDeviceDAO.getDeviceByIMEI(deviceInfo.getEmei());
//                                        if (userDevice == null)
//                                            userDevice = new UserDevice();
//                                        userDevice.setImei(deviceInfo.getEmei());
//                                        userDevice.setDeviceName(deviceInfo.getDeviceName());
//                                        userDevice.setModel(deviceInfo.getModel());
//                                        userDevice.setOsApiLevel(deviceInfo.getOsApiLevel());
//                                        userDevice.setOsVersion(deviceInfo.getOsVersion());
//                                        if (userDevice.getAttachedDate() == null) {
//                                            userDevice.setAttachedDate(new Date(System.currentTimeMillis()));
//                                        }
//                                        userDeviceDAO.put(userDevice);
//
//                                        usage.setAppVersion(deviceInfo.getAppVersion());
//                                        usage.setImei(deviceInfo.getEmei());
//                                    }
//                                    UserProfile.UserLocation location = user.getLocation();
//                                    if (location != null) {
//                                        usage.setLatitude((long)location.getLatitude());
//                                        usage.setLongitude((long)location.getLongitude());
//                                    }
//                                    usage.setTime(new Date(System.currentTimeMillis()));
//                                    usageDAO.put(usage);
//                                } catch (Exception e) {
//                                    logger.error("Error when gather user data. Message:: " + e.getMessage(), e);
//                                }
//                            }
//                        }).start();
//                    } else if (type.equalsIgnoreCase("staff")) {
//                        AdminDAO adminDAO = new AdminDAO();
//                        Admin admin = adminDAO.getUserByEmailPassword(user.getUsername(), StringUtil.md5(user.getPassword()));
//                        if (admin != null) {
//                            message = "success";
//                        } else {
//                            message = "invalid email address or password";
//                        }
//                    } else {
//                        message = "Invalid type";
//                    }
//                } else {
//                    message = "No data found";
//                }
//                out.print(message);
//            } else {
//                out.print("No parameter found");
//            }
//        } catch (Exception e) {
//            logger.error("Error when login. Message:: " + e.getMessage(),e);
//            out.print("Error when login. Message:: " + e.getMessage());
//        }
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doPost(request,response);
//    }
}
