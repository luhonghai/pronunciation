package com.cmg.vrc.servlet;

import com.cmg.vrc.common.DeviceInfoCommon;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.FeedbackDAO;
import com.cmg.vrc.data.jdo.Feedback;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class FeedbackHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            //create a new Map<String,String> to store all parameter
            Map<String, String> storePara = new HashMap<String, String>();
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();
            // Parse the request
            FileItemIterator iter = null;
            iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(stream);
                    storePara.put(name, value);
                }
            }
            String profile = storePara.get(PARA_PROFILE);
            if (profile != null && profile.length() > 0) {
                Gson gson = new Gson();
                UserProfile user = gson.fromJson(profile, UserProfile.class);
                String message = "";
                if (user != null) {
                    Feedback feedback = new Feedback();
                    feedback.setAccount(user.getUsername());
                    feedback.setAppVersion(storePara.get(DeviceInfoCommon.APP_VERSION));
                    feedback.setCreatedDate(new Date(System.currentTimeMillis()));
                    feedback.setDescription(storePara.get(DeviceInfoCommon.FEEDBACK_DESCRIPTION));
                    feedback.setDeviceName(storePara.get(DeviceInfoCommon.DEVICE_NAME));
                    feedback.setImei(storePara.get(DeviceInfoCommon.IMEI));
                    feedback.setOsApiLevel(storePara.get(DeviceInfoCommon.OS_API_LEVEL));
                    feedback.setOsVersion(storePara.get(DeviceInfoCommon.OS_VERSION));
                    feedback.setScreenshoot("");
                    feedback.setStackTrace(storePara.get(DeviceInfoCommon.STACK_TRACE));
                    FeedbackDAO feedbackDAO = new FeedbackDAO();
                    feedbackDAO.put(feedback);
                    message="Done";
                }
                out.print(message);
            } else {
                out.print("No parameter found");
            }
        } catch (FileUploadException e) {
            logger.error("Error when login. Message: " + e.getMessage(),e);
            out.print("Error when login. Message: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error when login. Message:: " + e.getMessage(),e);
            out.print("Error when login. Message:: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
