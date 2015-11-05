package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.ipa.IpaMapDTO;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.services.ipa.IpaMapArpabetService;
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
 * Created by lantb on 2015-10-27.
 */
@WebServlet(name = "ManageIpaMapArpabetServlet")
public class ManageIpaMapArpabetServlet extends BaseServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String action= (String)StringUtil.isNull(request.getParameter("action"),"");
        Gson gson = new Gson();
        IpaMapArpabetService service = new IpaMapArpabetService();
        try {
            if(action.equalsIgnoreCase("list")){
                int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
                int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
                int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
                String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
                String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
                int column = Integer.parseInt(StringUtil.isNull(request.getParameter("order[0][column]"),"").toString());
                String createDateFrom = (String) StringUtil.isNull(request.getParameter("CreateDateFrom"), "");
                String createDateTo = (String) StringUtil.isNull(request.getParameter("CreateDateTo"),"");
                IpaMapDTO dto = service.search(start,length,search,column,order,createDateFrom,createDateTo,draw);
                String json = gson.toJson(dto);
                System.out.println("json form list : " + json);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("add")){
                String clientData = (String) StringUtil.isNull(request.getParameter("dto"),"");
                IpaMapArpabet map = gson.fromJson(clientData,IpaMapArpabet.class);
                IpaMapDTO dto = service.addMapping(map);
                String json = gson.toJson(dto);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("edit")){
                String clientData = (String) StringUtil.isNull(request.getParameter("dto"),"");
                IpaMapArpabet map = gson.fromJson(clientData,IpaMapArpabet.class);
                IpaMapDTO dto = service.update(map);
                String json = gson.toJson(dto);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("getById")){
                String id = (String)StringUtil.isNull(request.getParameter("id"),"");
                IpaMapArpabet map = service.getById(id);
                String json = gson.toJson(map);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("delete")){
                String id = (String)StringUtil.isNull(request.getParameter("id"),"");
                IpaMapDTO dto = service.delete(id);
                String json = gson.toJson(dto);
                response.getWriter().write(json);
            }
        }catch (Exception e){
            response.getWriter().print("Error : " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
