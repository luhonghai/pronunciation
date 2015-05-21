package com.cmg.vrc.servlet;


import org.apache.log4j.Logger;
import com.cmg.vrc.data.dao.impl.FeedbackDAO;
import com.cmg.vrc.data.jdo.Feedback;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;

/**
 * Created by CMGT400 on 5/11/2015.
 */
public class Feedbacks extends HttpServlet {
    class feed{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;
        List<Feedback> data;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        Feedback feedback=new Feedback();

        if(request.getParameter("list")!=null) {
            Feedbacks.feed abc=new feed();
            String s=request.getParameter("start");
            String l=request.getParameter("length");
            String d=request.getParameter("draw");
            int start=Integer.parseInt(s);
            int length=Integer.parseInt(l);
            int draw=Integer.parseInt(d);
            try {
                Double count=feedbackDAO.getCount();
                abc.recordsTotal=count;
                abc.recordsFiltered=count;
                abc.draw=draw;
                abc.data=feedbackDAO.listAll(start,length);
                Gson gson = new Gson();
                String feed = gson.toJson(abc);
                response.getWriter().write(feed);
            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("detail")!=null){
            String id=request.getParameter("id");
            try {
                Feedback byId=feedbackDAO.getById(id);
                Gson gson = new Gson();
                String feedbac = gson.toJson(byId);
                response.getWriter().write(feedbac);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(request.getParameter("filter")!=null){
            String account=request.getParameter("account");
            String imei=request.getParameter("imei");
            String appVersion=request.getParameter("appVersion");
            String osVersion=request.getParameter("osVersion");
            String date=request.getParameter("date");

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
