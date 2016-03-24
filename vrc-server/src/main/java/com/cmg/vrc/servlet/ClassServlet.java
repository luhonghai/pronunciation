package com.cmg.vrc.servlet;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
import com.cmg.vrc.data.dao.impl.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class ClassServlet extends HttpServlet {
    class ListClass{
        private String message;
        private List<ClassJDO> listclass;
    }
    class ListOpenAdd{
        private String message;
        private List<StudentMappingTeacher> studentMappingTeachers;
        private List<Course> courses;
    }
    class ListOpenEdit{
        private String message;
        private List<StudentMappingTeacher> smt;
        private List<StudentMappingTeacher> smtOnClass;
        private List<Course> courses;
        private List<Course> coursesOnClass;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClassDAO classDAO=new ClassDAO();
        ClassMappingTeacherDAO classMappingTeacherDAO=new ClassMappingTeacherDAO();
        StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
        StudentMappingClassDAO studentMappingClassDAO=new StudentMappingClassDAO();
        CourseMappingClassDAO courseMappingClassDAO=new CourseMappingClassDAO();
        CMTDAO cmtdao=new CMTDAO();
        ListClass listClass=new ListClass();
        Gson gson=new Gson();
        ListOpenAdd listOpenAdd=new ListOpenAdd();
        ListOpenEdit listOpenEdit=new ListOpenEdit();
        String action=request.getParameter("action");
        String teacherName=request.getSession().getAttribute("username").toString();
        String teacherID=request.getSession().getAttribute("id").toString();
        if (action.equalsIgnoreCase("listMyClass")) {
            try {
                listClass.message="success";
                listClass.listclass=classDAO.listAll(teacherName);
                String list=gson.toJson(listClass);
                response.getWriter().write(list);
            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("openAdd")){
            try {
                listOpenAdd.message = "success";
                listOpenAdd.studentMappingTeachers = studentMappingTeacherDAO.getListStudentForClass(teacherName);
                listOpenAdd.courses = cmtdao.getMyCourses(teacherID);
                String list = gson.toJson(listOpenAdd);
                response.getWriter().write(list);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("addClass")){
            ClassJDO classs=new ClassJDO();
            String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
            String className=request.getParameter("classname");
            String definition=request.getParameter("definition");
            int version=0;
            String uuid="";

            try {
                classs=classDAO.getClassName(className);
                if(classs==null) {
                    StudentCourse studentCourse = gson.fromJson(jsonClient, StudentCourse.class);
                    uuid = UUIDGenerator.generateUUID();
                    version = classDAO.getLatestVersion() + 1;
                    ClassJDO classJDO = new ClassJDO();
                    ClassMappingTeacher classMappingTeacher = new ClassMappingTeacher();
                    classJDO.setId(uuid);
                    classJDO.setClassName(className);
                    classJDO.setDefinition(definition);
                    classJDO.setCreatedDate(new Date(System.currentTimeMillis()));
                    classJDO.setIsDeleted(false);
                    classJDO.setVersion(version);
                    classDAO.put(classJDO);
                    classMappingTeacher.setIdClass(uuid);
                    classMappingTeacher.setTeacherName(teacherName);
                    classMappingTeacher.setIsDeleted(false);
                    classMappingTeacherDAO.put(classMappingTeacher);
                    String[] listStudent = studentCourse.getStudents();
                    for (String s : listStudent) {
                        StudentMappingClass studentMappingClass = new StudentMappingClass();
                        studentMappingClass.setIdClass(uuid);
                        studentMappingClass.setStudentName(s);
                        studentMappingClass.setCreatedDate(new Date(System.currentTimeMillis()));
                        studentMappingClass.setIsDeleted(false);
                        studentMappingClassDAO.put(studentMappingClass);
                    }
                    String[] listCourse = studentCourse.getCourses();
                    for (String s : listCourse) {
                        CourseMappingClass courseMappingClass = new CourseMappingClass();
                        courseMappingClass.setIdClass(uuid);
                        courseMappingClass.setIdCourse(s);
                        courseMappingClass.setCreatedDate(new Date(System.currentTimeMillis()));
                        courseMappingClass.setIsDeleted(false);
                        courseMappingClassDAO.put(courseMappingClass);
                    }
                    response.getWriter().write("success");
                }else{
                    response.getWriter().write("exist");
                }



            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("openEdit")){
            String idClass=request.getParameter("id");
            try {
                listOpenEdit.message = "success";
                listOpenEdit.smt = classDAO.getStudentByTeacherName(idClass, teacherName);
                listOpenEdit.smtOnClass=classDAO.getStudentByTeacherNameOnClass(idClass, teacherName);
                listOpenEdit.courses = classDAO.getMyCourses(idClass,teacherID);
                listOpenEdit.coursesOnClass=classDAO.getMyCoursesOnClass(idClass,teacherID);
                String list = gson.toJson(listOpenEdit);
                response.getWriter().write(list);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("editClass")){
            String idClass=request.getParameter("id");
            String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
            String className=request.getParameter("classname");
            String definition=request.getParameter("difinition");
            try {
                ClassJDO classJDO=new ClassJDO();
                classJDO=classDAO.getById(idClass);
                StudentCourse studentCourse = gson.fromJson(jsonClient, StudentCourse.class);
                if(classJDO!=null) {
                    classJDO.setDefinition(definition);
                    classDAO.put(classJDO);
                    classDAO.updateCourseMappingClassEdit(idClass);
                    classDAO.updateStudentMappingClassEdit(idClass);
                    String[] listStudent = studentCourse.getStudents();
                    for (String s : listStudent) {
                        StudentMappingClass studentMappingClass = new StudentMappingClass();
                        studentMappingClass.setIdClass(idClass);
                        studentMappingClass.setStudentName(s);
                        studentMappingClass.setCreatedDate(new Date(System.currentTimeMillis()));
                        studentMappingClass.setIsDeleted(false);
                        studentMappingClassDAO.put(studentMappingClass);
                    }
                    String[] listCourse = studentCourse.getCourses();
                    for (String s : listCourse) {
                        CourseMappingClass courseMappingClass = new CourseMappingClass();
                        courseMappingClass.setIdClass(idClass);
                        courseMappingClass.setIdCourse(s);
                        courseMappingClass.setCreatedDate(new Date(System.currentTimeMillis()));
                        courseMappingClass.setIsDeleted(false);
                        courseMappingClassDAO.put(courseMappingClass);
                    }
                    response.getWriter().write("success");

                }else {
                    response.getWriter().write("not exist");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("deleteClass")){
            String idClass=request.getParameter("id");
            try {
                ClassJDO classJDO=new ClassJDO();
                classJDO=classDAO.getById(idClass);
                if(classJDO!=null) {
//                    classJDO.setIsDeleted(true);
//                    classDAO.put(classJDO);
//                    classDAO.updateStudentMappingClassDelete(idClass);
//                    classDAO.updateCourseMappingClassDelete(idClass);
                    classDAO.updateClassDelete(idClass);
                    response.getWriter().write("success");
                }else {
                    response.getWriter().write("error");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            response.getWriter().write("error");
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
