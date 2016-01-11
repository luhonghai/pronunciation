package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.ClassDAO;
import com.cmg.vrc.data.dao.impl.StudentMappingClassDAO;
import com.cmg.vrc.data.jdo.ClassJDO;
import com.cmg.vrc.data.jdo.Student;
import com.cmg.vrc.data.jdo.StudentMappingClass;
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
public class StudentServlet extends HttpServlet {
    class admin{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<StudentMappingClass> data;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentMappingClassDAO classDAO=new StudentMappingClassDAO();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        admin admin=new admin();
        if (request.getParameter("listStudent") != null) {
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
            String idClass=request.getParameter("");

            Double count;

            try {
                if(search.length()>0){
                    count=classDAO.getCountSearch(search);
                }else {
                    count = classDAO.getCount();
                }
                admin.data=classDAO.listAll(start,length,search,col,oder,idClass);
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

            try{
                ClassJDO classJDO=new ClassJDO();
                classJDO.setClassName(classname);
                classJDO.setDefinition(definition);
                classJDO.setCreatedDate(new Date(System.currentTimeMillis()));
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
