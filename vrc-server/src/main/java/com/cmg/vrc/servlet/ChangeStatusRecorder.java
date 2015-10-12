package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.data.jdo.Transcription;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CMGT400 on 8/28/2015.
 */
public class ChangeStatusRecorder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RecorderDAO recorderDAO = new RecorderDAO();
        RecordedSentence recordedSentence = new RecordedSentence();
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();
        Transcription transcription=new Transcription();
        int lastedVersion=recorderDAO.getLatestVersion();

        if(request.getParameter("reject")!=null){
            String id = request.getParameter("id");
            String idSentence=request.getParameter("idSentence");
            //String username=request.getSession().getAttribute("username").toString();
            String sentence=request.getParameter("sentence");

            try{
                recordedSentence=recorderDAO.getById(id);
                transcription=transcriptionDAO.getById(idSentence);
                if(transcription.isDeleted()!=1 && transcription.getSentence().equalsIgnoreCase(sentence)){
                    recordedSentence.setStatus(2);
                    recordedSentence.setVersion(lastedVersion + 1);
                    recorderDAO.put(recordedSentence);
                    response.getWriter().write("success");
                }else {
                    response.getWriter().write("change");
                }


            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if(request.getParameter("approved")!=null){
            String id = request.getParameter("id");
            String idSentence=request.getParameter("idSentence");
            //String username=request.getSession().getAttribute("username").toString();
            String sentence=request.getParameter("sentence");
            try{
                recordedSentence=recorderDAO.getById(id);
                transcription=transcriptionDAO.getById(idSentence);
                if(transcription.isDeleted()!=1 && transcription.getSentence().equalsIgnoreCase(sentence)) {
                    recordedSentence.setStatus(3);
                    recordedSentence.setVersion(lastedVersion + 1);
                    recorderDAO.put(recordedSentence);
                    response.getWriter().write("success");
                }else {
                    response.getWriter().write("change");
                }


            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if(request.getParameter("locked")!=null){
            String id = request.getParameter("id");
            String idSentence=request.getParameter("idSentence");
           // String username=request.getSession().getAttribute("username").toString();
            String sentence=request.getParameter("sentence");
            try{
                recordedSentence=recorderDAO.getById(id);
                transcription=transcriptionDAO.getById(idSentence);
                if(transcription.isDeleted()!=1 && transcription.getSentence().equalsIgnoreCase(sentence)) {
                    recordedSentence.setStatus(4);
                    recordedSentence.setVersion(lastedVersion + 1);
                    recorderDAO.put(recordedSentence);
                    response.getWriter().write("success");
                }else {
                    response.getWriter().write("change");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }


}
