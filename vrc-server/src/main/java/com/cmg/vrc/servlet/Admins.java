package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.AdminDAO;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class Admins extends HttpServlet {
    class admin{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<Admin> data;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO adminDAO = new AdminDAO();
        Admin ad = new Admin();
        String roless=request.getSession().getAttribute("role").toString();
        if (request.getParameter("list") != null) {
            Admins.admin admin = new admin();
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
            String first = request.getParameter("firstname");
            String last = request.getParameter("lastname");
            Double count;

            try {
                if(search.length()>0||username.length()>0||first.length()>0||last.length()>0){
                    count=adminDAO.getCountSearch(search,username,first,last);
                }else {
                    count = adminDAO.getCount();
                }
                admin.draw = draw;
                admin.recordsTotal = count;
                admin.recordsFiltered = count;
                admin.data = adminDAO.listAll(start, length, search, col, oder,username,first,last);
//                for(int i=0;i<admin.data.size();i++){
//                    int a=admin.data.get(i).getRole();
//                    if(a==1){
//
//                    }
//                }

//                List<com.cmg.vrc.data.jdo.LicenseCode> list=lis.listAll(start,length);
                Gson gson = new Gson();
                String admins = gson.toJson(admin);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("add")!=null){
            String username = request.getParameter("username");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            int ro=0;
            if(role.length()>0 && role.equals("Admin")) {
                ro=1;
            }
            if(role.length()>0 && role.equals("User")) {
                ro=2;
            }
            try{
                Admin a=adminDAO.getUserByEmail(username);
            if(a!=null) {
                response.getWriter().write("error");
            } else {
                ad.setUserName(username);
                ad.setPassword(StringUtil.md5(password));
                ad.setFirstName(firstname);
                ad.setLastName(lastname);
                ad.setRole(ro);
                adminDAO.put(ad);
                response.getWriter().write("success");

            }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if(request.getParameter("edit")!=null){
            String idd=request.getSession().getAttribute("id").toString();
            String id=request.getParameter("id");
            String username = request.getParameter("username");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            int ro=0;
            if(role.length()>0 && role.equals("Admin")) {
                ro=1;
            }
            if(role.length()>0 && role.equals("User")) {
                ro=2;
            }
            try{
                Admin a=adminDAO.getUserByEmail(username);
                if(a==null) {
                    Admin admi = adminDAO.getById(id);
                    if (username.length()>0) {
                        admi.setUserName(username);
                    }
                    if (password.length()>0) {
                        admi.setPassword(StringUtil.md5(password));
                    }
                    if (firstname.length()>0) {
                        admi.setFirstName(firstname);
                    }
                    if (lastname.length()>0) {
                        admi.setLastName(lastname);
                    }
                    admi.setRole(ro);
                    adminDAO.put(admi);
                    response.getWriter().write("success");
                }
                else if(a!=null && id.equals(idd)) {
                    Admin admi = adminDAO.getById(id);
                    if (username.length()>0) {
                        admi.setUserName(username);
                    }
                    if (password.length()>0) {
                        admi.setPassword(StringUtil.md5(password));
                    }
                    if (firstname.length()>0) {
                        admi.setFirstName(firstname);
                    }
                    if (lastname.length()>0) {
                        admi.setLastName(lastname);
                    }
                    //admi.setRole(ro);
                    adminDAO.put(admi);
                    response.getWriter().write("success");
                }
                else {
                    response.getWriter().write("error");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if(request.getParameter("delete")!=null){
//            String ids=request.getSession().getAttribute("id").toString();
            String id=request.getParameter("id");
            try {
//                if (id.equals(ids)) {
//                    response.getWriter().write("error");
//                }
//                else {
                    adminDAO.delete(id);
                    response.getWriter().write("success");
//                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
