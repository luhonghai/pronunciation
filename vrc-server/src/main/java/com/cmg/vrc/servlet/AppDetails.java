package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.AppDetailDAO;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.AppDetail;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by CMGT400 on 6/3/2015.
 */
public class AppDetails extends HttpServlet{
    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AppDetailDAO appDetailDAO=new AppDetailDAO();
        AppDetail appDetail=new AppDetail();
        if(request.getParameter("list")!=null){
            try {

                    List<AppDetail> appDetails=appDetailDAO.listAll();
                    Gson gson = new Gson();
                    String app = gson.toJson(appDetails);
                    response.getWriter().write(app);


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(request.getParameter("save")!=null){
//            Admin admin=(Admin)request.getSession().getAttribute("nambui");
//            String rr=admin.getRole();

            String role=request.getSession().getAttribute("role").toString();
            String message=request.getParameter("message");
            String regis=request.getParameter("regis");
            String id=request.getParameter("id");
            boolean register = Boolean.parseBoolean(regis);
            appDetail.setId(id);
            appDetail.setRegistration(register);
            appDetail.setNoAccessMessage(message);
            try {
                if(role.equals("1")) {
                    if (appDetailDAO.getCount() == 0) {
                        appDetailDAO.put(appDetail);
                    } else if (appDetailDAO.getCount() > 0) {
                        AppDetail appDetail1 = appDetailDAO.getById(id);
                        appDetail1.setNoAccessMessage(message);
                        appDetail1.setRegistration(register);
                        appDetail1.setId(id);
                        appDetailDAO.put(appDetail1);
                    }
                        response.getWriter().write("success");
                    }
                if(role.equals("2")){
                        response.getWriter().write("error");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
