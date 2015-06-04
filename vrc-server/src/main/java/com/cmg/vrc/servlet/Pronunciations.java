package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by CMGT400 on 6/4/2015.
 */
public class Pronunciations extends HttpServlet{
    class pronunciation{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<UserVoiceModel> data;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserVoiceModelDAO userVoiceModelDAO=new UserVoiceModelDAO();
        UserVoiceModel userVoiceModel=new UserVoiceModel();

        if (request.getParameter("list") != null) {

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
            String username = request.getParameter("username");
            String word = request.getParameter("word");
            String uuid = request.getParameter("uuid");
            try {
                Pronunciations.pronunciation pronunciation=new pronunciation();
                Double count = userVoiceModelDAO.getCount();
                pronunciation.draw=draw;
                pronunciation.recordsTotal=count;
                pronunciation.recordsFiltered=count;
                pronunciation.data=userVoiceModelDAO.listAll(start,length,search,col,oder,username,word,uuid);

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
