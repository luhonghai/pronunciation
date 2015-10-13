package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.services.question.QuestionService;
import com.cmg.vrc.servlet.BaseServlet;
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
public class ManagementQuestionServlet extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuestionService questionService = new QuestionService();
        QuestionDTO questionDTO = new QuestionDTO();
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
                questionDTO = questionService.search(start, length, search, column, order, createDateFrom, createDateTo, draw);
                String json = gson.toJson(questionDTO);
                response.getWriter().write(json);
            }else if(request.getParameter("add")!=null){
                String question =  (String)StringUtil.isNull(request.getParameter("question"),"");
                String message=questionService.addQuestionToDB(question).getMessage();
                response.getWriter().write(message);
            }else if(request.getParameter("edit")!=null){
                String questionId = (String)StringUtil.isNull( request.getParameter("id"),"");
                String question = (String)StringUtil.isNull(request.getParameter("question"),"");
                String message= questionService.updateQuestionToDB(questionId, question).getMessage();
                response.getWriter().write(message);
            }else if(request.getParameter("delete")!=null){
                String questionId =  (String)StringUtil.isNull(request.getParameter("id"),"");
                String message=questionService.deleteQuestionToDB(questionId).getMessage();
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
