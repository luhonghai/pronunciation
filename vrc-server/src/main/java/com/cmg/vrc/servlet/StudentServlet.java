package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.ClassDAO;
import com.cmg.vrc.data.dao.impl.StudentMappingClassDAO;
import com.cmg.vrc.data.dao.impl.StudentMappingTeacherDAO;
import com.cmg.vrc.data.jdo.ClassJDO;
import com.cmg.vrc.data.jdo.Student;
import com.cmg.vrc.data.jdo.StudentMappingClass;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class StudentServlet extends HttpServlet {
    class admin{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<StudentMappingClass> data;
    }
    class stusentTeacher{
        public String message;
        List<StudentMappingTeacher> studentMappingTeachers;
    }

    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentMappingClassDAO studentMappingClassDAO=new StudentMappingClassDAO();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        admin admin=new admin();
        if (request.getParameter("list") != null) {
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
            String idClass=request.getParameter("idClass");
            String student=request.getParameter("student");
            String dateFrom =(String) StringUtil.isNull(request.getParameter("CreateDateFrom"), "");
            String dateTo =(String) StringUtil.isNull(request.getParameter("CreateDateTo"), "");
            Date dateFrom1=null;
            Date dateTo1=null;


            if(dateFrom.length()>0){
                try {
                    dateFrom1=df.parse(dateFrom);

                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            if(dateTo.length()>0){
                try {
                    dateTo1=df.parse(dateTo);

                }catch (Exception e){
                    e.getStackTrace();
                }
            }


            Double count;

            try {
                if(search.length()>0||student.length()>0 || idClass.length()>0 || dateFrom1!=null||dateTo1!=null){
                    count=studentMappingClassDAO.getCountSearch(search,idClass,student,dateFrom1,dateTo1);
                }else {
                    count = studentMappingClassDAO.getCount();
                }
                admin.data=studentMappingClassDAO.listAll(start,length,search,col,oder,idClass,student,dateFrom1,dateTo1);
                admin.draw = draw;
                admin.recordsTotal = count;
                admin.recordsFiltered = count;
                Gson gson = new Gson();
                String admins = gson.toJson(admin);
                response.getWriter().write(admins);

            } catch (Exception e) {
                response.getWriter().write("error");
                e.printStackTrace();
            }






        }
        if(request.getParameter("listStudent")!=null){
            StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
            stusentTeacher stusentTeacher=new stusentTeacher();
            String teacherName=request.getParameter("teacherName");
            String idClass=request.getParameter("idClass");
            try{
                stusentTeacher.studentMappingTeachers=studentMappingTeacherDAO.getStudentByTeacherName(idClass,teacherName);
                stusentTeacher.message="success";
                Gson gson = new Gson();
                String listStudent = gson.toJson(stusentTeacher);
                response.getWriter().write(listStudent);
            }catch (Exception e){
                stusentTeacher.studentMappingTeachers=new ArrayList<StudentMappingTeacher>();
                stusentTeacher.message="error";
                Gson gson = new Gson();
                String listStudent = gson.toJson(stusentTeacher);
                response.getWriter().write(listStudent);
            }
        }

        if(request.getParameter("add")!=null){
            String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
            Gson gson = new Gson();

            try{
                Student student= gson.fromJson(jsonClient, Student.class);
                String idClass=student.getIdClass();
                String[] listStudent=student.getIdObjects();
                for(String s:listStudent){
                    StudentMappingClass studentMappingClass=new StudentMappingClass();
                    studentMappingClass.setIdClass(idClass);
                    studentMappingClass.setStudentName(s);
                    studentMappingClass.setCreatedDate(new Date(System.currentTimeMillis()));
                    studentMappingClass.setIsDeleted(false);
                    studentMappingClassDAO.put(studentMappingClass);
                }
                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }

        }


        if(request.getParameter("delete")!=null){
            String idClass=request.getParameter("idClass");
            String studentName=request.getParameter("studentName");
            try {
              //  StudentMappingClass studentMappingClass=new StudentMappingClass();
                StudentMappingClass studentMappingClass= studentMappingClassDAO.getByClassAndStudent(idClass,studentName);
                studentMappingClass.setIsDeleted(true);
                studentMappingClassDAO.put(studentMappingClass);
                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
