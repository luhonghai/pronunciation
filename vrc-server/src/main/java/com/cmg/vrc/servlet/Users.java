package com.cmg.vrc.servlet;


import com.cmg.vrc.data.dao.impl.UserDAO;

import com.cmg.vrc.data.jdo.User;
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

public class Users extends HttpServlet {
    class user{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<User> data;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        User user = new User();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        if (request.getParameter("list") != null) {
            Users.user use = new user();

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
            String fullname = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            String country = request.getParameter("country");
            String Acti = request.getParameter("Acti");
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            Date dateFrom1 = null;
            Date dateTo1 = null;
            Double count;
            if (dateFrom.length() > 0) {
                try {
                    dateFrom1 = df.parse(dateFrom);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            if (dateTo.length() > 0) {
                try {
                    dateTo1 = df.parse(dateTo);
                } catch (Exception e) {
                    e.getStackTrace();
                }

            }
            try {
                if(search.length()>0||username.length()>0||fullname.length()>0||gender.length()>0||country.length()>0||Acti.length()>0||dateFrom1!=null||dateTo1!=null){
                    count=userDAO.getCountSearch(search,username,fullname,gender,country,Acti,dateFrom1,dateTo1);
                }else {
                    count = userDAO.getCount();
                }
                use.draw = draw;
                use.recordsTotal = count;
                use.recordsFiltered = count;
                use.data = userDAO.listAll(start, length, search, col, oder,username,fullname,gender,country,Acti,dateFrom1,dateTo1);

//                List<com.cmg.vrc.data.jdo.LicenseCode> list=lis.listAll(start,length);
                Gson gson = new Gson();
                String users = gson.toJson(use);
                response.getWriter().write(users);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }

        }

        if(request.getParameter("activated")!=null){
            String acti=request.getParameter("acti");
            String id=request.getParameter("id");
            boolean activated = Boolean.parseBoolean(acti);
            user.setId(id);
            user.setActivated(activated);
            try {
                User licen = userDAO.getById(id);
                licen.setActivated(activated);
                userDAO.put(licen);
                response.getWriter().write("success");
            }catch (Exception e){
                e.printStackTrace();
            }



        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
