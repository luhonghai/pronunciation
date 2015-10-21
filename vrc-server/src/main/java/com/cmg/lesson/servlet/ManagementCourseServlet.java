package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.services.course.CourseService;
import com.cmg.lesson.services.level.LevelService;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CMGT400 on 10/8/2015.
 */
@WebServlet(name = "ManagementCourseServlet")
public class ManagementCourseServlet extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CourseService courseService=new CourseService();
        CourseDTO courseDTO=new CourseDTO();
        Gson gson = new Gson();
        try {
            if(request.getParameter("list")!=null){
                int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
                int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
                int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
                String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
                String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
                int column = Integer.parseInt(StringUtil.isNull(request.getParameter("order[0][column]"),"").toString());
                String createDateFrom = (String) StringUtil.isNull(request.getParameter("CreateDateFrom"), "");
                String createDateTo = (String) StringUtil.isNull(request.getParameter("CreateDateTo"),"");
//                courseDTO = courseService.search(start, length, search, column, order, createDateFrom, createDateTo, draw);
//                String json = gson.toJson(courseDTO);
//                response.getWriter().write(json);
            }else if(request.getParameter("add")!=null){
                String level =  (String)StringUtil.isNull(request.getParameter("level"),"");
                String description =  (String)StringUtil.isNull(request.getParameter("description"),"");
                String message = courseService.addCourseToDB(level, description).getMessage();
                response.getWriter().write(message);

            }else if(request.getParameter("edit")!=null){
                String levelId = (String)StringUtil.isNull( request.getParameter("id"),"");
                String level = (String)StringUtil.isNull(request.getParameter("level"),"");
                String description = (String)StringUtil.isNull(request.getParameter("description"),"");
                boolean isUpdateLessonName = Boolean.parseBoolean(request.getParameter("isUpdateLessonName"));
                String message = courseService.updateCourse(levelId, level, description).getMessage();
                response.getWriter().write(message);

            }else if(request.getParameter("delete")!=null){
                String levelId =  (String)StringUtil.isNull(request.getParameter("id"),"");
//                String message = courseService.d(levelId).getMessage();
//                response.getWriter().write(message);
            }
        }catch (Exception e){
            response.getWriter().print("Error : " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
