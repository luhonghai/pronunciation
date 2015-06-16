package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.UsageDAO;

import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.jdo.Usage;
import com.cmg.vrc.data.jdo.UserDevice;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by CMGT400 on 6/1/2015.
 */
public class UserManage extends HttpServlet {
    class user{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<Usage> data;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsageDAO usageDAO=new UsageDAO();
        Usage usage=new Usage();
        UserDeviceDAO userDeviceDAO=new UserDeviceDAO();
        UserDevice userDevice=new UserDevice();


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
            Double count;

            String username=request.getParameter("user");
            try {
                UserManage.user user=new user();
                if(search.length()>0||username.length()>0){
                     count=usageDAO.getCountSearch(search,username);
                }else {
                     count = usageDAO.getCount();
                }
                user.draw=draw;
                user.recordsTotal=count;
                user.recordsFiltered=count;
                user.data=usageDAO.listAll(start,length,search,col,oder,username);

                Gson gson = new Gson();
                String user1 = gson.toJson(user);
                response.getWriter().write(user1);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("detail")!=null){
            String emei=request.getParameter("emei");
            try {
                userDevice=userDeviceDAO.getDeviceByIMEI(emei);
                Gson gson = new Gson();
                String user1 = gson.toJson(userDevice);
                response.getWriter().write(user1);

            }catch (Exception e){
                e.printStackTrace();
            }


        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
