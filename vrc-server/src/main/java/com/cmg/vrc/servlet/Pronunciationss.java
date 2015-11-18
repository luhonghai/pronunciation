package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 6/23/2015.
 */
public class Pronunciationss extends HttpServlet{


    class score{
        String mess;
        Boolean status;
        List<List<Object>> sc;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserVoiceModelDAO userVoiceModelDAO=new UserVoiceModelDAO();
        UserVoiceModel userVoiceModel=new UserVoiceModel();
        UserDeviceDAO userDeviceDAO=new UserDeviceDAO();
        UserDevice userDevice=new UserDevice();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        if(request.getParameter("draw")!=null) {
            Pronunciationss.score score = new score();
            String search = (String) StringUtil.isNull(request.getParameter("search"), "");
            Double count=0d;
            String username =(String) StringUtil.isNull(request.getParameter("username"), "");
            String word =(String) StringUtil.isNull(request.getParameter("word"), "");
            int scores =Integer.parseInt(StringUtil.isNull(request.getParameter("score"), "").toString());
            String type =(String)StringUtil.isNull(request.getParameter("type"), "");
            int counts =Integer.parseInt(StringUtil.isNull(request.getParameter("count"), "").toString());
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

//                if(search.length()>0||username.length()>0||word.length()>0||dateFrom1!=null||dateTo1!=null){
//                    List<Score> scores1=userVoiceModelDAO.listAllScore(search, username, word, scores, type, dateFrom1, dateTo1);
//                    count=(double)scores1.size();
//                }else {
//                    count = userVoiceModelDAO.getCount();
//                }
                List<Score> scoress=userVoiceModelDAO.listAllScore(search,username,word,scores,counts,type,dateFrom1,dateTo1);
                if(scoress!=null) {
                    count = (double) scoress.size();
                }

                if(count<10000) {
                    List<Score> scores1 = userVoiceModelDAO.listAllScore(search,username,word,scores,counts,type,dateFrom1,dateTo1);
                    List<List<Object>> list = new ArrayList<List<Object>>();
                    if(scores1!=null) {
                        for (int i = 0; i < scores1.size(); i++) {
                            List<Object> item = new ArrayList<>();
                            item.add(scores1.get(i).getServerTime());
                            item.add(scores1.get(i).getScore());
                            list.add(item);
                        }
                        score.sc = list;
                    }else {
                        score.sc = null;
                    }
                    score.mess="success";
                    score.status=true;

                    Gson gson = new Gson();
                    String sc = gson.toJson(score);
                    response.getWriter().write(sc);
                }
                else {
                    score.mess="error";
                    score.status=false;
                    score.sc = null;
                    Gson gson = new Gson();
                    String sc = gson.toJson(score);
                    response.getWriter().write(sc);
                }

            } catch (Exception e) {
                score.mess="Error Sever";
                Gson gson = new Gson();
                String sc = gson.toJson(score);
                response.getWriter().write(sc);
                e.printStackTrace();
            }
        }

        if(request.getParameter("draws")!=null) {
            Pronunciationss.score score = new score();
            try {
                    List<UserVoiceModel> userVoiceModels = userVoiceModelDAO.listAllScore();
                    List<List<Object>> list = new ArrayList<List<Object>>();
                    if(userVoiceModels!=null) {
                        for (int i = 0; i < userVoiceModels.size(); i++) {
                            List<Object> item = new ArrayList<>();
                            item.add(userVoiceModels.get(i).getServerTime());
                            item.add(userVoiceModels.get(i).getScore());
                            list.add(item);
                        }
                        score.sc = list;
                    }else {
                        score.sc=null;
                    }
                    score.mess="success";
                    score.status=true;

                    Gson gson = new Gson();
                    String sc = gson.toJson(score);
                    response.getWriter().write(sc);


            } catch (Exception e) {
                score.mess="error";
                Gson gson = new Gson();
                String sc = gson.toJson(score);
                response.getWriter().write(sc);
                e.printStackTrace();
            }
        }


    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}


