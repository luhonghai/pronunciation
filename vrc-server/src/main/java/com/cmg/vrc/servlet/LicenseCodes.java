package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.ClientCodeDAO;
import com.cmg.vrc.data.dao.impl.LicenseCodeCompanyDAO;
import com.cmg.vrc.data.jdo.ClientCode;
import com.cmg.vrc.data.jdo.LicenseCodeCompany;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.data.dao.impl.LicenseCodeDAO;
import com.cmg.vrc.data.jdo.LicenseCode;
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

        List<LicenseCode> data;
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
            Date dateFrom1=null;
            Date dateTo1=null;
            Date dateFrom3=null;
            Date dateTo3=null;

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
            if(dateFrom2.length()>0){
                try {
                    dateFrom3=df.parse(dateFrom2);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            if(dateTo2.length()>0){
                try {
                    dateTo3=df.parse(dateTo2);
                }catch (Exception e){
                    e.getStackTrace();
                }

            }
            try {
                if(search.length()>0||ac.length()>0||activated.length()>0||dateFrom1!=null||dateTo1!=null ||dateFrom3!=null||dateTo3!=null && company.length()==0){
                    count=lis.getCountSearch(search,ac,activated,dateFrom1,dateTo1,dateFrom3,dateTo3);
                }else {
                     count = lis.getCount();
                }
                if(company.length()>0){
                    count=(double)lis.listAllByCompanySearch(search,ac,activated,dateFrom1,dateTo1,dateFrom3,dateTo3,company).size();
                }
                licen.draw = draw;
                licen.recordsTotal = count;
                licen.recordsFiltered = count;
                if(company.length()>0){
                    licen.data=lis.listAllByCompany(start, length, search,ac,activated,dateFrom1,dateTo1,dateFrom3,dateTo3,company);
                }
                else {
                    licen.data = lis.listAll(start, length, search, col, oder, ac, activated, dateFrom1, dateTo1,dateFrom3,dateTo3);
                }
//                List<com.cmg.vrc.data.jdo.LicenseCode> list=lis.listAll(start,length);
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
