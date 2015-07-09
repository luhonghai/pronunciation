package com.cmg.vrc.servlet;

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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;




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
            String co = request.getParameter("code");
            String activated = request.getParameter("Acti");
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
                if(search.length()>0||ac.length()>0||co.length()>0||activated.length()>0||dateFrom1!=null||dateTo1!=null){
                    count=lis.getCountSearch(search,ac,co,activated,dateFrom1,dateTo1);
                }else {
                     count = lis.getCount();
                }
                licen.draw = draw;
                licen.recordsTotal = count;
                licen.recordsFiltered = count;
                licen.data = lis.listAll(start, length, search, col, oder,ac,co,activated,dateFrom1,dateTo1);

//                List<com.cmg.vrc.data.jdo.LicenseCode> list=lis.listAll(start,length);
                Gson gson = new Gson();
                String licenseCode = gson.toJson(licen);
                response.getWriter().write(licenseCode);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if (request.getParameter("addCode") != null) {
            try {
                String codeRandom = randomString(6);
                List<com.cmg.vrc.data.jdo.LicenseCode> lists = lis.listAll();
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getCode().contains(codeRandom)) {
//                       logger.debug(lists.get(i).getCode());
                        codeRandom = randomString(6);
                    }
                }

                lisence.setCode(codeRandom);
                lis.put(lisence);
                lisence=lis.getByCode(codeRandom);
                Gson gson = new Gson();
                String li = gson.toJson(lisence);
                response.getWriter().write(li);

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
