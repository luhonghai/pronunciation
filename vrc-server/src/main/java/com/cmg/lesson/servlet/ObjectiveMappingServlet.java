package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.services.lessons.LessonCollectionService;
import com.cmg.lesson.services.objectives.ObjectiveMappingService;
import com.cmg.lesson.services.objectives.ObjectiveService;
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
        String action=(String) StringUtil.isNull(request.getParameter("action"), "");
        try {
            if(action.equalsIgnoreCase("addObj")){
                String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
                ObjectiveMappingDTO dto = gson.fromJson(jsonClient,ObjectiveMappingDTO.class);
                ObjectiveService service = new ObjectiveService();
                ObjectiveMappingDTO dtoToClient = service.addObjective(dto);
                String json = gson.toJson(dtoToClient);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("loadUpdateObj")){
                String idObjective = (String) StringUtil.isNull(request.getParameter("idObjective"), "");
                ObjectiveMappingDTO objectiveMappingDTO = objectiveMappingService.getDataForUpdatePopup(idObjective);
                String json = gson.toJson(objectiveMappingDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("updateObj")){
                String jsonClient = (String) StringUtil.isNull(request.getParameter("objDto"), "");
                ObjectiveMappingDTO dto = gson.fromJson(jsonClient,ObjectiveMappingDTO.class);
                ObjectiveService service = new ObjectiveService();
                ObjectiveMappingDTO dtoToClient = service.updateObjective(dto);
                String json = gson.toJson(dtoToClient);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("deleteObjective")){
                String objectiveId = (String) StringUtil.isNull(request.getParameter("objectiveId"), "");
                ObjectiveService objectiveService = new ObjectiveService();
                ObjectiveDTO objectiveDTO = objectiveService.deleteObjectiveAndLesson(objectiveId);
                String json = gson.toJson(objectiveDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("getLessonsForObj")){
                String idObjective = (String) StringUtil.isNull(request.getParameter("idObj"), "");
                LessonCollectionDTO lessonCollectionDTO = objectiveMappingService.getAllLessonByObjective(idObjective);
                String json = gson.toJson(lessonCollectionDTO);
                response.getWriter().write(json);

            }else if(action.equalsIgnoreCase("getLessonForTest")){


            }else if(action.equalsIgnoreCase("deleteLesson")){
                String lessonId =  (String)StringUtil.isNull(request.getParameter("lessonId"),"");
                String objectiveId =  (String)StringUtil.isNull(request.getParameter("objectiveId"),"");
                ObjectiveMappingDTO objectiveMappingDTO = objectiveMappingService.updateDeleted(objectiveId,lessonId);
                String json = gson.toJson(objectiveMappingDTO);
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
