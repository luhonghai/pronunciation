package com.cmg.lesson.servlet;

import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.services.calculation.ScoreService;
import com.cmg.lesson.services.question.WeightForPhonemeService;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.job.SummaryReportJob;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
 * Created by lantb on 2015-10-16.
 */
@WebServlet(name = "CalculationServlet")
public class CalculationServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CalculationServlet.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_WORD = "word";
    private static String PARA_WORD_ID = "idWord";
    private static String PARA_QUESTION_ID = "idQuestion";
    private static String PARA_COUNTRY_ID = "idCountry";
    private static String PARA_SESSION_ID = "session";
    private static String PARA_LESSON_COLLECTION_ID = "idLessonCollection";
    private static String PARA_TYPE = "type";

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
        ServletFileUpload upload = new ServletFileUpload();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        try {
            File voiceRecordDir = new File(FileHelper.getTmpSphinx4DataDir(), "voices");
            if (!voiceRecordDir.exists() || !voiceRecordDir.isDirectory()) {
                voiceRecordDir.mkdirs();
            }
            //String targetDir = Configuration.getValue(Configuration.VOICE_RECORD_DIR);
            String targetDir = voiceRecordDir.getAbsolutePath();
            String tmpFile = UUID.randomUUID().toString() + UUIDGenerator.generateUUID();
            String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
            //create a new Map<String,String> to store all parameter
            Map<String, String> storePara = new HashMap<String, String>();
            FileItemIterator iter = upload.getItemIterator(request);
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
            String word = storePara.get(PARA_WORD);
            String idWord = storePara.get(PARA_WORD_ID);
            String idQuestion = storePara.get(PARA_QUESTION_ID);
            String idCountry = storePara.get(PARA_COUNTRY_ID);

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
                FileUtils.moveFile(tmpFileIn, targetRaw);
                try {
                    if (tmpFileIn.exists())
                        FileUtils.forceDelete(tmpFileIn);
                } catch (Exception e) {}
                awsHelper.uploadInThread(Constant.FOLDER_RECORDED_VOICES + "/" + user.getUsername() + "/" + fileTempName,
                        targetRaw);
                UserLessonHistory model = new UserLessonHistory();
                model.setId(UUIDGenerator.generateUUID());
                model.setUsername(user.getUsername());
                model.setWord(word);
                model.setServerTime(System.currentTimeMillis());
                model.setIdWord(idWord);
                model.setIdQuestion(idQuestion);
                model.setIdCountry(idCountry);
                model.setRecordedFile(fileTempName);
                SphinxResult result = null;
                PhonemesDetector detector = new PhonemesDetector(targetRaw, model.getWord());
                try {
                    result = detector.analyze();
                } catch (Exception ex) {
                    logger.error("Could not analyze word", ex);
                }
                if(result!=null){
                    model.setResult(result);
                    ScoreService service = new ScoreService();
                    service.reCalculateBaseOnWeight(model);
                }
                String output = gson.toJson(model);
                out.print(output);
            }

        }catch (Exception e){
            logger.error("Error when upload file. Common exception, message: " + e.getMessage(),e);
            out.print("Error when upload file. Message: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
