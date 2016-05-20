package com.cmg.merchant.util;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.vrc.data.GcmMessage;
import com.cmg.vrc.data.dao.impl.ClassDAO;
import com.cmg.vrc.data.dao.impl.UsageDAO;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.jdo.StudentCourse;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.cmg.vrc.data.jdo.Usage;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.service.MessageService;
import com.google.android.gcm.server.Message;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lantb on 2016-05-17.
 */
public class Notification {


    /**
     *
     * @param users
     */
    public void sendNotificationWhenInvite (List<User> users){
        sendNotificationToUser(users,null,GcmMessage.TYPE_INVITE);
    }



    /**
     *
     * @param jsonClient
     */
    public void sendNotificationWhenCreateClass(String jsonClient){
        try {
            Gson gson = new Gson();
            StudentCourse studentCourse = gson.fromJson(jsonClient, StudentCourse.class);
            String[] listStudent = studentCourse.getStudents();
            sendNotificationToUser(swapToUser(listStudent),null,GcmMessage.TYPE_UPDATE_COURSE);
        }catch (Exception e){
        }
    }


    /**
     *
     * @param request
     * @param listCourseDb
     * @param listStudentDb
     * @param jsonClient
     */
    public void sendNotificationWhenUpdateClass(HttpServletRequest request, List<Course> listCourseDb,
                                                List<StudentMappingTeacher> listStudentDb, String jsonClient){
        try {
            Gson gson = new Gson();
            ClassDAO classDAO = new ClassDAO();
            SessionUtil util = new SessionUtil();
            StudentCourse studentCourse = gson.fromJson(jsonClient, StudentCourse.class);
            String[] listStudentClient = studentCourse.getStudents();
            String[] listCourseClient = studentCourse.getCourses();
            //filter data
            sendNotificationToUser(getStudentNew(listStudentDb,listStudentClient),request, GcmMessage.TYPE_UPDATE_COURSE);
            ArrayList<Course> listCourseNew = getCourseNew(listCourseDb,listCourseClient);
            if(listCourseNew!=null && listCourseNew.size() > 0){
                sendNotificationToUser(getStudentOld(listStudentDb,listStudentClient),request,GcmMessage.TYPE_UPDATE_COURSE);
            }
        }catch (Exception e){}
    }

    /**
     *
     * @param listStudentDb
     * @param listStudentClient
     * @return
     */
    public ArrayList<User> getStudentNew(List<StudentMappingTeacher> listStudentDb, String[] listStudentClient){
        if(listStudentDb!=null && listStudentDb.size() > 0){
            ArrayList<String> listStudentAddNew = new ArrayList<>();
            for(String stName : listStudentClient){
                boolean exist = false;
                for(StudentMappingTeacher stm : listStudentDb){
                    if(stm.getStudentName().equals(stName)){
                        exist = true;
                        break;
                    }
                }
                if(!exist) listStudentAddNew.add(stName);
            }
            return swapToUser(listStudentAddNew);
        }else {
            ArrayList<String> list =  new ArrayList<String>(Arrays.asList(listStudentClient));
            return swapToUser(list);
        }
    }

    /**
     *
     * @param listStudentDb
     * @param listStudentClient
     * @return
     */
    public ArrayList<User> getStudentRemove(List<StudentMappingTeacher> listStudentDb, String[] listStudentClient){
        ArrayList<String> listStudentRemove = new ArrayList<>();
        for(StudentMappingTeacher stm : listStudentDb){
            boolean exist = false;
            for(String stName : listStudentClient){
                if(stm.getStudentName().equals(stName)){
                    exist = true;
                    break;
                }
            }
            if(!exist) listStudentRemove.add(stm.getStudentName());
        }
        return swapToUser(listStudentRemove);
    }

    /**
     *
     * @param listStudentDb
     * @param listStudentClient
     * @return
     */
    public ArrayList<User> getStudentOld(List<StudentMappingTeacher> listStudentDb, String[] listStudentClient){
        ArrayList<String> listStudentOld = new ArrayList<>();
        for(StudentMappingTeacher stm : listStudentDb){
            boolean exist = false;
            for(String stName : listStudentClient){
                if(stm.getStudentName().equals(stName)){
                    exist = true;
                    break;
                }
            }
            if(exist) listStudentOld.add(stm.getStudentName());
        }
        return swapToUser(listStudentOld);
    }

    /**
     *
     * @param listCourseDb
     * @param listCourseClient
     * @return
     */
    public ArrayList<Course> getCourseNew(List<Course> listCourseDb, String[] listCourseClient){
        if(listCourseDb!=null && listCourseDb.size() > 0){
            ArrayList<String> listCourseAddNew = new ArrayList<>();
            for(String cId : listCourseClient){
                boolean exist = false;
                for(Course c : listCourseDb){
                    if(c.getId().equals(cId)){
                        exist = true;
                        break;
                    }
                }
                if(!exist) listCourseAddNew.add(cId);
            }
            return swapToCourse(listCourseAddNew);
        }else{
            ArrayList<String> list =  new ArrayList<String>(Arrays.asList(listCourseClient));
            return swapToCourse(list);
        }
    }

    /**
     *
     * @param listCourseDb
     * @param listCourseClient
     * @return
     */
    public ArrayList<Course> getCourseRemove(List<Course> listCourseDb, String[] listCourseClient){
        ArrayList<String> listCourseRemove = new ArrayList<>();
        for(Course c : listCourseDb){
            boolean exist = false;
            for(String cId : listCourseClient){
                if(c.getId().equals(cId)){
                    exist = true;
                    break;
                }
            }
            if(!exist) listCourseRemove.add(c.getId());
        }
        return swapToCourse(listCourseRemove);
    }

    /**
     *
     * @param listCourseDb
     * @param listCourseClient
     * @return
     */
    public ArrayList<Course> getCourseOld(List<Course> listCourseDb, String[] listCourseClient){
        ArrayList<String> listCourseOld = new ArrayList<>();
        for(Course c : listCourseDb){
            boolean exist = false;
            for(String cId : listCourseClient){
                if(c.getId().equals(cId)){
                    exist = true;
                    break;
                }
            }
            if(exist) listCourseOld.add(c.getId());
        }
        return swapToCourse(listCourseOld);
    }

    /**
     *
     * @param listEmail
     * @return
     */
    public ArrayList<User> swapToUser(ArrayList<String> listEmail){
        UserDAO userDAO=new UserDAO();
        try {
            ArrayList<User> list = new ArrayList<>();
            for(String email : listEmail){
                User u = userDAO.getUserByEmail(email);
                if(u!=null) list.add(u);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param listEmail
     * @return
     */
    public ArrayList<User> swapToUser(String[] listEmail){
        UserDAO userDAO=new UserDAO();
        try {
            ArrayList<User> list = new ArrayList<>();
            for(String email : listEmail){
                User u = userDAO.getUserByEmail(email);
                if(u!=null) list.add(u);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param listIdCourse
     * @return
     */
    public ArrayList<Course> swapToCourse(ArrayList<String> listIdCourse){
        CDAO dao = new CDAO();
        try {
            ArrayList<Course> list = new ArrayList<>();
            for(String id : listIdCourse){
                Course c = dao.getById(id);
                if(c!=null) list.add(c);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param listStudent
     * @param request
     */
    public void sendNotificationToUser(List<User> listStudent, HttpServletRequest request, int type){
        try {
            GcmMessage message = new GcmMessage(type);
            UserDeviceDAO userDeviceDAO = new UserDeviceDAO();
            UsageDAO usageDAO=new UsageDAO();
            for(User user : listStudent){
                List<String> gcmIds = new ArrayList<>();
                Usage usage=usageDAO.getByUserName(user.getUsername());
                if(usage!=null) {
                    gcmIds.addAll(userDeviceDAO.getListGCMID(usage.getUsername()));
                    message.setTitle("New accenteasy message");
                    message.setUsername(user.getUsername());
                    if (gcmIds.size() > 0) {
                        Message mMessage = new Message.Builder().addData("data", new Gson().toJson(message)).notification(new com.google.android.gcm.server.Notification.Builder("").title(message.getTitle()).body(message.getContent()).build()).priority(Message.Priority.HIGH).build();
                        MessageService messageService = new MessageService(mMessage);
                        messageService.doPostMessage(gcmIds);
                    }
                }
            }
        } catch (Exception e) {
        }
    }




}
