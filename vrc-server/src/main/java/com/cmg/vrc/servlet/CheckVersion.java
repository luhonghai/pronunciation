package com.cmg.vrc.servlet;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.DatabaseVersionDAO;
import com.cmg.vrc.data.jdo.DatabaseVersion;
import com.cmg.vrc.util.AWSHelper;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by CMGT400 on 11/16/2015.
 */
@WebServlet(name = "CheckVersion")
public class CheckVersion extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CheckVersion.class
            .getName());

    class VersionResponseData extends ResponseData<String> {
        int version;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String version = request.getParameter("version");
        PrintWriter out = response.getWriter();
        VersionResponseData responseData = new VersionResponseData();
        responseData.setStatus(false);
        try {
            int v = Integer.parseInt(version);
            DatabaseVersionDAO databaseVersionDAO = new DatabaseVersionDAO();
            DatabaseVersion db = databaseVersionDAO.getSelectedVersion();
            if (db != null) {
                if (db.getVersion() != v) {
                    AWSHelper awsHelper = new AWSHelper();
                    responseData.version = db.getVersion();
                    responseData.setData(awsHelper.generatePresignedUrl(Constant.FOLDER_DATABASE
                            + "/"
                            + db.getFileName()));
                    responseData.setMessage("success");
                    responseData.setStatus(true);
                } else {
                    responseData.setMessage("Current selected version is " + v);
                }
            } else {
                responseData.setMessage("No selected version found");
            }
        } catch (Exception e) {
            logger.error("Could not get selected database version",e);
            responseData.setMessage("Error when get database version. Message: " + e.getMessage());
        }
        out.print(new Gson().toJson(responseData));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}