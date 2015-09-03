package com.cmg.vrc.servlet;

import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.service.PhonemeScoreService;
import com.cmg.vrc.service.UserVoiceModelService;
import com.cmg.vrc.sphinx.SphinxResult;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lantb on 2015-09-03.
 */
public class DataSynchronizationServlet extends BaseServlet {

    private static final Logger logger = Logger.getLogger(DataSynchronizationServlet.class
            .getName());
    private static String PARA_USERNAME = "username";
    private static String PARA_VERSION = "version";
    private static String PARA_ACTION = "action";
    private static String LIST_USER_VOICE_MODEL = "uservoicemodel";
    private static String LIST_PHONEME_SCORE = "phonemescore";

    private class ResponseDataUserVoice extends com.cmg.vrc.servlet.ResponseData<UserVoiceModel> {
        List<UserVoiceModel> userVoiceModelList;
    }

    private class ResponseDataPhoneme extends com.cmg.vrc.servlet.ResponseData<PhonemeScoreDB> {
        List<SphinxResult.PhonemeScore> phonemeScoreDBList;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Map<String, String> storePara = new HashMap<String, String>();
            Gson gson = new Gson();
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = null;
            iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    logger.info(name);
                    String value = Streams.asString(stream);
                    storePara.put(name, value);
                }
            }
            String action = storePara.get(PARA_ACTION);
            logger.info("action sync : " + action);
            if(action.equalsIgnoreCase(LIST_USER_VOICE_MODEL)){
                String username =  storePara.get(PARA_USERNAME);
                int version = Integer.parseInt(storePara.get(PARA_VERSION));
                logger.info("username : " +username);
                logger.info("version : " +version);
                ResponseDataUserVoice responseData = new ResponseDataUserVoice();
                responseData.setMessage("success");
                UserVoiceModelService serviceVoice = new UserVoiceModelService();
                responseData.userVoiceModelList = serviceVoice.getListByUsernameAndVersion(username,version);
                printMessage(response,gson.toJson(responseData));
            }else if(action.equalsIgnoreCase(LIST_PHONEME_SCORE)){
                String username =  storePara.get(PARA_USERNAME);
                int version = Integer.parseInt( storePara.get(PARA_VERSION));
                logger.info("username : " +username);
                logger.info("version : " +version);
                ResponseDataPhoneme responseDataPhoneme = new ResponseDataPhoneme();
                responseDataPhoneme.setMessage("success");
                PhonemeScoreService phonemeScoreService = new PhonemeScoreService();
                responseDataPhoneme.phonemeScoreDBList = phonemeScoreService.swap(phonemeScoreService.listByUsernameAndVersion(username,version));
                printMessage(response,gson.toJson(responseDataPhoneme));
            }
        }catch (Exception e){
            logger.error("error in data synchronization " + e);
            response.getWriter().print("error");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
