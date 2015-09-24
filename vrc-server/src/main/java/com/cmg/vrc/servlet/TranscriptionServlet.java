package com.cmg.vrc.servlet;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.RecorderDAO;

import com.cmg.vrc.data.dao.impl.TranscriptionDAO;

import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.service.TranscriptionActionService;

import com.google.gson.Gson;
import org.apache.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 8/5/2015.
 */
public class TranscriptionServlet extends HttpServlet {
    class admin{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<Transcription> data;
    }


    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TranscriptionDAO adminDAO = new TranscriptionDAO();
        TranscriptionActionService trService = new TranscriptionActionService();
        Transcription ad = new Transcription();
        RecorderDAO recorderDAO=new RecorderDAO();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        if (request.getParameter("list") != null) {
            TranscriptionServlet.admin admin = new admin();
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
            String sentence = request.getParameter("sentence");
            String createDateFrom = request.getParameter("CreateDateFrom");
            String createDateTo = request.getParameter("CreateDateTo");

            String modifiedDateFrom = request.getParameter("ModifiedDateFrom");
            String modifiedDateTo = request.getParameter("ModifiedDateTo");

            Date createDateFrom1=null;
            Date createDateTo1=null;
            Date modifiedDateFrom1=null;
            Date modifiedDateTo1=null;


            if(createDateFrom.length()>0){
                try {
                    createDateFrom1=df.parse(createDateFrom);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            if(createDateTo.length()>0){
                try {
                    createDateTo1=df.parse(createDateTo);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }

            if(modifiedDateFrom.length()>0){
                try {
                    modifiedDateFrom1=df.parse(modifiedDateFrom);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            if(modifiedDateTo.length()>0){
                try {
                    modifiedDateTo1=df.parse(modifiedDateTo);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }


            Double count;
            try {
                if(search.length()>0||sentence.length()>0||createDateFrom1!=null||createDateTo1!=null ||modifiedDateFrom1!=null||modifiedDateTo1!=null ){
                    count=adminDAO.getCountSearch(search,sentence,createDateFrom1,createDateTo1,modifiedDateFrom1,modifiedDateTo1);
                }else {
                    count = adminDAO.getCount();
                }
                admin.draw = draw;
                admin.recordsTotal = count;
                admin.recordsFiltered = count;
                admin.data = adminDAO.listAll(start, length, search, col, oder,sentence,createDateFrom1,createDateTo1,modifiedDateFrom1,modifiedDateTo1);
                Gson gson = new Gson();
                String admins = gson.toJson(admin);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("add")!=null){
            String sentence=request.getParameter("sentence");
            String author=request.getSession().getAttribute("username").toString();
            ad.setAuthor(author);
            ad.setSentence(sentence);
            ad.setCreatedDate(date);
            ad.setModifiedDate(date);
            ad.setStatus(0);
            ad.setIsDeleted(Constant.ISDELETE_FALSE);
            try {
                trService.add(ad);
               // adminDAO.put(ad);
                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }
        if(request.getParameter("edit")!=null){
            String id=request.getParameter("id");
            String sentence=request.getParameter("sentence");
            String author=request.getSession().getAttribute("username").toString();
            try{

                Transcription transcription=trService.getById(id);
                transcription.setSentence(sentence);
                transcription.setModifiedDate(date);
                transcription.setAuthor(author);
                transcription.setIsDeleted(Constant.ISDELETE_FALSE);
                trService.edit(transcription);
                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }


        }

        if(request.getParameter("delete")!=null){
            String id= request.getParameter("id");
            try {
                Transcription transcription=trService.getById(id);
                transcription.setIsDeleted(Constant.ISDELETE_TRUE);
                trService.delete(transcription);
                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
