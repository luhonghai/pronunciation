package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.dto.lessons.LessonMappingQuestionDTO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.services.lessons.LessonCollectionService;
import com.cmg.lesson.services.lessons.LessonMappingQuestionService;
import com.cmg.lesson.services.question.QuestionService;
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
@WebServlet(name = "ManagementLessonsServlet")
public class ManagementLessonsServlet extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        LessonCollectionService lessonCollectionService = new LessonCollectionService();
        LessonMappingQuestionService lessonMappingQuestionService = new LessonMappingQuestionService();
        QuestionService questionService = new QuestionService();
        LessonCollectionDTO lessonCollectionDTO;
        LessonMappingQuestionDTO lessonMappingQuestionDTO;
        QuestionDTO questionDTO;
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
                lessonCollectionDTO = lessonCollectionService.search(start, length, search, column, order, createDateFrom, createDateTo, draw);
                String json = gson.toJson(lessonCollectionDTO);
                response.getWriter().write(json);
            }else if(request.getParameter("add")!=null){
                String lesson =  (String)StringUtil.isNull(request.getParameter("lesson"),"");
                String title =  (String)StringUtil.isNull(request.getParameter("title"),"");
                String shortDescription =  (String)StringUtil.isNull(request.getParameter("shortDescription"),"");
                String description =  (String)StringUtil.isNull(request.getParameter("description"),"");
                String message = lessonCollectionService.addLesson(lesson,title,shortDescription,description).getMessage();
                response.getWriter().write(message);

            }else if(request.getParameter("edit")!=null){
                String lessonId = (String)StringUtil.isNull( request.getParameter("id"),"");
                String lesson = (String)StringUtil.isNull(request.getParameter("lesson"),"");
                String title =  (String)StringUtil.isNull(request.getParameter("title"),"");
                String shortDescription =  (String)StringUtil.isNull(request.getParameter("shortDescription"),"");
                String description = (String)StringUtil.isNull(request.getParameter("description"),"");
                boolean isUpdateLessonName = Boolean.parseBoolean(request.getParameter("isUpdateLessonName"));
                String message = lessonCollectionService.updateLesson(lessonId,lesson,title,shortDescription,description, isUpdateLessonName).getMessage();
                response.getWriter().write(message);

            }else if(request.getParameter("delete")!=null){
                String lessonId =  (String)StringUtil.isNull(request.getParameter("id"),"");
                String message = lessonCollectionService.deleteLesson(lessonId).getMessage();
                response.getWriter().write(message);
            }else if(request.getParameter("listQuestionOfLesson")!=null){
                String lessonId =  (String)StringUtil.isNull(request.getParameter("idLesson"),"");
                //questionDTO = lessonMappingQuestionService.listQuestionByIdLesson(lessonId,"");
                //String json = gson.toJson(questionDTO);
                //response.getWriter().write(json);
                //questionService.
                //response.getWriter().write(message);
            }
        }catch (Exception e){
            response.getWriter().print("Error : " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
