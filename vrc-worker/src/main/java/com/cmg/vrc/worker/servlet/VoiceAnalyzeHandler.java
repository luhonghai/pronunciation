package com.cmg.vrc.worker.servlet;

import com.cmg.vrc.worker.AnalyzingRequest;
import com.cmg.vrc.worker.AnalyzingResponse;
import com.cmg.vrc.worker.common.Constant;
import com.cmg.vrc.worker.sphinx.PhonemesDetector;
import com.cmg.vrc.worker.sphinx.SphinxResult;
import com.cmg.vrc.worker.util.AWSHelper;
import com.cmg.vrc.worker.util.FileHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Created by luhonghai on 10/16/14.
 */
public class VoiceAnalyzeHandler extends HttpServlet {

    private static final long serialVersionUID = 6118076230101815405L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("Receive new message");
        String userAgent = request.getHeader("User-Agent");
        boolean isWorker = !StringUtils.isEmpty(userAgent) && userAgent.toLowerCase().contains("aws-sqsd");
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setPrettyPrinting().create();
        AnalyzingResponse responseData = new AnalyzingResponse();
        AnalyzingRequest analyzingRequest = null;
        try {
            responseData.setStartTime(System.currentTimeMillis());
            String body = getBody(request);
            log("Body: " + body);
            analyzingRequest = gson.fromJson(body, AnalyzingRequest.class);
            responseData.setRequest(analyzingRequest);
            PhonemesDetector detector = new PhonemesDetector(analyzingRequest);
            responseData.setResult(detector.analyze());
            responseData.setStatus(true);
            responseData.setMessage("success");
        } catch (Exception e) {
            log("could not analyzing voice", e);
            responseData.setStatus(false);
            responseData.setMessage( "Error: " + e.getMessage());
        } finally {
            responseData.setExecutionTime(System.currentTimeMillis() - responseData.getStartTime());
            String data = gson.toJson(responseData);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            if (analyzingRequest != null && isWorker) {
                try {
                    AWSHelper awsHelper = new AWSHelper(analyzingRequest.getRegionName(), analyzingRequest.getBucketName());
                    File tmpFile = new File(FileHelper.getTmpDir(Constant.CACHE_FOLDER), analyzingRequest.getAnalyzingKey());
                    FileUtils.write(tmpFile, data, "UTF-8");
                    if (tmpFile.exists()) {
                        awsHelper.upload(Constant.CACHE_FOLDER + "/" + analyzingRequest.getAnalyzingKey(), tmpFile);
                        FileUtils.forceDelete(tmpFile);
                    }
                } catch (Exception e) {
                    log("Could not upload response data to AWS S3", e);
                }
            }
            log("Response: " + data);
            response.getWriter().write(data);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    private String getBody(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {

                }
            }
        }
    }
}
