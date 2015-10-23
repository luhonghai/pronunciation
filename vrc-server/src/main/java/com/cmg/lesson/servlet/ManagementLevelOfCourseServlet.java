package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.dto.question.WeightPhonemesDTO;
import com.cmg.lesson.data.dto.word.ListWord;
import com.cmg.lesson.data.dto.word.WordDTO;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.lesson.services.course.CourseMappingLevelService;
import com.cmg.lesson.services.lessons.LessonCollectionService;
import com.cmg.lesson.services.question.WeightForPhonemeService;
import com.cmg.lesson.services.question.WordOfQuestionService;
import com.cmg.lesson.services.word.WordCollectionService;
import com.cmg.lesson.services.word.WordMappingPhonemesService;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CMGT400 on 10/5/2015.
 */
@WebServlet(name = "ManagementLevelOfCourseServlet")
public class ManagementLevelOfCourseServlet extends BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        CourseMappingLevelService courseMappingLevelService=new CourseMappingLevelService();
        CourseMappingLevel courseMappingLevel=new CourseMappingLevel();
        Gson gson = new Gson();
        String action=(String)StringUtil.isNull(request.getParameter("action"),"");
        try {
            if(action.equalsIgnoreCase("listLevel")){
                String idCourse=(String)StringUtil.isNull(request.getParameter("id"),"");
                LevelDTO dto = courseMappingLevelService.getLevels(idCourse);
                 String json = gson.toJson(dto);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("addLevel")){
                String idCourse=(String)StringUtil.isNull(request.getParameter("idCourse"),"");
                String idLevel=(String)StringUtil.isNull(request.getParameter("idLevel"),"");
                CourseDTO dto = courseMappingLevelService.addMappingLevel(idCourse, idLevel);
                String json = gson.toJson(dto);
                response.getWriter().write(json);

            }else if(action.equalsIgnoreCase("getObjAndTest")){
                String idCourse=(String)StringUtil.isNull(request.getParameter("idCourse"),"");
                String idLevel=(String)StringUtil.isNull(request.getParameter("idLevel"),"");
                CourseDTO dto = new CourseDTO();
                dto.setListObj(null);
                dto.setTest(null);
                dto.setMessage("success");
            }else if(action.equalsIgnoreCase("getLessonsForObj")){


            }else if(action.equalsIgnoreCase("getLessonForTest")){


            }else if(action.equalsIgnoreCase("getAllLesson")){
                LessonCollectionService service = new LessonCollectionService();
                LessonCollectionDTO dto = service.getAll();
                String json = gson.toJson(dto);
                response.getWriter().write(json);

            }else if(action.equalsIgnoreCase("delete")){
                String idCourse=(String)StringUtil.isNull(request.getParameter("idCourse"),"");
                String idLevel=(String)StringUtil.isNull(request.getParameter("idLevel"),"");
                CourseDTO dto = courseMappingLevelService.removeLevel(idCourse,idLevel);
                String json = gson.toJson(dto);
                response.getWriter().write(json);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.getWriter().print("error : " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}