package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.AdminDAO;
import com.cmg.vrc.data.dao.impl.ClientCodeDAO;
import com.cmg.vrc.data.dao.impl.StaffMappingCompanyDAO;
import com.cmg.vrc.data.dao.impl.TeacherMappingCompanyDAO;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class ManagementTeacherOrStaff extends HttpServlet {
    class listCompany{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<TeacherMappingCompany> data;
    }

    private static final Logger logger = Logger.getLogger(ManagementTeacherOrStaff.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TeacherMappingCompanyDAO teacherMappingCompanyDAO=new TeacherMappingCompanyDAO();
        listCompany listCompany=new listCompany();
        if (request.getParameter("list") != null) {
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
            String userName = request.getSession().getAttribute("username").toString();

            Double count=0.0;

            try {
                if (search.length() > 0 || userName.length() > 0) {
                    count = teacherMappingCompanyDAO.getCountSearch(search, userName);
                }
                listCompany.draw = draw;
                listCompany.recordsTotal = count;
                listCompany.recordsFiltered = count;
                listCompany.data = teacherMappingCompanyDAO.listAll(start, length, search, col, oder, userName);
                Gson gson = new Gson();
                String admins = gson.toJson(listCompany);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if (request.getParameter("add") != null) {

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (request.getParameter("edit") != null) {

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (request.getParameter("delete") != null) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
