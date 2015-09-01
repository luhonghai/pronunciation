package com.cmg.vrc.servlet;


import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.data.jdo.RecorderClient;
import com.cmg.vrc.service.RecorderSentenceService;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 8/5/2015.
 */
public class RecorderServlet extends BaseServlet {
    class admin{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<RecorderClient> data;
    }

    private class ResponseDataRecorded extends com.cmg.vrc.servlet.ResponseData<RecordedSentence> {
        List<RecordedSentence> RecordedSentences;
    }
    private static String LIST_BY_ADMIN = "listbyadmin";
    private static String LIST_BY_CLIENT = "listbyclient";
    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RecorderDAO recorderDAO = new RecorderDAO();
        RecordedSentence ad = new RecordedSentence();
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Date date = new Date();
        String action = request.getParameter("action");
        if(action.equalsIgnoreCase(LIST_BY_CLIENT)){
            String username = request.getParameter("data");
            int version = Integer.parseInt(request.getParameter("version"));
            RecorderSentenceService rsService = new RecorderSentenceService();
            Gson gson = new Gson();
            ResponseDataRecorded responseData = new ResponseDataRecorded();
            responseData.setStatus(true);
            responseData.setMessage("success");
            System.out.println("user name " + username);
            System.out.println("version  : " + version);
            responseData.RecordedSentences = rsService.getListByVersionAndUsername(version, username);
            System.out.println("list size " + responseData.RecordedSentences.size());
            printMessage(response, gson.toJson(responseData));
        }
        else if (action.equalsIgnoreCase(LIST_BY_ADMIN)) {
            RecorderServlet.admin admin = new admin();
            String s = request.getParameter("start");
            String l = request.getParameter("length");
            String d = request.getParameter("draw");
            String search = request.getParameter("search[value]");
            String column = request.getParameter("order[0][column]");
            String oder = request.getParameter("order[0][dir]");
            int start = Integer.parseInt(s);
            int length = Integer.parseInt(l);
            int col = Integer.parseInt(column);
            int draw = Integer.parseInt(d);
            String account = request.getParameter("account");
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            String status = request.getParameter("status");
            String accounts = request.getParameter("accounts");

            int sta=0;
            if(status.equalsIgnoreCase("All")){
                sta=6;
            }else {
                sta=Integer.parseInt(status);
            }

            Date dateFrom1=null;
            Date dateTo1=null;



            if(dateFrom.length()>0){
                try {
                    dateFrom1=df.parse(dateFrom);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            if(dateTo.length()>0){
                try {
                    dateTo1=df.parse(dateTo);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }


            Double count;
            try {
                if(search.length()>0|| account.length()>0 ||dateFrom1!=null||dateTo1!=null || accounts.length()>0){
                    count=recorderDAO.getCountSearch(search,account,dateFrom1,dateTo1,sta,accounts);
                }else {
                    count = recorderDAO.getCount();
                }
                List<RecorderClient> recorderClients=new ArrayList<RecorderClient>();
                List<RecordedSentence> recordedSentences = recorderDAO.listAll(start, length, search, col, oder,account,dateFrom1,dateTo1,sta,accounts);
                for(int i=0;i<recordedSentences.size();i++){
                    RecorderClient recorderClient=new RecorderClient();
                    recorderClient.setId(recordedSentences.get(i).getId());
                    recorderClient.setIsDeleted(recordedSentences.get(i).isDeleted());
                    recorderClient.setAccount(recordedSentences.get(i).getAccount());
                    recorderClient.setStatus(recordedSentences.get(i).getStatus());
                    recorderClient.setVersion(recordedSentences.get(i).getVersion());
                    recorderClient.setAdmin(recordedSentences.get(i).getAdmin());
                    recorderClient.setCreatedDate(recordedSentences.get(i).getCreatedDate());
                    recorderClient.setModifiedDate(recordedSentences.get(i).getModifiedDate());
                    recorderClient.setFileName(recordedSentences.get(i).getFileName());
                    recorderClient.setSentenceId(recordedSentences.get(i).getSentenceId());
                    String idSentence=recordedSentences.get(i).getSentenceId();
                    String sentence=transcriptionDAO.getById(idSentence).getSentence();
                    recorderClient.setSentence(sentence);
                    recorderClients.add(recorderClient);
                }

                admin.draw = draw;
                admin.recordsTotal = count;
                admin.recordsFiltered = count;
                admin.data=recorderClients;
                Gson gson = new Gson();
                String admins = gson.toJson(admin);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
