package com.cmg.vrc.servlet;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.StudentMappingTeacherDAO;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.cmg.vrc.data.jdo.StudentMappingTeacherClient;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmgvn on 4/27/16.
 */
@WebServlet(name = "InvitationServlet", urlPatterns = {"/InvitationServlet"})
public class InvitationServlet extends HttpServlet {

    public static class InvitationData {
        public String id;
        public String studentName;
        public String teacherName;
        public String firstTeacherName;
        public String lastTeacherName;
        public String companyName;
        public String status;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String action = request.getParameter("action");
        String profile = request.getParameter("profile");
        ResponseData<List<InvitationData>> responseData = new ResponseData<>();
        try {
            StudentMappingTeacherDAO teacherDAO = new StudentMappingTeacherDAO();
            if (action != null && action.length() > 0 && profile != null && profile.length() > 0) {
                UserProfile userProfile = gson.fromJson(profile, UserProfile.class);
                log(action + " invitation of user " + userProfile.getUsername());
                if (action.equalsIgnoreCase("getdata")) {
                    List<StudentMappingTeacherClient> mappingTeacherClients = teacherDAO.getStudentMappingTeaccher(userProfile.getUsername());
                    List<InvitationData> invitationDatas = new ArrayList<InvitationData>();
                    if (mappingTeacherClients  != null && mappingTeacherClients.size() > 0) {
                        log("found mapping count " + mappingTeacherClients.size());
                        for (StudentMappingTeacherClient studentMappingTeacherClient : mappingTeacherClients) {
                            InvitationData invitationData = new InvitationData();
                            invitationData.id = studentMappingTeacherClient.getId();
                            invitationData.companyName = studentMappingTeacherClient.getCompany();
                            invitationData.firstTeacherName = studentMappingTeacherClient.getFirstNameTeacher();
                            invitationData.lastTeacherName = studentMappingTeacherClient.getLastNameTeacher();
                            invitationData.status = studentMappingTeacherClient.getStatus();
                            invitationData.studentName = studentMappingTeacherClient.getStudentName();
                            invitationData.teacherName = studentMappingTeacherClient.getTeacherName();
                            invitationDatas.add(invitationData);
                        }
                    }
                    responseData.setData(invitationDatas);
                    responseData.setStatus(true);
                    responseData.setMessage("success");
                } else if (action.equalsIgnoreCase("updatereject")){
                    StudentMappingTeacher studentMappingTeacher = teacherDAO.getById(request.getParameter("id"));
                    studentMappingTeacher.setStatus("reject");
                    teacherDAO.update(studentMappingTeacher);
                    responseData.setStatus(true);
                    responseData.setMessage("success");
                } else if (action.equalsIgnoreCase("updateaccept")){
                    StudentMappingTeacher studentMappingTeacher = teacherDAO.getById(request.getParameter("id"));
                    studentMappingTeacher.setStatus("accept");
                    teacherDAO.update(studentMappingTeacher);
                    responseData.setStatus(true);
                    responseData.setMessage("success");
                } else if (action.equalsIgnoreCase("updateDeleteData")) {
                    StudentMappingTeacher studentMappingTeacher = teacherDAO.getById(request.getParameter("id"));
                    studentMappingTeacher.setStatus("deleted");
                    studentMappingTeacher.setIsDeleted(true);
                    teacherDAO.update(studentMappingTeacher);
                    responseData.setStatus(true);
                    responseData.setMessage("success");
                } else {
                    responseData.setMessage("no action found");
                }
            } else {
                responseData.setMessage("no action found");
            }
        } catch (Exception e) {
            log("could not fetch invitation", e);
            e.printStackTrace();
            responseData.setMessage("error: " + e.getMessage());
        }
        out.print(gson.toJson(responseData));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
