package com.cmg.vrc.servlet;

import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.services.objectives.ObjectiveService;
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
        StaffMappingCompanyDAO staffMappingCompanyDAO=new StaffMappingCompanyDAO();

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
                admin.data = adminDAO.listAll(start, length, search, col, oder, username, first, last);
//                for(int i=0;i<admin.data.size();i++){
//                    int a=admin.data.get(i).getRole();
//                    if(a==1){
//
//                    }
//                }

//                List<com.cmg.vrc.data.jdo.LicenseCode> list=lis.listAll(start,length);
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

            int ro = 0;
            if (role.length() > 0 && role.equals("Admin")) {
                ro = 1;
            }
            if (role.length() > 0 && role.equals("User")) {
                ro = 2;
            }
            try {
                Admin a = adminDAO.getUserByEmail(username);
                Admin ad=new Admin();
                if (a != null) {
                    response.getWriter().write("error");
                } else {
                    ad.setUserName(username);
                    ad.setPassword(StringUtil.md5(password));
                    ad.setFirstName(firstname);
                    ad.setLastName(lastname);
                    ad.setRole(ro);
                    adminDAO.put(ad);
                    response.getWriter().write("success");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (request.getParameter("edit") != null) {
            String idd = request.getSession().getAttribute("id").toString();
            String id = request.getParameter("id");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            int ro = 0;
            if (role.length() > 0 && role.equals("Admin")) {
                ro = 1;
            }
            if (role.length() > 0 && role.equals("User")) {
                ro = 2;
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
                    //admi.setRole(ro);
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
                    if(role.equals("3")){
                        staffMappingCompanyDAO.updateEdit(username);
                    }else {
                        teacherMappingCompanyDAO.updateEdit(username);

                    }
                }
            } catch (Exception e) {
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
        if(request.getParameter("addTeacher")!=null){
            String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
            Gson gson = new Gson();
            try{
                TeacherOrStaffMappingCompany dto = gson.fromJson(jsonClient, TeacherOrStaffMappingCompany.class);
                Admin ad=new Admin();
                String role=dto.getRole();
                int ro = 0;
                if (role.length() > 0 && role.equals("Staff")) {
                    ro = 3;
                }
                if (role.length() > 0 && role.equals("Teacher")) {
                    ro = 4;
                }
                Admin a = adminDAO.getUserByEmail(dto.getFullName());
                if (a != null) {
                    response.getWriter().write("exist");
                } else {
                    ad.setFirstName(dto.getFirstName());
                    ad.setUserName(dto.getFullName());
                    ad.setLastName(dto.getLastName());
                    ad.setPassword(StringUtil.md5(dto.getPassword()));
                    ad.setRole(ro);
                    adminDAO.put(ad);

                    List<Company> companies = dto.getCompanies();
                    if (ro == 3) {
                        for (Company company : companies) {

                            StaffMappingCompany staffMappingCompany = new StaffMappingCompany();
                            staffMappingCompany.setIsDeleted(false);
                            staffMappingCompany.setIdCompany(company.getIdCompany());
                            staffMappingCompany.setCompany(company.getCompanyName());
                            staffMappingCompany.setStaffName(dto.getFullName());
                            staffMappingCompanyDAO.put(staffMappingCompany);
                        }
                    } else {
                        for (Company company : companies) {
                            TeacherMappingCompany teacherMappingCompany = new TeacherMappingCompany();
                            teacherMappingCompany.setIsDeleted(false);
                            teacherMappingCompany.setIdCompany(company.getIdCompany());
                            teacherMappingCompany.setCompany(company.getCompanyName());
                            teacherMappingCompany.setTeacherName(dto.getFullName());
                            teacherMappingCompanyDAO.put(teacherMappingCompany);
                        }
                    }

                    response.getWriter().write("success");
                }
            }catch (Exception e){
               e.getStackTrace();
            }
        }

        if(request.getParameter("editTeacher")!=null){
            String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
            Gson gson = new Gson();
            try{
                TeacherOrStaffMappingCompany dto = gson.fromJson(jsonClient, TeacherOrStaffMappingCompany.class);
                String role=dto.getRole();
                int ro = 0;
                if (role.length() > 0 && role.equals("Staff")) {
                    ro = 3;
                }
                if (role.length() > 0 && role.equals("Teacher")) {
                    ro = 4;
                }
                Admin admin = adminDAO.getUserByEmail(dto.getFullName());
                int roleold=admin.getRole();
                String username=admin.getUserName();

                admin.setFirstName(dto.getFirstName());
                admin.setLastName(dto.getLastName());
                if(dto.getPassword().length()>0) {
                    admin.setPassword(StringUtil.md5(dto.getPassword()));
                }
                admin.setRole(ro);
                adminDAO.put(admin);

                    List<Company> companies = dto.getCompanies();
                if(ro==roleold) {
                    if (ro == 3) {
                        staffMappingCompanyDAO.deleteStaff(dto.getFullName());
                        for (Company company : companies) {
                            StaffMappingCompany staffMappingCompany = new StaffMappingCompany();
                            staffMappingCompany.setIsDeleted(false);
                            staffMappingCompany.setIdCompany(company.getIdCompany());
                            staffMappingCompany.setCompany(company.getCompanyName());
                            staffMappingCompany.setStaffName(dto.getFullName());
                            staffMappingCompanyDAO.put(staffMappingCompany);
                        }
                    } else {
                        teacherMappingCompanyDAO.deleteTeacher(dto.getFullName());
                        for (Company company : companies) {
                            TeacherMappingCompany teacherMappingCompany = new TeacherMappingCompany();

                            teacherMappingCompany.setIsDeleted(false);
                            teacherMappingCompany.setIdCompany(company.getIdCompany());
                            teacherMappingCompany.setCompany(company.getCompanyName());
                            teacherMappingCompany.setTeacherName(dto.getFullName());
                            teacherMappingCompanyDAO.put(teacherMappingCompany);
                        }
                    }
                }else if(ro==3){
                    teacherMappingCompanyDAO.updateEdit(username);
                    for (Company company : companies) {

                        StaffMappingCompany staffMappingCompany = new StaffMappingCompany();
                        staffMappingCompany.setIsDeleted(false);
                        staffMappingCompany.setIdCompany(company.getIdCompany());
                        staffMappingCompany.setCompany(company.getCompanyName());
                        staffMappingCompany.setStaffName(dto.getFullName());
                        staffMappingCompanyDAO.put(staffMappingCompany);
                    }
                }else {
                    staffMappingCompanyDAO.updateEdit(username);
                    for (Company company : companies) {
                        TeacherMappingCompany teacherMappingCompany = new TeacherMappingCompany();
                        teacherMappingCompany.setIsDeleted(false);
                        teacherMappingCompany.setIdCompany(company.getIdCompany());
                        teacherMappingCompany.setCompany(company.getCompanyName());
                        teacherMappingCompany.setTeacherName(dto.getFullName());
                        teacherMappingCompanyDAO.put(teacherMappingCompany);
                    }
                }

                    response.getWriter().write("success");

            }catch (Exception e){
                e.getStackTrace();
            }
        }
        if (request.getParameter("getCompany") != null) {
            ClientCodeDAO clientCodeDAO = new ClientCodeDAO();
            String role=request.getParameter("role");
            String username=request.getParameter("username");
            companys company=new companys();
            try {
                Admin admin=adminDAO.getUserByEmail(username);
                int ro=admin.getRole();
                if(ro==3) {
                    List<ClientCode> clientCode = clientCodeDAO.getCompanyByStaff(username);
                    List<ClientCode> check=clientCodeDAO.CompanyStaff(username);
                    company.message = "success";
                    company.clientCodes = clientCode;
                    company.check=check;
                    Gson gson = new Gson();
                    String companys = gson.toJson(company);
                    response.getWriter().write(companys);
                }else {
                    List<ClientCode> clientCodes = clientCodeDAO.getCompanyByTeacherName(username);
                    List<ClientCode> check=clientCodeDAO.CompanyTeacher(username);
                    company.message = "success";
                    company.clientCodes = clientCodes;
                    company.check=check;
                    Gson gson = new Gson();
                    String companys = gson.toJson(company);
                    response.getWriter().write(companys);
                }

            } catch (Exception e) {
                company.message="error";
                company.clientCodes=null;
                company.check=null;
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
                    if (role == 1 || role == 2) {
                        test.content = "<select name=\"editrole\" id=\"editrole\" class=\"form-control\" required=\"required\"> ' +\n" +
                                "            '<option value=\"Admin\">Admin</option> ' +\n" +
                                "            '<option value=\"User\">User</option> ' +\n" +
                                "            '<option value=\"Staff\">Staff</option> ' +\n" +
                                "            '<option value=\"Teacher\">Teacher</option> ' +\n" +
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
