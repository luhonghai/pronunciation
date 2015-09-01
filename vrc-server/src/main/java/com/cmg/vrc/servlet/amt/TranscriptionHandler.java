package com.cmg.vrc.servlet.amt;

import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.service.TranscriptionActionService;
import com.cmg.vrc.service.amt.TranscriptionService;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.servlet.ResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by cmg on 03/07/15.
 */
public class TranscriptionHandler extends BaseServlet {

    private class ResponseData extends com.cmg.vrc.servlet.ResponseData<Transcription> {
        List<Transcription> transcriptions;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String data = req.getParameter("data");
        String versionClient=req.getParameter("version");
        boolean isRawHtml = false;
        logger.info("Client version: " + versionClient);
        int version= 0;
        try {
            version = Integer.parseInt(versionClient);
        } catch (Exception e) {

        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ResponseData responseData = new ResponseData();
        responseData.setStatus(false);
        try {
            TranscriptionDAO transcriptionDAO = new TranscriptionDAO();
            //TranscriptionService transcriptionService = new TranscriptionService();
            TranscriptionActionService transcriptionActionService = new TranscriptionActionService();
            if (StringUtils.isEmpty(action)) {
                responseData.setMessage("No action found");
            } else if (action.equalsIgnoreCase("list")) {
                responseData.setStatus(true);
                responseData.setMessage("success");
                long start = System.currentTimeMillis();
                responseData.transcriptions = transcriptionActionService.listTranscription(version);
                logger.info("Execution time: " + (System.currentTimeMillis() - start));
                logger.info("Sentence size: " + (responseData.transcriptions == null ? 0 : responseData.transcriptions.size()));
            } else if (action.equalsIgnoreCase("delete")) {
                if (StringUtils.isEmpty(data)) {
                    responseData.setMessage("No transcription id found");
                } else {
                    if (transcriptionDAO.delete(data)) {
                        responseData.setStatus(true);
                        responseData.setMessage("success");
                    } else {
                        responseData.setMessage("Could not delete transcription ID " + data);
                    }
                }
            } else if (action.equalsIgnoreCase("save")) {
                Transcription transcription = gson.fromJson(data, Transcription.class);
                if (transcriptionDAO.put(transcription)) {
                    responseData.setStatus(true);
                    responseData.setMessage("success");
                    responseData.setData(transcription);
                } else {
                    responseData.setMessage("Could not save transcription");
                }
            } else if (action.equalsIgnoreCase("load")) {
                TranscriptionService transcriptionService = new TranscriptionService();
                transcriptionService.setUseJDO(true);
                transcriptionService.loadTranscription();
                responseData.setStatus(true);
                responseData.setMessage(FileUtils.readFileToString(transcriptionService.getResultHtmlFile(), "UTF-8"));
                isRawHtml = true;
            } else {
                responseData.setMessage("Invalid action");
            }
        } catch (Exception e) {
            responseData.setMessage("Error: " + e.getMessage());
        }
        if (isRawHtml) {
            printMessage(resp, responseData.getMessage());
        } else {
            printMessage(resp, gson.toJson(responseData));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doPost(req, resp);
    }
}
