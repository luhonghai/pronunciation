package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.job.SummaryReportJob;
import com.cmg.vrc.processor.AudioCleaner;
import com.cmg.vrc.processor.SoXCleaner;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.FileHelper;
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
