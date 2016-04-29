package com.cmg.vrc.servlet;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.dao.impl.StudentMappingTeacherDAO;
import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.service.TranscriptionActionService;
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
 * Created by CMGT400 on 8/5/2015.
 */
public class NotificationServlet extends HttpServlet {
    class SMT{
        List<StudentMappingTeacher> accept;
        List<StudentMappingTeacher> reject;
        List<StudentMappingTeacher> invitation;

        public List<StudentMappingTeacher> getAccept() {
            return accept;
        }

        public void setAccept(List<StudentMappingTeacher> accept) {
            this.accept = accept;
        }

        public List<StudentMappingTeacher> getReject() {
            return reject;
        }

        public void setReject(List<StudentMappingTeacher> reject) {
            this.reject = reject;
        }

        public List<StudentMappingTeacher> getInvitation() {
            return invitation;
        }

        public void setInvitation(List<StudentMappingTeacher> invitation) {
            this.invitation = invitation;
        }
    }


    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
        SMT smt=new SMT();
        String action=request.getParameter("action");
        String teacher=request.getSession().getAttribute("username").toString();
        if(action.equalsIgnoreCase("notification")) {
            try {
                smt.setAccept(studentMappingTeacherDAO.notificationAccept(teacher));
                smt.setReject(studentMappingTeacherDAO.notificationReject(teacher));
                smt.setInvitation(studentMappingTeacherDAO.notificationInvitation(teacher));
                Gson gson=new Gson();
                String list=gson.toJson(smt);
                response.getWriter().write(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("closeAccept")){
            List<StudentMappingTeacher> studentMappingTeachers=studentMappingTeacherDAO.notificationAccept(teacher);
            for(StudentMappingTeacher studentMappingTeacher:studentMappingTeachers){
                StudentMappingTeacher studentMappingTeacher1=new StudentMappingTeacher();
                try {
                    studentMappingTeacher1 = studentMappingTeacherDAO.getById(studentMappingTeacher.getId());
                    studentMappingTeacher1.setIsView(true);
                    studentMappingTeacherDAO.put(studentMappingTeacher1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else if(action.equalsIgnoreCase("closeReject")){
            List<StudentMappingTeacher> studentMappingTeachers=studentMappingTeacherDAO.notificationReject(teacher);
            for(StudentMappingTeacher studentMappingTeacher:studentMappingTeachers){
                StudentMappingTeacher studentMappingTeacher1=new StudentMappingTeacher();
                try {
                    studentMappingTeacher1 = studentMappingTeacherDAO.getById(studentMappingTeacher.getId());
                    studentMappingTeacher1.setIsView(true);
                    studentMappingTeacherDAO.put(studentMappingTeacher1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            List<StudentMappingTeacher> studentMappingTeachers=studentMappingTeacherDAO.notificationInvitation(teacher);
            for(StudentMappingTeacher studentMappingTeacher:studentMappingTeachers){
                StudentMappingTeacher studentMappingTeacher1=new StudentMappingTeacher();
                try {
                    studentMappingTeacher1 = studentMappingTeacherDAO.getById(studentMappingTeacher.getId());
                    studentMappingTeacher1.setIsView(true);
                    studentMappingTeacherDAO.put(studentMappingTeacher1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
