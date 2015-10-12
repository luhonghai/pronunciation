package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.dao.impl.RecorderSentenceDAO;
import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by CMGT400 on 8/6/2015.
 */
public class RecorderNumberServlet extends HttpServlet {
    class number{
        public double numberAccount,pending,reject,approved,locke,allsentence;
        public List<RecordedSentence> recordedSentences;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

           if(request.getParameter("loadNumber")!=null){
               RecorderNumberServlet.number number=new number();
               try{
                   number=getNumber();
                   Gson gson = new Gson();
                   String numbers = gson.toJson(number);
                   response.getWriter().write(numbers);


               }catch (Exception e){
                   e.printStackTrace();
               }


           }


           if(request.getParameter("loadNumberAccount")!=null){
               RecorderNumberServlet.number number=new number();
               String acount=request.getParameter("account");
               try{
                   if(acount.length()>0){
                      number = getNumberWithAccountandStatus(acount);
                   }else {
                      number = getNumber();
                   }

                   Gson gson = new Gson();
                   String numbers = gson.toJson(number);
                   response.getWriter().write(numbers);

               }catch (Exception e){
                   e.printStackTrace();
               }


           }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    public List<RecordedSentence> recordedSentenceList(){
        List<RecordedSentence> list=null;
        RecorderDAO recorderDAO=new RecorderDAO();
        try {
            List<RecordedSentence> recordedSentences = recorderDAO.list();
            for (int i = 0; i < recordedSentences.size(); i++) {
                for (int j = i + 1; j < recordedSentences.size(); j++) {
                    if (recordedSentences.get(i).getAccount().equals(recordedSentences.get(j).getAccount())){
                        recordedSentences.remove(j);
                        j=j-1;
                    }
                    else{
                        break;
                    }
                }
            }
            list=recordedSentences;
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public int numberAccount(){
        int number=0;
        number=recordedSentenceList().size();
        return number;
    }
    public number getNumber(){
        RecorderSentenceDAO recorderSentenceDAO=new RecorderSentenceDAO();
        RecorderDAO recorderDAO=new RecorderDAO();
        RecorderNumberServlet.number number=new number();
        try {
            double pending=recorderSentenceDAO.getCount(1);
            double reject=recorderSentenceDAO.getCount(2);
            double approved=recorderSentenceDAO.getCount(3);
            double locked=recorderSentenceDAO.getCount(4);
            double allsentence=recorderDAO.getCount();
            number.numberAccount=numberAccount();
            number.pending=pending;
            number.reject=reject;
            number.approved=approved;
            number.locke=locked;
            number.allsentence=allsentence;
            number.recordedSentences=recordedSentenceList();
            return number;
        }catch (Exception e){
            return null;
        }
    }
    public number getNumberWithAccountandStatus(String acount){
        RecorderSentenceDAO recorderSentenceDAO=new RecorderSentenceDAO();
        RecorderDAO recorderDAO=new RecorderDAO();
        RecorderNumberServlet.number number=new number();
        try{
            double pending=recorderSentenceDAO.getCount(acount,1);
            double reject=recorderSentenceDAO.getCount(acount,2);
            double approved=recorderSentenceDAO.getCount(acount,3);
            double locked=recorderSentenceDAO.getCount(acount,4);
            double allsentence=recorderDAO.getCount();
            number.numberAccount=numberAccount();
            number.pending=pending;
            number.reject=reject;
            number.approved=approved;
            number.locke=locked;
            number.allsentence=allsentence;
            number.recordedSentences=null;
            return number;

        }catch (Exception e){
            return null;
        }

    }

//    public byte[] audio(String WAV_FILE){
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            BufferedInputStream in = new BufferedInputStream(new FileInputStream(WAV_FILE));
//
//            int read;
//            byte[] buff = new byte[1024];
//            while ((read = in.read(buff)) > 0) {
//                out.write(buff, 0, read);
//            }
//            out.flush();
//            byte[] audioBytes = out.toByteArray();
//            return audioBytes;
//        }catch (Exception e){
//            return null;
//        }
//    }




}
