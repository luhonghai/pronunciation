package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.ClientCodeDAO;
import com.cmg.vrc.data.dao.impl.LicenseCodeCompanyDAO;
import com.cmg.vrc.data.jdo.*;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.dao.impl.LicenseCodeDAO;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by CMGT400 on 5/18/2015.
 */
public class LicenseCodes extends HttpServlet {
    class license{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<LicenseCodess> data;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LicenseCodeDAO lis = new LicenseCodeDAO();
        LicenseCode lisence = new LicenseCode();
        UserDeviceDAO userDeviceDAO=new UserDeviceDAO();
        UserDevice userDevice=new UserDevice();
        LicenseCodeCompanyDAO licenseCodeCompanyDAO=new LicenseCodeCompanyDAO();
        LicenseCodeCompany licenseCodeCompany=new LicenseCodeCompany();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        if (request.getParameter("list") != null) {
            LicenseCodes.license licen = new license();
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
            String ac = request.getParameter("account");
            String activated = request.getParameter("Acti");
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            String dateFrom2 = request.getParameter("dateFrom2");
            String dateTo2 = request.getParameter("dateTo2");
            String company=request.getParameter("company");
            Double count;
            try {
                if(search.length()>0||ac.length()>0||activated.length()>0||dateFrom!=null||dateTo!=null ||dateFrom2!=null||dateTo2!=null && company.length()>0){
                   List<LicenseCodess> licenseCodesses=lis.listAllByCompanySearch(search, col, oder ,ac,activated,dateFrom,dateTo,dateFrom2,dateTo2,company);
                    count=(double)licenseCodesses.size();
                }else {
                     count = lis.getCount();
                }
                licen.draw = draw;
                licen.recordsTotal = count;
                licen.recordsFiltered = count;
                licen.data=lis.listAllByCompany(start, length, search, col, oder ,ac,activated,dateFrom,dateTo,dateFrom2,dateTo2,company);
                Gson gson = new Gson();
                String licenseCode = gson.toJson(licen);
                response.getWriter().write(licenseCode);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("listCompany")!=null){
            ClientCodeDAO clientCodeDAO = new ClientCodeDAO();
            ClientCode ad = new ClientCode();
            try {
                List<ClientCode> clientCodes=clientCodeDAO.listAll();
                Gson gson = new Gson();
                String company = gson.toJson(clientCodes);
                response.getWriter().write(company);

            }catch (Exception e){
                e.printStackTrace();
            }


        }

        if (request.getParameter("addCode") != null) {

            String company=request.getParameter("company");
            String number=request.getParameter("number");
            int n=Integer.parseInt(number);
            try {
                for(int k=1;k<=n;k++) {
                    lisence = new LicenseCode();
                    licenseCodeCompany=new LicenseCodeCompany();
                    String codeRandom = randomString(6);
                    List<com.cmg.vrc.data.jdo.LicenseCode> lists = lis.listAll();
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getCode().equals(codeRandom)) {
                            codeRandom = randomString(6);
                        }
                    }
                    licenseCodeCompany.setCode(codeRandom);
                    licenseCodeCompany.setCompany(company);
                    licenseCodeCompany.setIsDeleted(false);
                    licenseCodeCompanyDAO.put(licenseCodeCompany);

                    lisence.setCode(codeRandom);
                    lisence.setCreatedDate(new Date());
                    lisence.setActivated(true);
                    lisence.setIsDeleted(false);
                    lis.put(lisence);
                }
                response.getWriter().write("success");

            } catch (Exception e) {
                logger.error("Could not add code", e);
            }

        }
        if(request.getParameter("activated")!=null){
            String acti=request.getParameter("acti");
            String id=request.getParameter("id");
            boolean activated = Boolean.parseBoolean(acti);
            lisence.setId(id);
            lisence.setActivated(activated);
            try {
                LicenseCode licen = lis.getById(id);
                licen.setActivated(activated);
                lis.put(licen);
                response.getWriter().write("success");
            }catch (Exception e){
               e.printStackTrace();
           }
        }

        if(request.getParameter("detailmodal")!=null){
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
    public static String randomString( int len )
    {
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

}
