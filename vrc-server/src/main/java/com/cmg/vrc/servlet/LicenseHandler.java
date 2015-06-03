package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.LicenseCodeDAO;
import com.cmg.vrc.data.jdo.LicenseCode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class LicenseHandler extends BaseServlet {
    private static final Logger logger = Logger.getLogger(LicenseHandler.class
            .getName());


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String message = "";
            LicenseCodeDAO licenseCodeDAO = new LicenseCodeDAO();
            String code = request.getParameter("code");
            String action = request.getParameter("action");
            String account = request.getParameter("account");
            String imei = request.getParameter("imei");

            if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(action)  && !StringUtils.isEmpty(imei)) {
                if (action.equalsIgnoreCase("check")) {
                    LicenseCode licenseCode = licenseCodeDAO.getByEmail(account);
                    if (licenseCode != null) {
                        if (imei.equalsIgnoreCase(licenseCode.getImei())
                                && account.equalsIgnoreCase(licenseCode.getAccount())) {
                            if (licenseCode.isActivated()) {
                                message = "success";
                            } else {
                                message = "Code is suspended";
                            }
                        } else {
                            message = "Your account was registered with another device";
                        }
                    } else {
                        message = "You need a valid licence code";
                    }
                } else {
                    if (!StringUtils.isEmpty(code)) {
                        LicenseCode licenseCode = licenseCodeDAO.getByCode(code);
                        if (licenseCode != null) {
                            if (licenseCode.isActivated()) {
                                if (StringUtils.isEmpty(licenseCode.getAccount())
                                        || account.equalsIgnoreCase(licenseCode.getAccount())) {
                                    if (!StringUtils.isEmpty(licenseCode.getImei()) && !imei.equalsIgnoreCase(licenseCode.getImei())) {
                                        message = "Your code and account was registered by another device";
                                    } else {
                                        licenseCode.setAccount(account);
                                        licenseCode.setImei(imei);
                                        licenseCode.setActivated(true);
                                        licenseCode.setActivatedDate(new Date(System.currentTimeMillis()));
                                        licenseCodeDAO.put(licenseCode);
                                        message = "success";
                                    }
                                } else {
                                    message = "Code is already registered by other account";
                                }
                            } else {
                                message = "Code is suspended";
                            }
                        } else {
                            message = "Invalid licence code";
                        }
                    } else {
                        message = "Please check your licence code";
                    }
                }
            } else {
                message = "Invalid parameter";
            }
            out.print(message);
        } catch (Exception e) {
            logger.error("Error when login. Message: " + e.getMessage(),e);
            out.print("Error when login. Message: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
