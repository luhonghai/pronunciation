package com.cmg.vrc.servlet;

import com.cmg.lesson.dao.country.CountryDAO;
import com.cmg.lesson.data.jdo.country.Country;
import com.cmg.vrc.data.GcmMessage;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.service.MessageService;
import com.cmg.vrc.util.StringUtil;
import com.google.android.gcm.server.Message;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

/**
 * Created by CMGT400 on 6/3/2015.
 */
public class SendMailUser extends HttpServlet{
    class Mail{
        private String message;
        private List<String> users;
    }
    private class ResponseDataExt extends ResponseData<StudentMappingTeacher> {
        public List<StudentMappingTeacher> studentMappingTeachers;
    }
    private File currentLogFile;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    private void appendLog(String log) {
        if (currentLogFile != null) {
            try {
                logger.info(log);
                FileUtils.writeStringToFile(currentLogFile,
                        sdf.format(new Date(System.currentTimeMillis())) + " " + log + "\n",
                        "UTF-8", true);
            } catch (Exception e) {}
        }
    }
    private void appendMessage(String message) {
        appendLog("INFO - " + message);
    }
    private void appendError(String message) {
        appendError(message, null);
    }

    private void appendError(String message, Throwable e) {
        appendLog("ERROR - " + message);
        if (e != null)
            appendLog(ExceptionUtils.getStackTrace(e));
    }
    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final ResponseDataExt responseData = new ResponseDataExt();
        PrintWriter out = response.getWriter();
        UserDAO userDAO=new UserDAO();
        Mail mails=new Mail();
        String action=request.getParameter("action");
        if(action.equalsIgnoreCase("send")) {
            String jsonClient = (String) StringUtil.isNull(request.getParameter("listmail"), "");
            Gson gson = new Gson();
            List<String> notExist=new ArrayList<>();
            List<String> Exist=new ArrayList<>();
            List<String> send=new ArrayList<>();
            List<User> users=new ArrayList<>();
            try{
                MailToUser mailToUser= gson.fromJson(jsonClient,MailToUser.class);
                String teacher=mailToUser.getTeacher();
                String []listMail=mailToUser.getListmail();
                int n=0;
                for(String mail:listMail){
                    StudentMappingTeacher studentMappingTeacher=new StudentMappingTeacher();
                    StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
                    String mailStudent=mail.trim();
                    studentMappingTeacher=studentMappingTeacherDAO.getByStudentAndTeacher(mailStudent,teacher);
                    User user=new User();
                    user=userDAO.getUserByEmail(mailStudent);
                    if(user!=null){
                        if(studentMappingTeacher==null){
                            StudentMappingTeacher studentMappingTeacher1=new StudentMappingTeacher();
                            studentMappingTeacher1.setStudentName(mailStudent);
                            studentMappingTeacher1.setTeacherName(teacher);
                            studentMappingTeacher1.setIsDeleted(false);
                            studentMappingTeacher1.setStatus("pending");
                            studentMappingTeacherDAO.put(studentMappingTeacher1);
                            send.add(mailStudent);
                            users.add(user);
                        }else{
                            Exist.add(mailStudent);
                            n++;
                        }
                    }else {
                        notExist.add(mailStudent);
                    }
                }
                if(users!=null) {
                    sendGcmMessage(users);
                }
                if(notExist.size()==0){
                    if(n==0) {
                        mails.message = "success";
                        mails.users = new ArrayList<String>();
                    }else {
                        mails.message = "exist";
                        mails.users = Exist;
                    }
                }else{
                    mails.message="notExit";
                    mails.users=notExist;
                }
                String listMails = gson.toJson(mails);
                response.getWriter().write(listMails);

            }catch (Exception e){
                e.getStackTrace();
            }

        }else if(action.equalsIgnoreCase("infoUser")){
            Gson gson=new Gson();
            String username=request.getParameter("username");
            List<StudentMappingTeacher> studentMappingTeachers=new ArrayList<StudentMappingTeacher>();
            StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
            try {
                if(username!="") {
                    studentMappingTeachers = studentMappingTeacherDAO.getByStudent(username);
                    responseData.setStatus(true);
                    responseData.setMessage("success");
                    responseData.studentMappingTeachers = studentMappingTeachers;
                    out.print(gson.toJson(responseData));
                }else {
                    responseData.setStatus(false);
                    responseData.setMessage("error");
                    responseData.studentMappingTeachers = null;
                    out.print(gson.toJson(responseData));
                }
            }catch (Exception e){
                e.getStackTrace();
            }


        }else if(action.equalsIgnoreCase("searchTeacher")) {
            AdminDAO adminDAO=new AdminDAO();
            StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
            StudentMappingTeacher studentMappingTeacher=new StudentMappingTeacher();
            String mailTeacher=request.getParameter("mailTeacher");
            String userName=request.getParameter("username");
            try {
                Admin admin = adminDAO.getUserByTeacher(mailTeacher);
                studentMappingTeacher=studentMappingTeacherDAO.getByStudentAndTeacher(userName,mailTeacher);
                if(admin!=null && studentMappingTeacher==null){
                    studentMappingTeacher.setIsDeleted(false);
                    studentMappingTeacher.setStatus("accept");
                    studentMappingTeacher.setTeacherName(mailTeacher);
                    studentMappingTeacher.setStudentName(userName);
                    studentMappingTeacherDAO.put(studentMappingTeacher);
                    out.print("success");
                }else{
                    out.print("error");
                }
            }catch (Exception e){
                out.print("error");
            }

        }else {
            Gson gson=new Gson();
            String username=request.getParameter("username");
            String status=request.getParameter("status");
            String mailTeacher=request.getParameter("mailTeacher");
            try {
                StudentMappingTeacher studentMappingTeacher=new StudentMappingTeacher();
                StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
                studentMappingTeacher=studentMappingTeacherDAO.getByStudentAndTeacher(username,mailTeacher);
                studentMappingTeacher.setStatus(status);
                studentMappingTeacherDAO.put(studentMappingTeacher);
                out.print("success");
            }catch (Exception e){
                out.print("error");
            }


        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    private void sendGcmMessage(List<User> users) {
        try {
            appendMessage("Send notification to all user devices");
            GcmMessage message = new GcmMessage(GcmMessage.TYPE_DATABASE);
            CountryDAO countryDAO = new CountryDAO();
            UserDeviceDAO userDeviceDAO = new UserDeviceDAO();
            UsageDAO usageDAO=new UsageDAO();
            List<UserDevice> userDevices=new ArrayList<UserDevice>();
            List<Country> countries = countryDAO.listAll();
            if (countries != null && countries.size() > 0) {
                //TODO only add language that have changed data
                for (Country country : countries) {
                    if (!country.isDeleted()) {
                        appendMessage("Add country id to message: " + country.getId() + ". Name: " + country.getName());
                        GcmMessage.Language language = new GcmMessage.Language();
                        language.setId(country.getId());
                        language.setTitle("New accenteasy lessons");
                        language.setMessage("There are new accenteasy lessons waiting for you. Download now.");
                        message.getLanguages().add(language);
                    }
                }
            }
            for(User user:users){
                UserDevice userDevice=new UserDevice();
                Usage usage=new Usage();
                usage=usageDAO.getByUserName(user.getUsername());
                if(usage!=null) {
                    userDevice = userDeviceDAO.getDeviceByIMEI(usage.getImei());
                    if(userDevice!=null){
                        userDevices.add(userDevice);
                    }
                }

            }

            List<String> gcmIds = new ArrayList<>();
            if (userDevices != null && userDevices.size() > 0) {
                for (UserDevice userDevice : userDevices) {
                    String gcmId = userDevice.getGcmId();
                    if (gcmId != null && gcmId.length() > 0 && !gcmIds.contains(gcmId)) {
                        gcmIds.add(gcmId);
                    }
                }
            }
            appendMessage("Send message to " + gcmIds.size() + " device(s)");
            if (gcmIds.size() > 0) {
                Message mMessage = new Message.Builder().addData("data", new Gson().toJson(message)).build();
                MessageService messageService = new MessageService(mMessage);
                messageService.doPostMessage(gcmIds);

            }
        } catch (Exception e) {
            appendError("Could not send notification", e);
        }
    }


}
