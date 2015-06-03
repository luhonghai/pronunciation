package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
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
 * Created by CMGT400 on 6/2/2015.
 */
public class UserDevice extends HttpServlet {
    class user{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<com.cmg.vrc.data.jdo.UserDevice> data;


    }
    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            int start = Integer.parseInt(s);
            int length = Integer.parseInt(l);
            int col = Integer.parseInt(column);
            int draw = Integer.parseInt(d);
            String emei=request.getParameter("emei");
            String model = request.getParameter("model");
            String osVersion = request.getParameter("osVersion");
            String osApiLevel = request.getParameter("osApiLevel");
            String deviceName = request.getParameter("deviceName");
            String emeisearch = request.getParameter("emeisearch");
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
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
                UserDevice.user user=new user();
                Double count = userDeviceDAO.getCount();
                user.draw=draw;
                user.recordsTotal=count;
                user.recordsFiltered=count;
                user.data=userDeviceDAO.listAll(start,length,search,col,oder,emei,model,osVersion,osApiLevel,deviceName,emeisearch,dateFrom1,dateTo1);
                Gson gson = new Gson();
                String user1 = gson.toJson(user);
                response.getWriter().write(user1);

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
