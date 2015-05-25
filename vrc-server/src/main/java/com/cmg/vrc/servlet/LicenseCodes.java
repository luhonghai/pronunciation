package com.cmg.vrc.servlet;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import com.cmg.vrc.data.dao.impl.LicenseCodeDAO;
import com.cmg.vrc.data.jdo.LicenseCode;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
        LicenseCodeDAO lis=new LicenseCodeDAO();
        LicenseCode lisence=new LicenseCode();

        if(request.getParameter("list")!=null){
            LicenseCodes.license licen=new license();

            String s=request.getParameter("start");
            String l=request.getParameter("length");
            String d=request.getParameter("draw");
            String search=request.getParameter("search[value]");
            int start=Integer.parseInt(s);
            int length=Integer.parseInt(l);
            int draw=Integer.parseInt(d);
            try {
                Double count=lis.getCount();
                licen.draw=draw;
                licen.recordsTotal=count;
                licen.recordsFiltered=count;
                licen.data=lis.listAll(start,length);
//                List<com.cmg.vrc.data.jdo.LicenseCode> list=lis.listAll(start,length);
                Gson gson = new Gson();
                String licenseCode = gson.toJson(licen);
                response.getWriter().write(licenseCode);

            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if (request.getParameter("addCode")!=null){
            try {
                String codeRandom=randomString(6);
               List<com.cmg.vrc.data.jdo.LicenseCode> lists=lis.listAll();
                for (int i=0;i<lists.size();i++){
                    if(lists.get(i).getCode().contains(codeRandom)){
//                       logger.debug(lists.get(i).getCode());
                        codeRandom=randomString(6);
                    }
                }

            lisence.setCode(codeRandom);
            response.getWriter().write("success");
               lis.put(lisence);
            }catch (Exception e){
               logger.error("Could not add code",e);
            }

       }

        if(request.getParameter("filter")!=null){
            String ac=request.getParameter("account");
            String co=request.getParameter("code");
            String activated=request.getParameter("Acti");
            String dateFrom=request.getParameter("dateFrom");
            String dateTo=request.getParameter("dateTo");
            String dateFrom1=dateFrom.substring(0, 10);
            String dateTo1=dateTo.substring(0,10);

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
