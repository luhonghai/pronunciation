package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.lessons.LessonMappingQuestionDTO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.dto.question.WeightPhonemesDTO;
import com.cmg.lesson.data.dto.word.ListWord;
import com.cmg.lesson.data.dto.word.WordDTO;
import com.cmg.lesson.services.lessons.LessonMappingQuestionService;
import com.cmg.lesson.services.question.QuestionService;
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
@WebServlet(name = "ManagementWordServlet")
public class ManagementQuestionOfLessonServlet extends BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        WordCollectionService wordCollectionService = new WordCollectionService();
        WordOfQuestionService wordOfQuestionService = new WordOfQuestionService();
        QuestionService questionService = new QuestionService();
        LessonMappingQuestionService lessonMappingQuestionService = new LessonMappingQuestionService();
        Gson gson = new Gson();
        String action=request.getParameter("action");
        try {
            if(action.equalsIgnoreCase("list")){
                int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
                int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
                int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
                String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
                String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
                String lessonId =  (String)StringUtil.isNull(request.getParameter("lessonId"),"");
                //ListWord list = wordOfQuestionService.listWordByIdQuestion(questionId, search, order, start, length, draw);
                QuestionDTO questionDTO = lessonMappingQuestionService.listQuestionByIdLesson(lessonId, search, order, start, length, draw);
                String json = gson.toJson(questionDTO);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("listQuestion")){
                String lessonId = (String)StringUtil.isNull(request.getParameter("lessonId"),"");
                String questionSearch = (String)StringUtil.isNull(request.getParameter("questionSearch"),"");
                QuestionDTO questionDTO = questionService.searchName(lessonId, questionSearch);
                String json = gson.toJson(questionDTO);
                response.getWriter().write(json);

            }else if(action.equalsIgnoreCase("add")){
                String lessonId = (String)StringUtil.isNull(request.getParameter("lessonId"),"");
                String questionId = (String)StringUtil.isNull(request.getParameter("questionId"),"");
                LessonMappingQuestionDTO lessonMappingQuestionDTO = lessonMappingQuestionService.addQuestionToLessonDB(lessonId, questionId);
                String json = gson.toJson(lessonMappingQuestionDTO);
                response.getWriter().write(json);

            }else if(action.equalsIgnoreCase("delete")){
                String lessonId = (String)StringUtil.isNull(request.getParameter("lessonId"),"");
                String questionId = (String)StringUtil.isNull(request.getParameter("questionId"),"");
                LessonMappingQuestionDTO lessonMappingQuestionDTO = lessonMappingQuestionService.updateDeleted(lessonId, questionId);
                String json = gson.toJson(lessonMappingQuestionDTO);
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
