package com.cmg.vrc.servlet;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.processor.AudioCleaner;
import com.cmg.vrc.http.FileCommon;
import com.cmg.vrc.http.FileUploader;
import com.cmg.vrc.http.exception.UploaderException;
import com.cmg.vrc.job.SummaryReportJob;
import com.cmg.vrc.processor.SoXCleaner;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.AWSHelper;
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
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

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

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            SummaryReportJob.startJob();
        } catch (SchedulerException e) {
            logger.error("Could not start schedule", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        AWSHelper awsHelper = new AWSHelper();
        try {
            //create a new Map<String,String> to store all parameter
            Map<String, String> storePara = new HashMap<String, String>();
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();
            // Parse the request
            FileItemIterator iter = null;
            iter = upload.getItemIterator(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            File voiceRecordDir = new File(FileHelper.getTmpSphinx4DataDir(), "voices");
            if (!voiceRecordDir.exists() || !voiceRecordDir.isDirectory()) {
                voiceRecordDir.mkdirs();
            }
            //String targetDir = Configuration.getValue(Configuration.VOICE_RECORD_DIR);
            String targetDir = voiceRecordDir.getAbsolutePath();
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
                //String fileClean = word + "_" + uuid + "_clean" + ".wav";
                //File targetClean = new File(target, fileClean);
                FileUtils.moveFile(tmpFileIn, targetRaw);
                try {
                    if (tmpFileIn.exists())
                        FileUtils.forceDelete(tmpFileIn);
                } catch (Exception e) {}
                awsHelper.uploadInThread(Constant.FOLDER_RECORDED_VOICES + "/" + user.getUsername() + "/" + fileTempName,
                        targetRaw);

//                AudioCleaner cleaner = new SoXCleaner(targetClean,targetRaw);
//                cleaner.clean();

                UserVoiceModel model = new UserVoiceModel();
                //model.setCleanRecordFile(fileClean);
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
                SphinxResult result = null;
                PhonemesDetector detector = new PhonemesDetector(targetRaw, model.getWord());
                try {
                    result = detector.analyze();
                } catch (Exception ex) {
                    logger.error("Could not analyze word", ex);
                }
                if (result != null) {
                    model.setResult(result);
                    model.setScore(result.getScore());
                }

                UserVoiceModelDAO dao = new UserVoiceModelDAO();
                dao.create(model);
                String output = gson.toJson(model);
                File jsonModel = new File(target, word + "_" + uuid + ".json");
                FileUtils.writeStringToFile(jsonModel, output);
                awsHelper.uploadInThread(Constant.FOLDER_RECORDED_VOICES + "/" + user.getUsername() + "/" + word + "_" + uuid + ".json",
                        jsonModel);
                if (jsonModel.exists()) {
                    try {
                        FileUtils.forceDelete(jsonModel);
                    } catch (Exception e) {

                    }
                }
                if (targetRaw.exists()) {
                    try {
                        FileUtils.forceDelete(targetRaw);
                    } catch (Exception e) {

                    }
                }
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
