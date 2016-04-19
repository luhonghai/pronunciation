package com.cmg.vrc.servlet;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class LicenseHandler extends BaseServlet {

    private static final Logger logger = Logger.getLogger(LicenseHandler.class
            .getName());

    class LicenseData {
        String code;
        UserProfile.DeviceInfo deviceInfo;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String message = "";
            LicenseCodeDAO licenseCodeDAO = new LicenseCodeDAO();
            LicenseCodeCompanyDAO licenseCodeCompanyDAO=new LicenseCodeCompanyDAO();
            LicenseCodeCompany licenseCodeCompany=new LicenseCodeCompany();
            TeacherMappingCompanyDAO teacherMappingCompanyDAO=new TeacherMappingCompanyDAO();
            StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
            String code = request.getParameter("code");
            String action = request.getParameter("action");
            String account = request.getParameter("account");
            String token = request.getParameter("token");
            String imei = request.getParameter("imei");

            if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(action)  && !StringUtils.isEmpty(imei)) {
                LoginTokenDAO loginTokenDAO = new LoginTokenDAO();
                UserDeviceDAO userDeviceDAO = new UserDeviceDAO();
                account = account.toLowerCase();
                if (action.equalsIgnoreCase("check")) {
                    List<LicenseCode> licenseCodes = licenseCodeDAO.listByEmail(account);
                    boolean check = false;
                    if (licenseCodes != null && licenseCodes.size() > 0) {
                        for (LicenseCode licenseCode : licenseCodes) {
                            if (imei.equalsIgnoreCase(licenseCode.getImei()) && licenseCode.isActivated()) {
                                check = true;
                                break;
                            }
                        }
                    }
                    if (check) {
                        message = "success";
                    } else {
                        message = "you need a valid licence code";
                    }
                } else if (action.equalsIgnoreCase("list")) {
                    ResponseData<List<LicenseData>> responseData = new ResponseData<>();
                    responseData.setStatus(false);
                    try {
                        LoginToken loginToken = loginTokenDAO.getByAccountAndToken(account, token);
                        if (loginToken != null) {
                            List<LicenseCode> licenseCodes = licenseCodeDAO.listByEmail(account);
                            List<LicenseData> licenseDatas = new ArrayList<>();
                            if (licenseCodes != null && licenseCodes.size() > 0) {
                                for (LicenseCode licenseCode : licenseCodes) {
                                    if (!licenseCode.getImei().equals(imei)) {
                                        LicenseData data = new LicenseData();
                                        data.code = licenseCode.getCode();
                                        UserProfile.DeviceInfo deviceInfo = new UserProfile.DeviceInfo();
                                        UserDevice device = userDeviceDAO.getDeviceByIMEI(licenseCode.getImei());
                                        deviceInfo.setDeviceName(device.getDeviceName());
                                        deviceInfo.setModel(device.getModel());
                                        deviceInfo.setOsApiLevel(device.getOsApiLevel());
                                        deviceInfo.setOsVersion(device.getOsVersion());
                                        deviceInfo.setEmei(device.getImei());
                                        data.deviceInfo = deviceInfo;
                                        licenseDatas.add(data);
                                    }
                                }
                            }
                            if (licenseDatas.size() > 0) {
                                responseData.setData(licenseDatas);
                                responseData.setMessage("success");
                                responseData.setStatus(true);
                            } else {
                                responseData.setMessage("no licence code found");
                            }
                        } else {
                            responseData.setMessage("invalid login token " + token + " of account " + account);
                        }
                    } catch (Exception e) {
                        logger.error("Could not list all license of account " + account, e);
                        responseData.setMessage("Error: " + e.getMessage());
                    } finally {
                        message = new Gson().toJson(responseData);
                    }
                } else if(action.equalsIgnoreCase("switch")) {
                    ResponseData<Object> responseData = new ResponseData<>();
                    responseData.setStatus(false);
                    try {
                        LoginToken loginToken = loginTokenDAO.getByAccountAndToken(account, token);
                        if (loginToken != null) {
                            LicenseCode licenseCode = licenseCodeDAO.getByCodeAndEmail(code, account);
                            if (licenseCode != null) {
                                if (licenseCode.isActivated()) {
                                    licenseCode.setImei(imei);
                                    licenseCode.setActivatedDate(new Date(System.currentTimeMillis()));
                                    licenseCodeDAO.put(licenseCode);
                                    responseData.setStatus(true);
                                    responseData.setMessage("success");
                                } else {
                                    responseData.setMessage("licence code is suspended");
                                }
                            } else {
                                responseData.setMessage("invalid licence code");
                            }
                        } else {
                            responseData.setMessage("invalid login token");
                        }
                    } catch (Exception e) {
                        logger.error("Could not switch of account " + account, e);
                        responseData.setMessage("Error: " + e.getMessage());
                    } finally {
                        message = new Gson().toJson(responseData);
                    }
                } else {
                    if (!StringUtils.isEmpty(code)) {
                        LicenseCode licenseCode = licenseCodeDAO.getByCode(code);
                        licenseCodeCompany=licenseCodeCompanyDAO.getByCode(code);
                        AdminDAO adminDAO=new AdminDAO();
                        if (licenseCode != null) {
                            if (licenseCode.isActivated()) {
                                if (StringUtils.isEmpty(licenseCode.getAccount())) {
                                    licenseCode.setAccount(account);
                                    licenseCode.setImei(imei);
                                    licenseCode.setActivated(true);
                                    licenseCode.setActivatedDate(new Date(System.currentTimeMillis()));
                                    licenseCodeDAO.put(licenseCode);
                                    if(licenseCodeCompany!=null){
                                        String company=licenseCodeCompany.getCompany();
                                        List<TeacherMappingCompany> teacherMappingCompanies=teacherMappingCompanyDAO.getByCompany(company);
                                        for(TeacherMappingCompany mappingCompany:teacherMappingCompanies){
                                            Admin admin=adminDAO.getUserByEmail(mappingCompany.getUserName());
                                           StudentMappingTeacher studentMappingTeacher=new StudentMappingTeacher();
                                            studentMappingTeacher.setStudentName(account);
                                            studentMappingTeacher.setTeacherName(mappingCompany.getUserName());
                                            studentMappingTeacher.setFirstTeacherName(admin.getFirstName());
                                            studentMappingTeacher.setLastTeacherName(admin.getLastName());
                                            studentMappingTeacher.setIsDeleted(false);
                                            studentMappingTeacher.setStatus(Constant.STATUS_PENDING);
                                            studentMappingTeacher.setLicence(true);
                                            studentMappingTeacherDAO.put(studentMappingTeacher);
                                        }

                                    }
                                    message = "success";
                                } else {
                                    if (account.equalsIgnoreCase(licenseCode.getAccount())) {
                                        if (imei.equalsIgnoreCase(licenseCode.getImei())) {
                                            message = "success";
                                        } else {
                                            message = "licence code and account are already registered by another device";
                                        }
                                    } else {
                                        message = "licence code is already registered by another account";
                                    }
                                }
                            } else {
                                message = "licence code is suspended";
                            }
                        } else {
                            message = "invalid licence code";
                        }
                    } else {
                        message = "please check your licence code";
                    }
                }
            } else {
                message = "invalid parameter";
            }
            out.print(message);
        } catch (Exception e) {
            logger.error("Error when login. Message: " + e.getMessage(),e);
            out.print("sorry our engineers are just upgrading the server, please try again");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
