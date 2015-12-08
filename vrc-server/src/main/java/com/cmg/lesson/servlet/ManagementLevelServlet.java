package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.services.lessons.LessonCollectionService;
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
@WebServlet(name = "ManagementLevelServlet")
public class ManagementLevelServlet extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LevelService levelService=new LevelService();
        LevelDTO levelDTO=new LevelDTO();
        Gson gson = new Gson();
        try {
            if(request.getParameter("list")!=null){
                int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
                int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
                int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
                String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
                String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
                int column = Integer.parseInt(StringUtil.isNull(request.getParameter("order[0][column]"),"").toString());
                String description = (String) StringUtil.isNull(request.getParameter("description"), "");
                String createDateFrom = (String) StringUtil.isNull(request.getParameter("CreateDateFrom"), "");
                String createDateTo = (String) StringUtil.isNull(request.getParameter("CreateDateTo"),"");
                levelDTO = levelService.search(start, length, search, column, order,description, createDateFrom, createDateTo, draw);
                String json = gson.toJson(levelDTO);
                response.getWriter().write(json);
            }else if(request.getParameter("add")!=null){
                String level =  (String)StringUtil.isNull(request.getParameter("level"),"");
                String description =  (String)StringUtil.isNull(request.getParameter("description"),"");
                String color =  (String)StringUtil.isNull(request.getParameter("color"),"");
                boolean isDemo =  Boolean.parseBoolean(StringUtil.isNull(request.getParameter("isDemo"), "").toString());
                String message = levelService.addLevelToDB(level, description,color,isDemo).getMessage();
                response.getWriter().write(message);

            }else if(request.getParameter("edit")!=null){
                String levelId = (String)StringUtil.isNull( request.getParameter("id"),"");
                String level = (String)StringUtil.isNull(request.getParameter("level"),"");
                String description = (String)StringUtil.isNull(request.getParameter("description"),"");
                boolean isUpdateLessonName = Boolean.parseBoolean(request.getParameter("isUpdateLessonName"));
                String color =  (String)StringUtil.isNull(request.getParameter("color"),"");
                boolean isDemo =  Boolean.parseBoolean(StringUtil.isNull(request.getParameter("isDemo"), "").toString());
                String message = levelService.updateLevel(levelId, level, description,color,isDemo,isUpdateLessonName).getMessage();
                response.getWriter().write(message);

            }else if(request.getParameter("delete")!=null){
                String levelId =  (String)StringUtil.isNull(request.getParameter("id"),"");
                String message = levelService.deleteLevelToDB(levelId).getMessage();
                response.getWriter().write(message);
            }
        }catch (Exception e){
            response.getWriter().print("Error : " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
