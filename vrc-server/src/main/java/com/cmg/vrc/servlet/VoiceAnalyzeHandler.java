package com.cmg.vrc.servlet;

import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.FileHelper;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/16/14.
 */
public class VoiceAnalyzeHandler extends HttpServlet {

    private static final Logger logger = Logger.getLogger(VoiceAnalyzeHandler.class.getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        File tempWav = null;
        try {
            //create a new Map<String,String> to store all parameter
            Map<String, String> storePara = new HashMap<String, String>();
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();
            // Parse the request
            FileItemIterator iter = null;
            iter = upload.getItemIterator(request);
            String uuid = UUID.randomUUID().toString();
            String tmpDir = System.getProperty("java.io.tmpdir");
            tempWav = new File(tmpDir, uuid);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(stream);
                    storePara.put(name, value);
                }else{
                    String getName = item.getName();
                    // Process the input stream
                    if(getName.endsWith(".wav")){
                        FileHelper.saveFile(tmpDir, uuid, stream);
                    }
                }
            }
            String key = storePara.get("key");
            String apiKey = Configuration.getValue(Configuration.API_KEY);
            if (key != null && key.length() > 0 && key.equalsIgnoreCase(apiKey)) {
                if (tempWav.exists()) {
                    Gson gson = new Gson();
                    PhonemesDetector detector = new PhonemesDetector(tempWav, "");
                    SphinxResult result = detector.analyze();
                    out.print(gson.toJson(result));
                } else {
                    out.print("No file found");
                }
            } else {
                out.print("No parameter found");
            }
        } catch (FileUploadException e) {
            logger.log(Level.SEVERE,"Error when upload file. FileUploadException, message: " + e.getMessage(),e);
            out.print("Error when upload file. FileUploadException, message: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error when upload file. Common exception, message: " + e.getMessage(),e);
            out.print("Error when upload file. Message: " + e.getMessage());
        } finally {
            if (tempWav != null && tempWav.exists())
                FileUtils.forceDelete(tempWav);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
