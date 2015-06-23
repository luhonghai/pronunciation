package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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

        if(request.getParameter("draw")!=null) {
            Pronunciationss.score score = new score();

            String search = request.getParameter("search");
            Double count;
            String username = request.getParameter("username");
            String word = request.getParameter("word");
            String uuid = request.getParameter("uuid");
            try {

                if (search.length() > 0 || username.length() > 0 || word.length() > 0 || uuid.length() > 0) {
                    count = userVoiceModelDAO.getCountSearch(search, username, word, uuid);
                } else {
                    count = userVoiceModelDAO.getCount();
                }

                if(count<10000) {
                    List<UserVoiceModel> userVoiceModels = userVoiceModelDAO.listAllScore(search, username, word, uuid);
                    List<List<Object>> list = new ArrayList<>();
                    for (int i = 0; i < userVoiceModels.size(); i++) {
                        List<Object> item = new ArrayList<>();
                        item.add(userVoiceModels.get(i).getServerTime());
                        item.add(userVoiceModels.get(i).getScore());
                        list.add(item);
                    }
                    score.mess="success";
                    score.status=true;
                    score.sc = list;
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

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}


