package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.QuestionDTO;
import com.cmg.lesson.services.QuestionService;
import com.cmg.vrc.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

/**
 * Created by CMGT400 on 10/8/2015.
 */
@WebServlet(name = "ManagementQuestionServlet")
public class ManagementQuestionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuestionService questionService = new QuestionService();
        QuestionDTO questionDTO = new QuestionDTO();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        if(request.getParameter("list")!=null){
            int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
            int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
            int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
            String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
            String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
            int column = Integer.parseInt(StringUtil.isNull(request.getParameter("order[0][column]"),"").toString());
            Date createDateFrom=null;
            if(StringUtil.isNull(request.getParameter("CreateDateFrom"),"").toString() != ""){
                try {
                    createDateFrom = df.parse(StringUtil.isNull(request.getParameter("CreateDateFrom"),"").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date createDateTo=null;
            if(StringUtil.isNull(request.getParameter("createDateTo"),"").toString() != ""){
                try {
                    createDateFrom = df.parse(StringUtil.isNull(request.getParameter("createDateTo"),"").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            questionDTO = questionService.search(start, length, search, column, order, createDateFrom, createDateTo, draw);
            try {
                Gson gson = new Gson();
                String json = gson.toJson(questionDTO);
                response.getWriter().write(json);
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
