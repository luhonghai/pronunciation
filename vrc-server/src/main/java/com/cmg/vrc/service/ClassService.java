package com.cmg.vrc.service;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.dao.report.ReportLessonDAO;
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
    public class ListClass{
        private String message;
        private List<ClassJDO> list;

        public List<ClassJDO> getList() {
            return list;
        }

        public void setList(List<ClassJDO> list) {
            this.list = list;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    public class ListOpenAdd{
        private String message;
        private List<StudentMappingTeacher> studentMappingTeachers;
        private List<Course> courses;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<StudentMappingTeacher> getStudentMappingTeachers() {
            return studentMappingTeachers;
        }

        public void setStudentMappingTeachers(List<StudentMappingTeacher> studentMappingTeachers) {
            this.studentMappingTeachers = studentMappingTeachers;
        }

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }
    }
    public class ListOpenEdit{
        private String message;
        private List<StudentMappingTeacher> smt;
        private List<StudentMappingTeacher> smtOnClass;
        private List<Course> courses;
        private List<Course> coursesOnClass;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<StudentMappingTeacher> getSmt() {
            return smt;
        }

        public void setSmt(List<StudentMappingTeacher> smt) {
            this.smt = smt;
        }

        public List<StudentMappingTeacher> getSmtOnClass() {
            return smtOnClass;
        }

        public void setSmtOnClass(List<StudentMappingTeacher> smtOnClass) {
            this.smtOnClass = smtOnClass;
        }

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        public List<Course> getCoursesOnClass() {
            return coursesOnClass;
        }

        public void setCoursesOnClass(List<Course> coursesOnClass) {
            this.coursesOnClass = coursesOnClass;
        }
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
            ListClass lc = new ListClass();
            lc.setMessage("success");
            lc.setList(classDAO.listAll(teacherName));
            list= gson.toJson(lc);
        }catch (Exception e){
            ClassService.ListClass lc = new ClassService.ListClass();
            lc.setMessage("error");
            lc.setList(new ArrayList<ClassJDO>());
            list= gson.toJson(lc);
        }
        return list;
    }
    public String openAddClass(String teacherName,String idTeacher,String idCompany){
        String list=null;
        try {
            ListOpenAdd loa = new ListOpenAdd();
            loa.setMessage("success");
            loa.setStudentMappingTeachers(studentMappingTeacherDAO.getListStudentForClass(teacherName));
            loa.setCourses(cmtdao.getMyCourses(idTeacher, idCompany, Constant.STATUS_PUBLISH));
            list = gson.toJson(loa);
        }catch (Exception e){
            ListOpenAdd loa = new ListOpenAdd();
            loa.setMessage("error");
            loa.setStudentMappingTeachers(new ArrayList<StudentMappingTeacher>());
            loa.setCourses(new ArrayList<Course>());
            list = gson.toJson(loa);
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
            if(!checkNameExisted(teacherName,null,className)) {
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
            ListOpenEdit loe = new ListOpenEdit();
            loe.setMessage("success");
            loe.setCoursesOnClass(classDAO.getMyCoursesOnClass(idClass, teacherID, Constant.STATUS_PUBLISH));
            loe.setSmt(classDAO.getStudentByTeacherName(idClass, teacherName));
            loe.setSmtOnClass(classDAO.getStudentByTeacherNameOnClass(idClass, teacherName));
            loe.setCourses(classDAO.getMyCourses(idClass, teacherID,idCompany,Constant.STATUS_PUBLISH));
            list = gson.toJson(loe);
        }catch (Exception e){
            ListOpenEdit loe = new ListOpenEdit();
            loe.setMessage("error");
            loe.setCoursesOnClass(null);
            loe.setSmt(null);
            loe.setSmtOnClass(null);
            loe.setCourses(null);
            list = gson.toJson(loe);
        }
        return list;
    }

    /**
     *
     * @param idClass
     * @param nameClass
     * @return
     */
    public boolean checkNameExisted(String teacherName,String idClass, String nameClass){
        ReportLessonDAO dao = new ReportLessonDAO();
        try {
            List<ClassJDO> list = dao.getClassByTeacher(teacherName);
            if(list!=null && list.size() > 0){
                if(idClass!=null){
                    for(ClassJDO c : list){
                        if(c.getClassName().equalsIgnoreCase(nameClass) && !c.getId().equalsIgnoreCase(idClass)){
                            return true;
                        }
                    }
                }else{
                    for(ClassJDO c : list){
                        if(c.getClassName().equalsIgnoreCase(nameClass)){
                            return true;
                        }
                    }
                }
            }
        }catch (Exception e){
        }
        return false;
    }

    public String editClassToDb(String teacherName, String idClass,String nameClass,String definition,String jsonClient){
        String message=null;
        try {
            ClassJDO classJDO=new ClassJDO();
            classJDO=classDAO.getById(idClass);
            StudentCourse studentCourse = gson.fromJson(jsonClient, StudentCourse.class);
            if(classJDO!=null) {
                if(!checkNameExisted(teacherName,idClass,nameClass)){
                    classJDO.setClassName(nameClass);
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
                    message = "success";
                }else{
                    message = "name existed";
                }
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
            ClassJDO classJDO = classDAO.getById(idClass);
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
