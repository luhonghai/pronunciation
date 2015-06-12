package com.cmg.vrc.servlet;


import com.cmg.vrc.util.AWSHelper;
import org.apache.log4j.Logger;
import com.cmg.vrc.data.dao.impl.FeedbackDAO;
import com.cmg.vrc.data.jdo.Feedback;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        if(request.getParameter("list")!=null) {
            Feedbacks.feed abc=new feed();
            String s=request.getParameter("start");
            String l=request.getParameter("length");
            String d=request.getParameter("draw");
            String search = request.getParameter("search[value]");
            String column = request.getParameter("order[0][column]");
            String oder = request.getParameter("order[0][dir]");
            int start=Integer.parseInt(s);
            int length=Integer.parseInt(l);
            int col = Integer.parseInt(column);
            int draw=Integer.parseInt(d);
            String ac = request.getParameter("account");
            String app = request.getParameter("appVersion");
            String os = request.getParameter("osVersion");
            String imei = request.getParameter("imei");
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            Date dateFrom1=null;
            Date dateTo1=null;
            Double count;
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
                if (search.length()>0||ac.length()>0||app.length()>0||os.length()>0||imei.length()>0||dateFrom1!=null||dateTo1!=null){
                    count=feedbackDAO.getCountSearch(search,ac,app,os,imei,dateFrom1,dateTo1);
                }else {
                    count = feedbackDAO.getCount();
                }
                abc.recordsTotal=count;
                abc.recordsFiltered=count;
                abc.draw=draw;
                abc.data=feedbackDAO.listAll(start,length,search,col,oder,ac,app,os,imei,dateFrom1,dateTo1);
//                if (abc.data != null && abc.data.size() > 0) {
//                    AWSHelper awsHelper = new AWSHelper();
//                    for (final Feedback f : abc.data) {
//                        f.setScreenshoot(awsHelper.generateFeedbackImageUrl(f.getAccount(), f.getScreenshoot()));
//                    }
//                }
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
                if (byId != null) {
                    AWSHelper awsHelper = new AWSHelper();
                    byId.setScreenshoot(awsHelper.generateFeedbackImageUrl(byId.getAccount(), byId.getScreenshoot()));
                }
                Gson gson = new Gson();
                String feedbac = gson.toJson(byId);
                response.getWriter().write(feedbac);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
