package com.cmg.vrc.servlet;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.filters.HighPass;
import be.tarsos.dsp.filters.LowPassFS;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.PhonemesDetector;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class VoiceRecordHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(VoiceRecordHandler.class
            .getName());
    private static String PARA_FILE_NAME = "FILE_NAME";
    private static String PARA_FILE_PATH = "FILE_PATH";
    private static String PARA_FILE_TYPE = "FILE_TYPE";
    private static String PARA_PROFILE = "profile";
    private static String PARA_WORD = "word";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            //create a new Map<String,String> to store all parameter
            Map<String, String> storePara = new HashMap<String, String>();
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();
            // Parse the request
            FileItemIterator iter = null;
            iter = upload.getItemIterator(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

            String targetDir = Configuration.getValue(Configuration.VOICE_RECORD_DIR);
            String tmpFile = UUID.randomUUID().toString();
            String tmpDir = System.getProperty("java.io.tmpdir");
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    logger.info(name);
                    String value = Streams.asString(stream);
                    storePara.put(name, value);
                }else{
                    String getName = item.getName();
                    logger.info("getname = :" +getName);
                    // Process the input stream
                    if(getName.endsWith(".wav")){
                        FileHelper.saveFile(tmpDir, tmpFile, stream);
                    }
                }
            }
            String profile = storePara.get(PARA_PROFILE);
            String word = storePara.get(PARA_WORD);
            if (profile != null && profile.length() > 0 && word != null && word.length() > 0) {
                Gson gson = new Gson();
                UserProfile user = gson.fromJson(profile, UserProfile.class);

                File target = new File(targetDir, user.getUsername());
                if (!target.exists() && !target.isDirectory()) {
                    target.mkdirs();
                }
                File tmpFileIn = new File(tmpDir, tmpFile);
                String uuid = UUIDGenerator.generateUUID();
                String fileTempName  = word + "_" + uuid + "_raw" + ".wav";
                File targetRaw = new File(target, fileTempName);
                String fileClean = word + "_" + uuid + "_clean" + ".wav";
                File targetClean = new File(target, fileClean);
                FileUtils.moveFile(tmpFileIn, targetRaw);
                tmpFileIn.delete();
                AudioFormat format = AudioSystem.getAudioFileFormat(targetRaw).getFormat();
                AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(targetRaw, 1024, 0);
                dispatcher.addAudioProcessor(new LowPassFS(400f, format.getSampleRate()));
               // dispatcher.addAudioProcessor(new HighPass(50f, format.getSampleRate()));
                dispatcher.addAudioProcessor(new GainProcessor(2));
                dispatcher.addAudioProcessor(new WaveformWriter(format, targetClean.getAbsolutePath()));
                dispatcher.run();

                UserVoiceModel model = new UserVoiceModel();
                model.setCleanRecordFile(fileClean);
                model.setUsername(user.getUsername());
                model.setCountry(user.getCountry());
                model.setDob(user.getDob());
                model.setDuration(user.getDuration());
                model.setTime(user.getTime());
                model.setServerTime(System.currentTimeMillis());
                model.setEnglishProficiency(user.getEnglishProficiency());
                model.setGender(user.isGender());
                model.setRecordFile(fileTempName);
                model.setWord(word);
                model.setNativeEnglish(user.isNativeEnglish());
                model.setUuid(user.getUuid());
                UserProfile.UserLocation location = user.getLocation();
                if (location != null) {
                    model.setLatitude(location.getLatitude());
                    model.setLongitude(location.getLongitude());
                }

                PhonemesDetector detector = new PhonemesDetector(targetClean);
                PhonemesDetector.Result result = detector.analyze();
                if (result != null) {
                    model.setPhonemes(result.getPhonemes());
                    model.setHypothesis(result.getHypothesis());
                }

                UserVoiceModelDAO dao = new UserVoiceModelDAO();
                dao.create(model);
                String output = gson.toJson(model);
                FileUtils.writeStringToFile(new File(target, word + "_" + uuid + ".json"), output);
                out.print(output);
            } else {
                out.print("No parameter found");
            }
        } catch (FileUploadException e) {
            logger.error("Error when upload file. FileUploadException, message: " + e.getMessage(),e);
            out.print("Error when upload file. FileUploadException, message: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error when upload file. Common exception, message: " + e.getMessage(),e);
            out.print("Error when upload file. Message: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
