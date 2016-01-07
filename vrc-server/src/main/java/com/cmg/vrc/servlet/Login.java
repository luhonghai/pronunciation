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
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by CMGT400 on 5/8/2015.
 */
   public class Login extends HttpServlet{
     private static final Logger logger = Logger.getLogger(AuthHandler.class
            .getName());
    class logins{
        public String message;
        public int role;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     if(request.getParameter("login1")!=null){
         login(request,response);

     }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO adminDAO=new AdminDAO();
        HttpSession session=request.getSession();
        String name=request.getParameter("account");
        String pass=request.getParameter("pass");
        logins logins=new logins();
        try {
           Admin admin= adminDAO.getUserByEmailPassword(name, StringUtil.md5(pass));
            if (admin!=null){
                    session.setAttribute("nambui",admin);
                    session.setAttribute("id",admin.getId());
                    session.setAttribute("username",admin.getUserName());
                    session.setAttribute("password",admin.getPassword());
                    session.setAttribute("role",admin.getRole());
                    logins.message="success";
                    logins.role=admin.getRole();
                    Gson gson = new Gson();
                    String admins = gson.toJson(logins);
                    response.getWriter().write(admins);
            }
            else {
                logins.message="error";
                logins.role=admin.getRole();
                Gson gson = new Gson();
                String admins = gson.toJson(logins);
                response.getWriter().write(admins);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


//        if(name.equals("admin") && pass.equals("admincmg@3f")){
//            session.setAttribute("username",name);
//            session.setAttribute("password",pass);
//            response.getWriter().write("success");
//        }
//        else {
//            response.getWriter().write("error");
//        }
    }
}
