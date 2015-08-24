package com.cmg.vrc.servlet.amt;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.data.jdo.amt.RecordedSentenceHistory;
import com.cmg.vrc.job.SummaryReportJob;
import com.cmg.vrc.service.RecorderSentenceService;
import com.cmg.vrc.service.amt.TranscriptionService;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.servlet.ResponseData;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class RecordedSentenceHandler extends BaseServlet {
    private static final Logger logger = Logger.getLogger(RecordedSentenceHandler.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_SENTENCE_ID = "sentence";
    private static String PARA_VERSION = "version";
    private static String PARA_VERSIONMAX = "versionmax";
	private class ResponseDataExt extends ResponseData<RecordedSentence> {
		public List<RecordedSentence> RecordedSentences;

	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        AWSHelper awsHelper = new AWSHelper();
        ResponseDataExt responseData = new ResponseDataExt();
        RecorderDAO recorderDAO=new RecorderDAO();
        RecorderSentenceService recorderSentenceService=new RecorderSentenceService();
        responseData.setStatus(false);
        try {
            //create a new Map<String,String> to store all parameter
            Map<String, String> storePara = new HashMap<String, String>();
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();
            // Parse the request
            FileItemIterator iter = null;
            iter = upload.getItemIterator(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            File voiceRecordDir = new File(FileHelper.getTmpSphinx4DataDir(), "sentences");
            if (!voiceRecordDir.exists() || !voiceRecordDir.isDirectory()) {
                voiceRecordDir.mkdirs();
            }
            //String targetDir = Configuration.getValue(Configuration.VOICE_RECORD_DIR);
            String targetDir = voiceRecordDir.getAbsolutePath();
            String tmpFile = UUID.randomUUID().toString() + UUIDGenerator.generateUUID();
            String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
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
                        FileUtils.copyInputStreamToFile(stream, new File(tmpDir, tmpFile));
                        //FileHelper.saveFile(tmpDir, tmpFile, stream);
                    }
                }
            }

            String profile = storePara.get(PARA_PROFILE);
            String sentenceId = storePara.get(PARA_SENTENCE_ID);
            String versionmax = storePara.get(PARA_VERSIONMAX);
            int versions=1;
            if(recorderDAO.getCount()!=0){
                int versionmaxserver=recorderDAO.getLatestVersion();
                versions=versionmaxserver +1;
            }
            int versionmaxs=Integer.parseInt(versionmax);

            logger.info("SentenceID: " + sentenceId);
            logger.info("Profile: " + profile);
            if (profile != null && profile.length() > 0 && sentenceId != null && sentenceId.length() > 0) {
                UserProfile user = gson.fromJson(profile, UserProfile.class);
                File tmpFileIn = new File(tmpDir, tmpFile);


//                File target = new File(targetDir, user.getUsername());
//                if (!target.exists() && !target.isDirectory()) {
//                    target.mkdirs();
//                }

//                String uuid = UUIDGenerator.generateUUID();
//                String fileTempName  = PARA_SENTENCE_ID + "_" + uuid + "_raw" + ".wav";
//                File targetRaw = new File(target, fileTempName);
//                //String fileClean = word + "_" + uuid + "_clean" + ".wav";
//                //File targetClean = new File(target, fileClean);
//                FileUtils.moveFile(tmpFileIn, targetRaw);
//                try {
//                    if (tmpFileIn.exists())
//                        FileUtils.forceDelete(tmpFileIn);
//                } catch (Exception e) {}
//                awsHelper.uploadInThread("sentences" + "/" + user.getUsername() + "/" + fileTempName,
//                        targetRaw);
                File targetRaw = recorderSentenceService.saveRecorderFile(user, tmpFileIn, tmpDir, PARA_SENTENCE_ID);
                String condition = recorderSentenceService.clientUpdate(user, sentenceId, targetRaw, versions);
                List<RecordedSentence> result = null;
                if(condition.equalsIgnoreCase(RecorderSentenceService.RETURN_SUCCESS)){
                    result = recorderSentenceService.getListByVersionAndUsername(versionmaxs,user.getUsername());
                }
                logger.info("Try to save");
//                TranscriptionService transcriptionService = new TranscriptionService();
//                RecordedSentenceHistory result = transcriptionService.handleUploadedSentence(user, sentenceId, targetRaw);
               logger.info("Save completed");
                if (result != null) {
                    responseData.setStatus(true);
                    responseData.setMessage("success");
                    responseData.RecordedSentences = result;
                } else {

                    responseData.setMessage("Could not upload recorded voice");
                }

            } else {
                responseData.setMessage("No parameter found");
            }
        } catch (FileUploadException e) {
            logger.error("Error when upload file. FileUploadException, message: " + e.getMessage(),e);
            responseData.setMessage("Error when upload file. FileUploadException, message: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error when upload file. Common exception, message: " + e.getMessage(),e);
            responseData.setMessage("Error when upload file. Message: " + e.getMessage());
        }
        String responseText = gson.toJson(responseData);
        logger.info("Response: " + responseText);
        System.out.println("json response when upload : " + responseText);
        printMessage(response, gson.toJson(responseData));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
