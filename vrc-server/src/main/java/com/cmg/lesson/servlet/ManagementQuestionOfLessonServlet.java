package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.dto.question.WeightPhonemesDTO;
import com.cmg.lesson.data.dto.word.ListWord;
import com.cmg.lesson.data.dto.word.WordDTO;
import com.cmg.lesson.services.lessons.LessonMappingQuestionService;
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
                QuestionDTO questionDTO = lessonMappingQuestionService.listQuestionByIdLesson(lessonId,search,order,start,length,draw);
                String json = gson.toJson(questionDTO);
                response.getWriter().write(json);
            }else if(request.getParameter("add")!=null){
                String wordAdd = request.getParameter("word");
                WeightPhonemesDTO dtoClient = gson.fromJson(wordAdd, WeightPhonemesDTO.class);
                QuestionDTO dto = wordOfQuestionService.addWordToQuestion(dtoClient);
                String json = gson.toJson(dto);
                response.getWriter().write(json);

            }else if(request.getParameter("edit")!=null){
                String wordEdit = request.getParameter("word");
                WeightPhonemesDTO dtoClient = gson.fromJson(wordEdit, WeightPhonemesDTO.class);
                QuestionDTO dto = wordOfQuestionService.updateWordToQuestion(dtoClient);
                String json = gson.toJson(dto);
                response.getWriter().write(json);

            }else if(request.getParameter("listPhonemes")!=null){
                String word= (String)StringUtil.isNull(request.getParameter("word"), "");
                WordMappingPhonemesService service = new WordMappingPhonemesService();
                WordDTO dto = service.getByWord(word);
                String json = gson.toJson(dto);
                response.getWriter().write(json);

            }else if(request.getParameter("listPhonemesEdit")!=null){
                String wordId = (String)StringUtil.isNull(request.getParameter("idWord"), "");
                String questionId = (String)StringUtil.isNull(request.getParameter("idQuestion"), "");
                WeightForPhonemeService service =  new WeightForPhonemeService();
                QuestionDTO dto = service.listAll(questionId, wordId);
                String json = gson.toJson(dto);
                response.getWriter().write(json);

            }else if(request.getParameter("delete")!=null){
                String idQuestion = (String)StringUtil.isNull(request.getParameter("idQuestion"), "");
                String idWord = (String)StringUtil.isNull(request.getParameter("idWord"), "");
                QuestionDTO dto = wordOfQuestionService.deleteWordOfQuestion(idQuestion,idWord);
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
