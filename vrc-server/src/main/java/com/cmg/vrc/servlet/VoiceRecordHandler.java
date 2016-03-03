package com.cmg.vrc.servlet;

import com.cmg.lesson.services.ipa.IpaMapArpabetService;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.LoginTokenDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.LoginToken;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.job.CheckNumberDateJob;
import com.cmg.vrc.job.SummaryReportJob;
import com.cmg.vrc.processor.CustomFFMPEGLocator;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.service.PhonemeScoreService;
import com.cmg.vrc.service.UserVoiceModelService;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
public class VoiceRecordHandler extends HttpServlet {
    private static final Logger logger = Logger.getLogger(VoiceRecordHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_WORD = "word";
    private static String PARA_COUNTRY = "country";

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            SummaryReportJob.startJob();
        } catch (SchedulerException e) {
            logger.error("Could not start schedule SummaryReportJob", e);
        }
        try {
            CheckNumberDateJob.startJob();
        } catch (SchedulerException e) {
            logger.error("Could not start schedule CheckNumberDateJob", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        AWSHelper awsHelper = new AWSHelper();

        response.setCharacterEncoding("UTF-8");
        //DENP-238 : call service
        UserVoiceModelService uVoiceService = new UserVoiceModelService();
        PhonemeScoreService pScoreService = new PhonemeScoreService();
        ResponseData<UserVoiceModel> responseData = new ResponseData<>();
        responseData.setStatus(false);
        Gson gson = new Gson();
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
            String tmpFile = UUID.randomUUID().toString() + UUIDGenerator.generateUUID() + ".wav";
            String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = null;
                try {
                    stream = item.openStream();
                    if (item.isFormField()) {
                        logger.info(name);
                        String value = Streams.asString(stream);
                        storePara.put(name, value);
                    }else{
                        String getName = item.getName();
                        logger.info("getname = :" +getName);
                        // Process the input stream
                        if (getName.toLowerCase().endsWith(".wav")) {
                            FileUtils.copyInputStreamToFile(stream, new File(tmpDir, tmpFile));
                        }
                    }
                }finally {
                    IOUtils.closeQuietly(stream);
                }

            }

            String profile = storePara.get(PARA_PROFILE);
            String word = storePara.get(PARA_WORD);
            String countryId = storePara.get(PARA_COUNTRY);
            logger.info("word : " + word);
            if (profile != null && profile.length() > 0 && word != null && word.length() > 0) {
                UserProfile user = gson.fromJson(profile, UserProfile.class);
                LoginTokenDAO loginTokenDAO = new LoginTokenDAO();
                LoginToken loginToken = loginTokenDAO.getByAccountAndDevice(user.getUsername(), user.getDeviceInfo().getEmei());
                if (loginToken != null) {
                    File target = new File(targetDir, user.getUsername());
                    if (!target.exists() && !target.isDirectory()) {
                        target.mkdirs();
                    }
                    File tmpFileIn = new File(tmpDir, tmpFile);
                    String uuid = UUIDGenerator.generateUUID();
                    String fileTempName = word + "_" + uuid + "_raw" + ".wav";
                    File targetRaw = new File(target, fileTempName);
                    //String fileClean = word + "_" + uuid + "_clean" + ".wav";
                    //File targetClean = new File(target, fileClean);
                    AudioAttributes audio = new AudioAttributes();
                  //  audio.setBitRate(128000);
                    audio.setChannels(1);
                    audio.setSamplingRate(16000);
                    EncodingAttributes attrs = new EncodingAttributes();
                    attrs.setFormat("wav");
                    attrs.setAudioAttributes(audio);
                    logger.info("Origin file path :" + tmpFileIn.getAbsolutePath());
                    String env = Configuration.getValue(Configuration.SYSTEM_ENVIRONMENT);
                    Encoder encoder;
                    if (env.equalsIgnoreCase("prod") || env.equalsIgnoreCase("sat")
                            || env.equalsIgnoreCase("int")
                            || env.equalsIgnoreCase("aws")) {
                        encoder = new Encoder(
                                new CustomFFMPEGLocator()
                        );
                    } else {
                        encoder = new Encoder(
                                new CustomFFMPEGLocator()
                        );
                    }
                    try {
                        encoder.encode(tmpFileIn, targetRaw, attrs);
                    } catch (Exception e) {
                        // ingore
                    }

                    //FileUtils.moveFile(tmpFileIn, targetRaw);
                    try {
                        if (tmpFileIn.exists())
                            FileUtils.forceDelete(tmpFileIn);
                    } catch (Exception e) {
                    }
                    awsHelper.uploadInThread(Constant.FOLDER_RECORDED_VOICES + "/" + user.getUsername() + "/" + fileTempName,
                            targetRaw);

//                AudioCleaner cleaner = new SoXCleaner(targetClean,targetRaw);
//                cleaner.clean();

                    UserVoiceModel model = new UserVoiceModel();
                    //model.setCleanRecordFile(fileClean);
                    logger.info("username : " + user.getUsername());
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
                    //DENP-238 : set version for user voice model
                    model.setVersion(uVoiceService.getMaxVersion(user.getUsername()));
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
                    UserVoiceModelDAO dao = new UserVoiceModelDAO();
                    if (result != null)
                        model.setScore(result.getScore());
                    model = dao.createObj(model);
                    if (result != null) {
                        model.setResult(result);
                        //DENP-238 : save phoneme score to database
                        int maxVersionPhoneme = pScoreService.getMaxVersion(user.getUsername());
                        model.setVersionPhoneme(maxVersionPhoneme);
                        pScoreService.addPhonemeScore(model);
                    }
                    //set ipa for result phoneme send to client;
                    IpaMapArpabetService ipaService = new IpaMapArpabetService();
                    model = ipaService.setIpa(model);
                    String output = gson.toJson(model);
                    logger.info("json from server to client : " + output);
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
                    responseData.setStatus(true);
                    responseData.setData(model);
                    responseData.setMessage("success");
                } else {
                    responseData.setMessage("invalid token");
                }
            } else {
                responseData.setMessage("no parameter found");
            }
        } catch (Exception e) {
            logger.error("Error when upload file. Common exception, message: " + e.getMessage(),e);
            responseData.setMessage("Error: " + e.getMessage());
        }
        out.print(gson.toJson(responseData));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
