package com.cmg.vrc.servlet;

import com.cmg.lesson.dao.word.WordCollectionDAO;
import com.cmg.lesson.dao.word.WordMappingPhonemesDAO;
import com.cmg.lesson.data.jdo.history.PhonemeLessonScore;
import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.data.jdo.word.WordMappingPhonemes;
import com.cmg.lesson.services.calculation.ScoreService;
import com.cmg.lesson.servlet.CalculationServlet;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.jdo.TestPage1;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by CMGT400 on 11/19/2015.
 */
@WebServlet(name = "TestPage")
public class TestPage extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CalculationServlet.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        WordCollectionDAO wordCollectionDAO=new WordCollectionDAO();
        WordMappingPhonemesDAO wordMappingPhonemesDAO=new WordMappingPhonemesDAO();
        Gson gson = new Gson();
        AWSHelper awsHelper = new AWSHelper();
        ResponseData<SphinxResult> resultResponseData = new ResponseData<>();
        List<PhonemeLessonScore> list = null;

        try {
            String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
            SphinxResult result = null;
            Map<String, String> storePara = getMap(request, tmpDir);

            String action = (String)StringUtil.isNull(storePara.get("test"),"");
            if(action.equalsIgnoreCase("test")) {
                TestPage1 page1=testPage1(storePara);
                WordCollection wordCollection=wordCollectionDAO.getByWord(page1.getWord());

                if(wordCollection!=null){
                    //List<WordMappingPhonemes> wordMappingPhonemes=wordMappingPhonemesDAO.getByWordID(wordCollection.getId());
                    PhonemesDetector detector = new PhonemesDetector(page1.getFileAudio(), page1.getWord());
                    result = detector.analyze();
//                    List<SphinxResult.PhonemeScore> scores = result.getPhonemeScores();
//                    if(scores!=null && scores.size() > 0){
//                        list = new ArrayList<PhonemeLessonScore>();
//                        for(SphinxResult.PhonemeScore ph : scores){
//                            PhonemeLessonScore pls = new PhonemeLessonScore();
//                            pls.setPhoneme(ph.getName());
//                            pls.setIndex(ph.getIndex());
//                            pls.setTotalScore(ph.getTotalScore());
//                            list.add(pls);
//                        }
//                    }
                    resultResponseData.setData(result);
                    resultResponseData.setMessage("success");
                    resultResponseData.setStatus(true);
                    String resultResponse = gson.toJson(resultResponseData);
                    response.getWriter().write(resultResponse);

                }else {
                    response.getWriter().write("notExist");
                }

            }
        }catch (Exception e){
            logger.error("Error", e);
            response.getWriter().write("error");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    private Map<String, String> getMap(HttpServletRequest request,String targetDir){
        Map<String, String> storePara = new HashMap<String, String>();
        ServletFileUpload upload = new ServletFileUpload();
        try {
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(stream, "UTF-8");
                    logger.info(name + "-" + value);
                    storePara.put(name, value);
                }else{
                    String getName = item.getName();
                    logger.info("file name : " + getName);
                    if(getName.endsWith(".mp3") || getName.endsWith(".wav")){
                        String filename = UUID.randomUUID().toString() + UUIDGenerator.generateUUID()+ ".wav";
                        FileUtils.copyInputStreamToFile(stream, new File(targetDir, filename));
                        logger.info(name + "-" + new File(targetDir, filename).getAbsolutePath());
                        storePara.put(name,new File(targetDir, filename).getAbsolutePath());
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storePara;
    }
    private TestPage1 testPage1(Map<String, String> map){
        TestPage1 testPage1=new TestPage1();
        testPage1.setWord(map.get("word").trim());
        testPage1.setFileAudio(new File(map.get("audio")));
        return testPage1;
    }

}
