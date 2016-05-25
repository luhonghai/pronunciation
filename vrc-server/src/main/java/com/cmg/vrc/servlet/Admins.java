package com.cmg.vrc.servlet;

import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.services.objectives.ObjectiveService;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.AdminDAO;
import com.cmg.vrc.data.dao.impl.ClientCodeDAO;
import com.cmg.vrc.data.dao.impl.StaffMappingCompanyDAO;
import com.cmg.vrc.data.dao.impl.TeacherMappingCompanyDAO;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
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
public class Admins extends HttpServlet {
    class admin{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<Admin> data;
    }
    class company{
        public String message;

        List<ClientCode> clientCodes;
    }
    class companys{
        public String message;

        List<ClientCode> clientCodes;
        List<ClientCode> check;
    }

    class test{
        private String message;
        private String content;
    }





    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        AdminDAO adminDAO = new AdminDAO();
        TeacherMappingCompanyDAO teacherMappingCompanyDAO=new TeacherMappingCompanyDAO();

        String roless = request.getSession().getAttribute("role").toString();
        if (request.getParameter("list") != null) {
            Admins.admin admin = new admin();
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
            String username = request.getParameter("username");
            String first = request.getParameter("firstname");
            String last = request.getParameter("lastname");
            Double count;

            try {
                if (search.length() > 0 || username.length() > 0 || first.length() > 0 || last.length() > 0) {
                    count = adminDAO.getCountSearch(search, username, first, last);
                } else {
                    count = adminDAO.getCount();
                }
                admin.draw = draw;
                admin.recordsTotal = count;
                admin.recordsFiltered = count;
                List<Admin> fromDb = adminDAO.listAll(start, length, search, col, oder, username, first, last);
                List<Admin> toClient = adminDAO.addedCompanyToUser(fromDb);
                admin.data = toClient;
                Gson gson = new Gson();
                String admins = gson.toJson(admin);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

        if (request.getParameter("add") != null) {
            String username = request.getParameter("username");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String idCompany = request.getParameter("idCompany");
            int ro = 0;
            boolean addTeacherStaff = false;
            if (role.equals("Admin")) {
                ro = Constant.ROLE_ADMIN;
            }else if (role.equals("User")) {
                ro = Constant.ROLE_USER;
            }else if (role.equals("Staff")){
                ro = Constant.ROLE_STAFF;
                addTeacherStaff = true;
            }else if (role.equals("Teacher")){
                ro = Constant.ROLE_TEACHER;
                addTeacherStaff = true;
            }
            try {
                Admin a = adminDAO.getUserByEmail(username);

                if (a != null) {
                    response.getWriter().write("error");
                } else {
                    Admin ad=new Admin();
                    ad.setUserName(username);
                    ad.setPassword(StringUtil.md5(password));
                    ad.setFirstName(firstname);
                    ad.setLastName(lastname);
                    ad.setRole(ro);
                    adminDAO.put(ad);
                    if(addTeacherStaff){
                        TeacherMappingCompanyDAO dao = new TeacherMappingCompanyDAO();
                        TeacherMappingCompany tmc = new TeacherMappingCompany();
                        tmc.setIdCompany(idCompany);
                        tmc.setUserName(username);
                        tmc.setIsDeleted(false);
                        tmc.setType(role);
                        dao.put(tmc);
                    }
                    response.getWriter().write("success");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (request.getParameter("edit") != null) {
            String idd = request.getSession().getAttribute("id").toString();
            String id = request.getParameter("id");
            String username = request.getParameter("username");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String idCompany = request.getParameter("idCompany");
            int ro = 0;
            boolean addTeacherStaff = false;
            if (role.equals("Admin")) {
                ro = Constant.ROLE_ADMIN;
            }else if (role.equals("User")) {
                ro = Constant.ROLE_USER;
            }else if (role.equals("Staff")){
                ro = Constant.ROLE_STAFF;
                addTeacherStaff = true;
            }else if (role.equals("Teacher")){
                ro = Constant.ROLE_TEACHER;
                addTeacherStaff = true;
            }
            try {
                if (!id.equals(idd)) {
                    Admin admi = adminDAO.getById(id);
                    if (password.length() > 0) {
                        admi.setPassword(StringUtil.md5(password));
                    }
                    if (firstname.length() > 0) {
                        admi.setFirstName(firstname);
                    }
                    if (lastname.length() > 0) {
                        admi.setLastName(lastname);
                    }
                    admi.setRole(ro);
                    adminDAO.put(admi);
                    if (addTeacherStaff){
                        TeacherMappingCompanyDAO dao = new TeacherMappingCompanyDAO();
                        TeacherMappingCompany tmc = dao.getCompanyByTeacherName(username);
                        if(tmc!=null){
                            TeacherMappingCompany tmp = dao.getById(tmc.getId());
                            tmp.setIdCompany(idCompany);
                            tmp.setType(role);
                            dao.put(tmp);
                        }else{
                            TeacherMappingCompany tmp = new TeacherMappingCompany();
                            tmp.setIdCompany(idCompany);
                            tmp.setUserName(username);
                            tmp.setIsDeleted(false);
                            tmp.setType(role);
                            dao.put(tmp);
                        }

                    }
                    response.getWriter().write("success");
                } else if (id.equals(idd)) {
                    Admin admi = adminDAO.getById(id);
                    if (password.length() > 0) {
                        admi.setPassword(StringUtil.md5(password));
                    }
                    if (firstname.length() > 0) {
                        admi.setFirstName(firstname);
                    }
                    if (lastname.length() > 0) {
                        admi.setLastName(lastname);
                    }
                    adminDAO.put(admi);
                    response.getWriter().write("success");
                } else {
                    response.getWriter().write("error");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (request.getParameter("delete") != null) {
            String idd = request.getSession().getAttribute("id").toString();
            String id = request.getParameter("id");
            String role = request.getParameter("role");
            String username = request.getParameter("username");
            try {
                if(role.equals("1") || role.equals("2")) {
                    if (!id.equals(idd)) {
                        adminDAO.delete(id);
                        response.getWriter().write("success");
                    } else {
                        response.getWriter().write("error");
                    }
                }else{
                    adminDAO.delete(id);
                    teacherMappingCompanyDAO.updateEdit(username);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }
        if (request.getParameter("action") != null) {
            ClientCodeDAO clientCodeDAO = new ClientCodeDAO();
            company company=new company();
            try {
                List<ClientCode> clientCodes = clientCodeDAO.listAll();
                company.message="success";
                company.clientCodes=clientCodes;
                Gson gson = new Gson();
                String companys = gson.toJson(company);
                response.getWriter().write(companys);

            } catch (Exception e) {
                company.message="error";
                company.clientCodes=null;
                Gson gson = new Gson();
                String companys = gson.toJson(company);
                response.getWriter().write(companys);
                e.printStackTrace();
            }
        }


        if(request.getParameter("edittest")!=null) {
            String id=request.getParameter("id");
            test test=new test();
            try {
                Admin admin=adminDAO.getById(id);
                if(admin!=null) {
                    int role = admin.getRole();
                    if (role == Constant.ROLE_ADMIN || role == Constant.ROLE_USER) {
                        test.content = "<select name=\"editrole\" id=\"editrole\" class=\"form-control\" required=\"required\"> ' +\n" +
                                "            '<option value=\"Admin\">Admin</option> ' +\n" +
                                "            '<option value=\"User\">User</option> ' +\n" +
                                "            '</select>";
                    } else {
                        test.content = "<select name=\"editrole\" id=\"editrole\" class=\"form-control\" required=\"required\"> ' +\n" +
                                "            '<option value=\"Staff\">Staff</option> ' +\n" +
                                "            '<option value=\"Teacher\">Teacher</option> ' +\n" +
                                "            '</select>";
                    }
                    test.message = "success";
                    Gson gson = new Gson();
                    String companys = gson.toJson(test);
                    response.getWriter().write(companys);
                }else {
                    test.message="error";
                    test.content="";
                    Gson gson = new Gson();
                    String companys = gson.toJson(test);
                    response.getWriter().write(companys);
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
