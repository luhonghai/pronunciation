package com.cmg.vrc.service;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.util.SessionUtil;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 3/25/2016.
 */
public class ClassService {
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

    public String listClass(String teacherName){
        String list=null;
        try {
            listClass.message = "success";
            listClass.listclass = classDAO.listAll(teacherName);
            list= gson.toJson(listClass);
        }catch (Exception e){
            listClass.message = "error";
            listClass.listclass = new ArrayList<>();
            list= gson.toJson(listClass);
        }
        return list;
    }
    public String openAddClass(String teacherName,String idTeacher,String idCompany){
        String list=null;
        try {
            listOpenAdd.message = "success";
            listOpenAdd.studentMappingTeachers = studentMappingTeacherDAO.getListStudentForClass(teacherName);
            listOpenAdd.courses = cmtdao.getMyCourses(idTeacher, idCompany, Constant.STATUS_PUBLISH);
            list = gson.toJson(listOpenAdd);
        }catch (Exception e){
            listOpenAdd.message = "error";
            listOpenAdd.studentMappingTeachers = new ArrayList<>();
            listOpenAdd.courses = new ArrayList<>();
            list = gson.toJson(listOpenAdd);
        }
        return list;
    }
    public String addClassToDb(String teacherName,String className,String definition,String jsonClient){
        ClassJDO classs=new ClassJDO();
        int version=0;
        String uuid="";
        String message=null;
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
                message= "success";
            }else{
                message= "exist";
            }



        }catch (Exception e){
            message="error";
            e.printStackTrace();
        }
        return message;
    }
    public String openEditClass(String teacherName,String idClass,String teacherID,String idCompany){
        String list=null;
        try {
            listOpenEdit.message = "success";
            listOpenEdit.smt = classDAO.getStudentByTeacherName(idClass, teacherName);
            listOpenEdit.smtOnClass=classDAO.getStudentByTeacherNameOnClass(idClass, teacherName);
            listOpenEdit.courses = classDAO.getMyCourses(idClass, teacherID,idCompany);
            listOpenEdit.coursesOnClass=classDAO.getMyCoursesOnClass(idClass, teacherID,Constant.STATUS_PUBLISH);
            list = gson.toJson(listOpenEdit);
        }catch (Exception e){
            listOpenEdit.message = "error";
            listOpenEdit.smt =new ArrayList<>();
            listOpenEdit.smtOnClass=new ArrayList<>();
            listOpenEdit.courses =new ArrayList<>();
            listOpenEdit.coursesOnClass=new ArrayList<>();
            list = gson.toJson(listOpenEdit);
        }
        return list;
    }

    public String editClassToDb(String idClass,String definition,String jsonClient){
        String message=null;
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
                message="success";

            }else {
                message="not exist";
            }

        }catch (Exception e){
            message="error";
            e.printStackTrace();
        }
        return message;
    }
    public String deleteClass(String idClass){
        String message=null;
        try {
            ClassJDO classJDO=new ClassJDO();
            classJDO=classDAO.getById(idClass);
            if(classJDO!=null) {
                classDAO.updateClassDelete(idClass);
                message="success";
            }else {
                message="not exist";
            }

        }catch (Exception e){
            message="error";
            e.printStackTrace();
        }
        return message;
    }
}
