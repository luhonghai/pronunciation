package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.AdminDAO;
import com.cmg.vrc.data.dao.impl.ClassDAO;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.ClassJDO;
import com.cmg.vrc.util.StringUtil;
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

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class ClassServlet extends HttpServlet {
    class admin{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<ClassJDO> data;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClassDAO classDAO=new ClassDAO();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        if (request.getParameter("list") != null) {
            ClassServlet.admin admin = new admin();
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
            String classname = request.getParameter("classname");
            String dateFrom =(String) StringUtil.isNull(request.getParameter("CreateDateFrom"), "");
            String dateTo =(String) StringUtil.isNull(request.getParameter("CreateDateTo"), "");
            Date dateFrom1=null;
            Date dateTo1=null;


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
            Double count;

            try {
                if(search.length()>0||classname.length()>0|| dateFrom1!=null||dateTo1!=null){
                    count=classDAO.getCountSearch(search,classname,dateFrom1,dateTo1);
                }else {
                    count = classDAO.getCount();
                }
                admin.data=classDAO.listAll(start,length,search,col,oder,classname,dateFrom1,dateTo1);
                admin.draw = draw;
                admin.recordsTotal = count;
                admin.recordsFiltered = count;
                Gson gson = new Gson();
                String admins = gson.toJson(admin);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("add")!=null){
            String classname = request.getParameter("classname");
            String definition = request.getParameter("definition");
            int version=0;

            try{
                version=classDAO.getLatestVersion() +1;
                ClassJDO classJDO=new ClassJDO();
                classJDO.setClassName(classname);
                classJDO.setDefinition(definition);
                classJDO.setCreatedDate(new Date(System.currentTimeMillis()));
                classJDO.setIsDeleted(false);
                classJDO.setVersion(version);
                classDAO.put(classJDO);
                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }

        }
        if(request.getParameter("edit")!=null){
            String id=request.getParameter("id");
            String difinition = request.getParameter("difinition");

            try{
                ClassJDO classJDO=new ClassJDO();
                classJDO=classDAO.getById(id);
                classJDO.setDefinition(difinition);
                classDAO.put(classJDO);
                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }

        }

        if(request.getParameter("delete")!=null){

            String id=request.getParameter("id");
            try {
                    classDAO.delete(id);
                    response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
