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

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO adminDAO = new AdminDAO();
        TeacherMappingCompanyDAO teacherMappingCompanyDAO=new TeacherMappingCompanyDAO();
        StaffMappingCompanyDAO staffMappingCompanyDAO=new StaffMappingCompanyDAO();
        Admin ad = new Admin();
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
            String id = request.getParameter("id");
            try {
                adminDAO.delete(id);
                response.getWriter().write("success");
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
            String uuid=null;
            String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
            Gson gson = new Gson();
            TeacherMappingCompany teacherMappingCompany=new TeacherMappingCompany();
            StaffMappingCompany staffMappingCompany=new StaffMappingCompany();
            try{
                uuid = UUIDGenerator.generateUUID();
                TeacherOrStaffMappingCompany dto = gson.fromJson(jsonClient, TeacherOrStaffMappingCompany.class);
                String role=dto.getRole();
                int ro = 0;
                if (role.length() > 0 && role.equals("Staff")) {
                    ro = 3;
                }
                if (role.length() > 0 && role.equals("Teacher")) {
                    ro = 4;
                }
                ad.setFirstName(dto.getFirstName());
                ad.setUserName(dto.getFullName());
                ad.setLastName(dto.getLastName());
                ad.setPassword(dto.getPassword());
                ad.setRole(ro);
                ad.setId(uuid);
                adminDAO.put(ad);
                String[] Company=dto.getIdObjects();
                if(ro==3){
                    for(String company:Company){
                        staffMappingCompany.setIsDeleted(false);
                        staffMappingCompany.setIdCompany(company);
                        staffMappingCompany.setIdStaff(uuid);
                        staffMappingCompanyDAO.put(staffMappingCompany);
                    }
                }else{
                    for(String company:Company){
                        teacherMappingCompany.setIsDeleted(false);
                        teacherMappingCompany.setIdCompany(company);
                        teacherMappingCompany.setIdTeacher(uuid);
                        teacherMappingCompanyDAO.put(teacherMappingCompany);
                    }
                }

                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
