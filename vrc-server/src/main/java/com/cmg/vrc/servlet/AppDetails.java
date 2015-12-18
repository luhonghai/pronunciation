package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.AppDetailDAO;
import com.cmg.vrc.data.dao.impl.NumberDateDAO;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.AppDetail;
import com.cmg.vrc.data.jdo.NumberDate;
import com.cmg.vrc.data.jdo.Setting;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
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
        NumberDate numberDate=new NumberDate();
        NumberDateDAO numberDateDAO=new NumberDateDAO();
        Setting setting=new Setting();
        if(request.getParameter("list")!=null){
            try {

                    List<AppDetail> appDetails=appDetailDAO.listAll();
                    NumberDate numberDates=numberDateDAO.numberDate();
                    if(appDetails.size()>0) {
                        setting.setIdAppDetail(appDetails.get(0).getId());
                        setting.setRegistration(appDetails.get(0).isRegistration());
                        setting.setNoAccessMessage(appDetails.get(0).getNoAccessMessage());
                        setting.setIdNumberDate(numberDates.getId());
                        setting.setCreatedDate(numberDates.getCreatedDate());
                        setting.setUserName(numberDates.getUserName());
                        setting.setNumberDate(numberDates.getNumberDate());
                    }
                    Gson gson = new Gson();
                    String app = gson.toJson(setting);
                    response.getWriter().write(app);


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(request.getParameter("save")!=null){
//            Admin admin=(Admin)request.getSession().getAttribute("nambui");
//            String rr=admin.getRole();
            boolean check=false;

            String role=request.getSession().getAttribute("role").toString();
            String username=request.getSession().getAttribute("username").toString();
            String message=request.getParameter("message");
            String regis=request.getParameter("regis");
            String number=request.getParameter("numberDate");
            String id=request.getParameter("id");
            String idnumber=request.getParameter("idnumber");
            boolean register = Boolean.parseBoolean(regis);
            appDetail.setId(id);
            appDetail.setRegistration(register);
            appDetail.setNoAccessMessage(message);
            numberDate.setNumberDate(Integer.parseInt(number));
            numberDate.setCreatedDate(new Date(System.currentTimeMillis()));
            numberDate.setUserName(username);
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
                    if(numberDateDAO.getCount()==0){
                        numberDateDAO.put(numberDate);
                    }else  if(numberDateDAO.getCount()>0){
                        NumberDate numberDate1=numberDateDAO.getById(idnumber);
                        numberDate1.setCreatedDate(new Date(System.currentTimeMillis()));
                        numberDate1.setUserName(username);
                        numberDate1.setNumberDate(Integer.parseInt(number));
                        numberDateDAO.put(numberDate1);
                    }
                        response.getWriter().write("success");
                    }
                if(role.equals("2")){
                        response.getWriter().write("error");
                }

            } catch (Exception e) {
                response.getWriter().write("error");
            }
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
