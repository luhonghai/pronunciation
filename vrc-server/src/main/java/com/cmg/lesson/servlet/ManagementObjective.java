package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.dto.level.LevelMappingObjDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.data.dto.test.TestDTO;
import com.cmg.lesson.data.dto.test.TestMappingDTO;
import com.cmg.lesson.services.objectives.ObjectiveMappingService;
import com.cmg.lesson.services.objectives.ObjectiveService;
import com.cmg.lesson.services.test.TestMappingService;
import com.cmg.lesson.services.test.TestService;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lantb on 2016-01-04.
 */
@WebServlet(name = "ManagementObjective")
public class ManagementObjective extends BaseServlet {

    private String LIST_ALL = "listall";
    private String ADD_OBJ = "addobj";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        Gson gson = new Gson();
        ObjectiveService service = new ObjectiveService();
        try {
            String action = (String)StringUtil.isNull(request.getParameter("action"),"");
            if(action.equalsIgnoreCase(LIST_ALL)){
                int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
                int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
                int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
                String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
                String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
                int column = Integer.parseInt(StringUtil.isNull(request.getParameter("order[0][column]"),"").toString());
                String description = (String) StringUtil.isNull(request.getParameter("description"), "");
                String createDateFrom = (String) StringUtil.isNull(request.getParameter("CreateDateFrom"), "");
                String createDateTo = (String) StringUtil.isNull(request.getParameter("CreateDateTo"),"");
                ObjectiveDTO dto = service.search(start,length,search,column,order,description,createDateFrom,createDateTo,draw);
                String json = gson.toJson(dto);
                response.getWriter().println(json);
            }else if(action.equalsIgnoreCase(ADD_OBJ)){

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
