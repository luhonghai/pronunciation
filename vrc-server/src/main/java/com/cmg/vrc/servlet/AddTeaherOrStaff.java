package com.cmg.vrc.servlet;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class AddTeaherOrStaff extends HttpServlet {
    class teacherorStaff{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<TeacherOrStaffList> data;
    }
    private static final Logger logger = Logger.getLogger(AddTeaherOrStaff.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO adminDAO = new AdminDAO();
        ManageTeacherOrStaffDAO manageTeacherOrStaffDAO=new ManageTeacherOrStaffDAO();
        TeacherMappingCompanyDAO teacherMappingCompanyDAO=new TeacherMappingCompanyDAO();
        Admin ad = new Admin();
        teacherorStaff teacherorStaff=new teacherorStaff();
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
            String idCompany = request.getParameter("idCompany");
            Double count=0.0;

            try {
                if (search.length() > 0 || idCompany.length() > 0) {
                    List<TeacherOrStaffList> teacherOrStaffLists=manageTeacherOrStaffDAO.listAll(search, col, oder,idCompany);
                    if(teacherOrStaffLists!=null){
                        count = (double)teacherOrStaffLists.size();
                    }
                }
                teacherorStaff.draw = draw;
                teacherorStaff.recordsTotal = count;
                teacherorStaff.recordsFiltered = count;
                teacherorStaff.data = manageTeacherOrStaffDAO.listAll(start, length, search, col, oder, idCompany);
                Gson gson = new Gson();
                String admins = gson.toJson(teacherorStaff);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if(request.getParameter("add")!=null){
            String username = request.getParameter("username");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String idCompany = request.getParameter("idCompany");
            String company = request.getParameter("company");
            Gson gson = new Gson();
            try{
                int ro = 0;
                if (role.length() > 0 && role.equals(Constant.STAFF)) {
                    ro = Constant.ROLE_STAFF;
                }
                if (role.length() > 0 && role.equals(Constant.TEACHER)) {
                    ro = Constant.ROLE_TEACHER;
                }
                Admin a = adminDAO.getUserByEmail(username);
                if (a != null) {
                    response.getWriter().write("exist");
                } else {
                    ad.setFirstName(firstname);
                    ad.setUserName(username);
                    ad.setLastName(lastname);
                    ad.setPassword(StringUtil.md5(password));
                    ad.setRole(ro);
                    adminDAO.put(ad);

                    TeacherMappingCompany teacherMappingCompany = new TeacherMappingCompany();
                    teacherMappingCompany.setIsDeleted(false);
                    teacherMappingCompany.setIdCompany(idCompany);
                    if(ro == Constant.ROLE_STAFF){
                        teacherMappingCompany.setType(Constant.STAFF);
                    }else{
                        teacherMappingCompany.setType(Constant.TEACHER);
                    }
                    teacherMappingCompany.setCompany(company);
                    teacherMappingCompany.setUserName(username);
                    teacherMappingCompanyDAO.put(teacherMappingCompany);

                    response.getWriter().write("success");
                }
            }catch (Exception e){
                response.getWriter().write("error");
               e.getStackTrace();
            }
        }
        if (request.getParameter("edit") != null) {
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String username = request.getParameter("username");
            String roleold = request.getParameter("roleold");
            String idCompany = request.getParameter("idCompany");
            String company = request.getParameter("company");
            int ro = 0;
            if (role.length() > 0 && role.equals("Staff")) {
                ro = Constant.ROLE_STAFF;
            }
            if (role.length() > 0 && role.equals("Teacher")) {
                ro = Constant.ROLE_TEACHER;
            }
            try {

                if(role.equals(roleold)){
                    Admin admin = adminDAO.getUserByEmail(username);
                    admin.setPassword(StringUtil.md5(password));
                    adminDAO.put(admin);
                }else{
                        Admin admin = adminDAO.getUserByEmail(username);
                        admin.setPassword(StringUtil.md5(password));
                        admin.setRole(ro);
                        adminDAO.put(admin);
                        TeacherMappingCompany teacherMappingCompany=new TeacherMappingCompany();
                        teacherMappingCompany.setIdCompany(idCompany);
                        teacherMappingCompany.setCompany(company);
                        teacherMappingCompany.setIsDeleted(false);
                        teacherMappingCompany.setUserName(username);
                        if(role.equals("Staff")){
                            teacherMappingCompany.setType(Constant.STAFF);
                        }else {
                            teacherMappingCompany.setType(Constant.TEACHER);
                        }
                        teacherMappingCompanyDAO.put(teacherMappingCompany);
                }
                response.getWriter().write("success");

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }

        }

        if (request.getParameter("delete") != null) {
            String id = request.getParameter("id");
            String role = request.getParameter("role");
            String username = request.getParameter("username");
            try {
                Admin admin=adminDAO.getUserByEmail(username);
                adminDAO.delete(admin.getId());
                  teacherMappingCompanyDAO.updateEdit(username);
                response.getWriter().write("success");
            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
