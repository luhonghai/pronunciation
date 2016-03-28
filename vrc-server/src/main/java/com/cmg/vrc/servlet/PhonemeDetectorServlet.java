package com.cmg.vrc.servlet;

import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by luhonghai on 3/28/16.
 */
public class PhonemeDetectorServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String audioUrl = req.getParameter("url");
        String word = req.getParameter("word");
        logger.info("Analyze word " + word + " from URL: " + audioUrl);
        File tmpFile = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".tmp.wav");
        ResponseData<SphinxResult> responseData = new ResponseData<>();
        responseData.setMessage("");
        responseData.setStatus(false);
        try {
            FileUtils.copyURLToFile(new URL(audioUrl), tmpFile);
            PhonemesDetector phonemesDetector = new PhonemesDetector(tmpFile, word);
            responseData.setData(phonemesDetector.analyze());
            responseData.setStatus(true);
            responseData.setMessage("success");
        } catch (Exception e) {
            responseData.setMessage(e.getMessage());
        } finally {
            if (tmpFile.exists()) {
                try {
                    FileUtils.forceDelete(tmpFile);
                } catch (Exception e) {}
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        resp.getWriter().write(gson.toJson(responseData));
    }
}
