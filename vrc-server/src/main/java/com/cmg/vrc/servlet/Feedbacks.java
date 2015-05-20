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
//    class feed{
//        public int iTotalRecords;
//        public int iTotalDisplayRecords;
//        List<Feedback> data;
//    }





    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        if(request.getParameter("list")!=null) {
            try {
//                Feedbacks.feed abc=new feed();
//                abc.iTotalDisplayRecords=3;
//                abc.iTotalRecords=3;
//
//                abc.data = feedbackDAO.listAll();

                List<Feedback> list = feedbackDAO.listAll();

                Gson gson = new Gson();
                String feedbacks = gson.toJson(list);
                response.getWriter().write(feedbacks);

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
                String feedback = gson.toJson(byId);
                response.getWriter().write(feedback);
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
