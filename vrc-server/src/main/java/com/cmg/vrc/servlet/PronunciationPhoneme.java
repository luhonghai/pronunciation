package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelPhonemeDAO;
import com.cmg.vrc.data.jdo.Phoneme;
import com.cmg.vrc.data.jdo.Score;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.util.StringUtil;
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
 * Created by CMGT400 on 6/4/2015.
 */
public class PronunciationPhoneme extends HttpServlet{
    class pronunciation{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<Phoneme> data;

    }


    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserVoiceModelPhonemeDAO userVoiceModelPhonemeDAO=new UserVoiceModelPhonemeDAO();
        UserVoiceModel userVoiceModel=new UserVoiceModel();
        UserDeviceDAO userDeviceDAO=new UserDeviceDAO();
        UserDevice userDevice=new UserDevice();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


        if (request.getParameter("list") != null) {

            String s = request.getParameter("start");
            String l = request.getParameter("length");
            String d = request.getParameter("draw");
            String search = request.getParameter("search[value]");
            String column = request.getParameter("order[0][column]");
            String oder = request.getParameter("order[0][dir]");

            int  start = Integer.parseInt(s);
            int length = Integer.parseInt(l);
            int col= Integer.parseInt(column);
            int  draw= Integer.parseInt(d);
            Double count;
            String username =(String) StringUtil.isNull(request.getParameter("username"), "");
            String phoneme =(String) StringUtil.isNull(request.getParameter("phoneme"), "");
            String country =(String) StringUtil.isNull(request.getParameter("country"), "");
            int scores =Integer.parseInt(StringUtil.isNull(request.getParameter("score"), "").toString());
            String type =(String)StringUtil.isNull(request.getParameter("type"), "");
            String dateFrom =(String) StringUtil.isNull(request.getParameter("dateFrom"), "");
            String dateTo =(String) StringUtil.isNull(request.getParameter("dateTo"), "");
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

            try {
                PronunciationPhoneme.pronunciation pronunciation=new pronunciation();
                List<Phoneme> phonemes=userVoiceModelPhonemeDAO.getCountSearch(search,col,oder,username,phoneme,country,scores,type,dateFrom1,dateTo1);
                count=(double)phonemes.size();
                pronunciation.draw=draw;
                pronunciation.recordsTotal=count;
                pronunciation.recordsFiltered=count;
                pronunciation.data = userVoiceModelPhonemeDAO.listAll(start, length, search, col, oder, username,phoneme,country,scores,type,dateFrom1,dateTo1);
                Gson gson = new Gson();
                String score = gson.toJson(pronunciation);
                response.getWriter().write(score);

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