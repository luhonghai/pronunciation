package com.cmg.vrc.servlet;

import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.processor.CustomFFMPEGLocator;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.DictionaryHelper;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 3/28/16.
 */


public class PhonemeDetectorServlet extends BaseServlet {

    class TestResponseData extends ResponseData<SphinxResult> {
        Map<String, List<String>> neighbourPhones;
        Map<String, String> beepPhonemes = DictionaryHelper.BEEP_TO_CMU_PHONEMES;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String audioUrl = req.getParameter("url");
        String word = req.getParameter("word");
        logger.info("Analyze word " + word + " from URL: " + audioUrl);
        File tmpFile = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".raw.wav");
        File tmpFileWav = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".tmp.wav");
        TestResponseData responseData = new TestResponseData();
        responseData.setMessage("");
        responseData.setStatus(false);
        try {
            FileUtils.copyURLToFile(new URL(audioUrl), tmpFile);

            AudioAttributes audio = new AudioAttributes();
            //  audio.setBitRate(128000);
            audio.setChannels(1);
            audio.setSamplingRate(16000);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("wav");
            attrs.setAudioAttributes(audio);
            logger.info("Origin file path :" + tmpFile.getAbsolutePath());
            String env = Configuration.getValue(Configuration.SYSTEM_ENVIRONMENT);
            Encoder encoder;
            if (env.equalsIgnoreCase("prod") || env.equalsIgnoreCase("sat")
                    || env.equalsIgnoreCase("int")
                    || env.equalsIgnoreCase("aws")) {
                encoder = new Encoder(new CustomFFMPEGLocator());
            } else {
                encoder = new Encoder(new CustomFFMPEGLocator.MacFFMPEGLocator());
            }
            try {
                encoder.encode(tmpFile, tmpFileWav, attrs);
            } catch (Exception e) {
                // ingore
            }
            PhonemesDetector phonemesDetector = new PhonemesDetector(tmpFileWav, word);
            phonemesDetector.setAllowAdditionalData(true);
            responseData.setData(phonemesDetector.analyze());
            responseData.neighbourPhones = phonemesDetector.getNeighbourPhones();
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
            if (tmpFileWav.exists()) {
                try {
                    FileUtils.forceDelete(tmpFileWav);
                } catch (Exception e) {}
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        resp.getWriter().write(gson.toJson(responseData));
    }
}