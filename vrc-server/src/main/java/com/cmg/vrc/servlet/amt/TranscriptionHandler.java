package com.cmg.vrc.servlet.amt;

import com.cmg.vrc.data.dao.impl.amt.TranscriptionDAO;
import com.cmg.vrc.data.jdo.amt.Transcription;
import com.cmg.vrc.servlet.BaseServlet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private class ResponseData {
        String message;
        boolean status;
        Transcription item;
        List<Transcription> transcriptions;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String data = req.getParameter("data");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ResponseData responseData = new ResponseData();
        responseData.status = false;
        try {
            TranscriptionDAO transcriptionDAO = new TranscriptionDAO();
            if (StringUtils.isEmpty(action)) {
                responseData.message = "No action found";
            } else if (action.equalsIgnoreCase("list")) {
                responseData.status = true;
                responseData.message = "success";
                responseData.transcriptions = transcriptionDAO.listAll();
            } else if (action.equalsIgnoreCase("delete")) {
                if (StringUtils.isEmpty(data)) {
                    responseData.message = "No transcription id found";
                } else {
                    if (transcriptionDAO.delete(data)) {
                        responseData.status = true;
                        responseData.message = "success";
                    } else {
                        responseData.message = "Could not delete transcription ID " + data;
                    }
                }
            } else if (action.equalsIgnoreCase("save")) {
                Transcription transcription = gson.fromJson(data, Transcription.class);
                if (transcriptionDAO.put(transcription)) {
                    responseData.status = true;
                    responseData.message = "success";
                    responseData.item = transcription;
                } else {
                    responseData.message = "Could not save transcription";
                }
            } else {
                responseData.message = "Invalid action";
            }
        } catch (Exception e) {
            responseData.message = "Error: " + e.getMessage();
        }
        printMessage(resp, gson.toJson(responseData));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doPost(req, resp);
    }
}
