package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.http.HttpContacter;
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
import java.util.Date;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class AuthHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AuthHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_TYPE = "type";
    private static String PARA_CHECK = "check";
    private class ResponseDataExt extends ResponseData<LoginToken> {
        //
    }
    class ValidResponse {
        String id;
    }

    class GoogleValidResponse {
        String access_type;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String profile = request.getParameter(PARA_PROFILE);
            String check = request.getParameter(PARA_CHECK);
            boolean willCheck = check != null && check.length() > 0 && check.equalsIgnoreCase("true");
            final String uuid = UUIDGenerator.generateUUID();
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
                        UserProfile.DeviceInfo deviceInfo = user.getDeviceInfo();
                        int appVersion = -1;
                        if (deviceInfo.getAppName() != null && deviceInfo.getAppName().length() > 0) {
                            appVersion = Integer.parseInt(deviceInfo.getAppName());
                        }
                        if (appVersion >= 400000) {
                            UserDAO userDAO = new UserDAO();
                            User u = userDAO.getUserByEmail(user.getUsername());
                            LoginTokenDAO loginTokenDAO = new LoginTokenDAO();
                            LoginToken loginToken = loginTokenDAO.getByAccountAndDevice(user.getUsername(), deviceInfo.getEmei());
                            if (u != null && !u.isActivated()) {
                                responseData.setMessage("account " + u.getUsername() + " is not activated. please contact support@accenteasy.com");
                                responseData.setStatus(false);
                            } else if (loginToken != null && willCheck && user.getToken().length() > 0 && user.getToken().equals(loginToken.getToken())) {
                                logger.info("Check current token is matched. Allow access by default. " + user.getToken() + ". Username: " + user.getUsername());
                            } else if (user.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
                                u = userDAO.getUserByEmailPassword(user.getUsername(), StringUtil.md5(user.getPassword()));
                                if (u != null) {
                                    if (!u.isActivated()) {
                                        responseData.setMessage("account " + u.getUsername() + " is not activated. please contact support@accenteasy.com");
                                        responseData.setStatus(false);
                                    }
                                } else {
                                    responseData.setMessage("invalid email address or password");
                                    responseData.setStatus(false);
                                }
                            } else if (user.getLoginType().equalsIgnoreCase(UserProfile.TYPE_GOOGLE_PLUS)) {
                                logger.info("Additional token: " + user.getAdditionalToken());
                                HttpContacter httpContacter = new HttpContacter();
                                String output = httpContacter.get("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + user.getAdditionalToken());
                                logger.info("google plus check access token response: " + output);
                                try {
                                    GoogleValidResponse googleValidResponse = gson.fromJson(output, GoogleValidResponse.class);
                                    if (googleValidResponse != null
                                            && googleValidResponse.access_type != null
                                            && googleValidResponse.access_type.equalsIgnoreCase("online")) {
                                        logger.info("valid google+ access token: " + user.getAdditionalToken());
                                    } else {
                                        responseData.setStatus(false);
                                        responseData.setMessage("invalid ID token. please contact support@accenteasy.com");
                                    }
                                } catch (Exception e) {
                                    logger.error("could not check google plus access token", e);
                                    responseData.setStatus(false);
                                    responseData.setMessage("invalid ID token. please contact support@accenteasy.com");
                                }
                            } else if (user.getLoginType().equalsIgnoreCase(UserProfile.TYPE_FACEBOOK)) {
                                logger.info("Additional token: " + user.getAdditionalToken());
                                HttpContacter httpContacter = new HttpContacter();
                                String output = httpContacter.get("https://graph.facebook.com/me?fields=id&access_token=" + user.getAdditionalToken());
                                logger.info("facebook check access token response: " + output);

                                try {
                                    ValidResponse validResponse = gson.fromJson(output, ValidResponse.class);
                                    if (validResponse != null && validResponse.id != null && validResponse.id.length() > 0) {
                                        logger.info("Valid facebook access token: " + user.getAdditionalToken() + ". ID: " + validResponse.id);
                                    } else {
                                        responseData.setStatus(false);
                                        responseData.setMessage("invalid ID token. please contact support@accenteasy.com");
                                    }
                                } catch (Exception e) {
                                    logger.error("could not parse facebook access token", e);
                                    responseData.setStatus(false);
                                    responseData.setMessage("invalid ID token. please contact support@accenteasy.com");
                                }
                            } else {
                                responseData.setStatus(false);
                                responseData.setMessage("invalid login type");
                            }
                            if (responseData.isStatus()) {
                                try {
                                    if (loginToken == null || !willCheck) {
                                        loginToken = new LoginToken();
                                        loginToken.setUserName(user.getUsername());
                                        loginToken.setToken(uuid);
                                        loginToken.setDeviceName(deviceInfo.getEmei());
                                        loginToken.setCreatedDate(new Date(System.currentTimeMillis()));
                                    }
                                    loginToken.setAppName(deviceInfo.getAppVersion());
                                    loginToken.setAppVersion(appVersion);
                                    loginToken.setAccessDate(new Date(System.currentTimeMillis()));
                                    loginTokenDAO.put(loginToken);
                                    responseData.setData(loginTokenDAO.getByAccountAndDevice(user.getUsername(), deviceInfo.getEmei()));
                                } catch (Exception e) {
                                    responseData.setStatus(false);
                                    responseData.setMessage("Error: " + e.getMessage());
                                    logger.error("Could not get user token . Username: " + user.getUsername() + ". IMEI: " + deviceInfo.getEmei(), e);
                                }
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
                                            usage.setLatitude((long) location.getLatitude());
                                            usage.setLongitude((long) location.getLongitude());
                                        }
                                        usage.setTime(new Date(System.currentTimeMillis()));
                                        usageDAO.put(usage);
                                    } catch (Exception e) {
                                        logger.error("Error when gather user data. Message:: " + e.getMessage(), e);
                                    }
                                }
                            }).start();
                        } else {
                            responseData.setStatus(false);
                            responseData.setMessage("please upgrade application to latest version. https://play.google.com/store/apps/details?id=com.cmg.android.bbcaccent");
                        }
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
            logger.error("Error when login. Message: " + e.getMessage(),e);
            out.print("could not connect to server. please contact support@accenteasy.com");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
