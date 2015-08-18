package com.cmg.vrc.service;

import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.data.jdo.Transcription;

import java.util.List;

/**
 * Created by CMGT400 on 8/13/2015.
 */
public class TranscriptionActionService {
    private String RETURN_SUCCESS = "success";
    private String RETURN_ERROR="error";

    public int version(){
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();
        int version=transcriptionDAO.getLatestVersion()+1;
        return version;
    }


    public Transcription getById(String id){
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();
        try {
            Transcription temp = transcriptionDAO.getById(id);
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String add(Transcription transcription){
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();
        String result = RETURN_ERROR;
        transcription.setVersion(version());
       try{
           transcriptionDAO.put(transcription);
           result = RETURN_SUCCESS;
       }catch (Exception e){
           e.printStackTrace();
       }
        return result;
    }
    public String edit(Transcription transcription){
        String result = RETURN_ERROR;
        String condition = add(transcription);
        if(condition.equalsIgnoreCase(RETURN_SUCCESS)){
            RecorderSentenceService rsServices = new RecorderSentenceService();
            condition = rsServices.adminUpdate(transcription.getId(),false);
            if(condition.equalsIgnoreCase(RETURN_SUCCESS)){
                condition = RETURN_SUCCESS;
            }
        }
        return result;
    }
    public String delete(Transcription transcription){
        String result = RETURN_ERROR;
        String condition = add(transcription);
        if(condition.equalsIgnoreCase(RETURN_SUCCESS)){
            RecorderSentenceService rsServices = new RecorderSentenceService();
            condition = rsServices.adminUpdate(transcription.getId(),true);
            if(condition.equalsIgnoreCase(RETURN_SUCCESS)){
                condition = RETURN_SUCCESS;
            }
        }
        return result;
    }
    public List<Transcription> listTranscription(int version){
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();
        List<Transcription> list=null;
        try {
            list=transcriptionDAO.getListByVersion(version);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}
