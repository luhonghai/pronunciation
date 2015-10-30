package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.data.dto.test.TestDTO;
import com.cmg.lesson.data.dto.test.TestMappingDTO;
import com.cmg.lesson.services.lessons.LessonCollectionService;
import com.cmg.lesson.services.objectives.ObjectiveMappingService;
import com.cmg.lesson.services.objectives.ObjectiveService;
import com.cmg.lesson.services.test.TestMappingService;
import com.cmg.lesson.services.test.TestService;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by CMG Dev156 on 10/23/2015.
 */
@WebServlet(name = "ObjectiveMappingServlet")
public class ObjectiveMappingServlet extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        Gson gson = new Gson();
        ObjectiveMappingService objectiveMappingService = new ObjectiveMappingService();
        TestMappingService testMappingService = new TestMappingService();
        String action=(String) StringUtil.isNull(request.getParameter("action"), "");
        try {
            if(action.equalsIgnoreCase("addObj")){
                String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
                ObjectiveMappingDTO dto = gson.fromJson(jsonClient,ObjectiveMappingDTO.class);
                ObjectiveService service = new ObjectiveService();
                ObjectiveMappingDTO dtoToClient = service.addObjective(dto);
                String json = gson.toJson(dtoToClient);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("addTest")){
                String jsonClient = (String) StringUtil.isNull(request.getParameter("testDto"), "");
                TestMappingDTO dto = gson.fromJson(jsonClient,TestMappingDTO.class);
                TestService service = new TestService();
                TestMappingDTO dtoToClient = service.addTest(dto);
                String json = gson.toJson(dtoToClient);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("loadUpdateObj")){
                String idObjective = (String) StringUtil.isNull(request.getParameter("idObjective"), "");
                ObjectiveMappingDTO objectiveMappingDTO = objectiveMappingService.getDataForUpdatePopup(idObjective);
                String json = gson.toJson(objectiveMappingDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("loadUpdateTest")){
                String idTest = (String) StringUtil.isNull(request.getParameter("idTest"), "");
                TestMappingDTO testMappingDTO = testMappingService.getDataForUpdatePopup(idTest);
                String json = gson.toJson(testMappingDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("updateObj")){
                String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
                ObjectiveMappingDTO dto = gson.fromJson(jsonClient,ObjectiveMappingDTO.class);
                ObjectiveService service = new ObjectiveService();
                ObjectiveMappingDTO dtoToClient = service.updateObjective(dto);
                String json = gson.toJson(dtoToClient);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("updateTest")){
                String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
                TestMappingDTO dto = gson.fromJson(jsonClient,TestMappingDTO.class);
                TestService service = new TestService();
                TestMappingDTO dtoToClient = service.updateTest(dto);
                String json = gson.toJson(dtoToClient);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("deleteObjective")){
                String objectiveId = (String) StringUtil.isNull(request.getParameter("objectiveId"), "");
                ObjectiveService objectiveService = new ObjectiveService();
                ObjectiveDTO objectiveDTO = objectiveService.deleteObjectiveAndLesson(objectiveId);
                String json = gson.toJson(objectiveDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("deleteTest")){
                String testId = (String) StringUtil.isNull(request.getParameter("testId"), "");
                TestService testService = new TestService();
                TestDTO testDTO = testService.deleteTestAndLesson(testId);
                String json = gson.toJson(testDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("getLessonsForObj")){
                String idObjective = (String) StringUtil.isNull(request.getParameter("idObj"), "");
                LessonCollectionDTO lessonCollectionDTO = objectiveMappingService.getAllLessonByObjective(idObjective);
                String json = gson.toJson(lessonCollectionDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("getLessonForTest")){
                String idTest = (String) StringUtil.isNull(request.getParameter("idTest"), "");
                LessonCollectionDTO lessonCollectionDTO = testMappingService.getAllLessonByTest(idTest);
                String json = gson.toJson(lessonCollectionDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("deleteLesson")){
                String lessonId =  (String)StringUtil.isNull(request.getParameter("lessonId"),"");
                String objectiveId =  (String)StringUtil.isNull(request.getParameter("objectiveId"),"");
                ObjectiveMappingDTO objectiveMappingDTO = objectiveMappingService.updateDeleted(objectiveId,lessonId);
                String json = gson.toJson(objectiveMappingDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("deleteLessonForTest")){
                String lessonId =  (String)StringUtil.isNull(request.getParameter("lessonId"),"");
                String testId =  (String)StringUtil.isNull(request.getParameter("testId"),"");
                TestMappingDTO testMappingDTO = testMappingService.updateDeleted(testId,lessonId);
                String json = gson.toJson(testMappingDTO);
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
