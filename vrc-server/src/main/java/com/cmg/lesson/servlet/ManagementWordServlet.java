package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.word.ListWord;
import com.cmg.lesson.data.dto.word.WordDTO;
import com.cmg.lesson.services.word.WordCollectionService;
import com.cmg.lesson.services.word.WordMappingPhonemesService;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CMGT400 on 10/5/2015.
 */
@WebServlet(name = "ManagementWordServlet")
public class ManagementWordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        WordCollectionService wordCollectionService=new WordCollectionService();
        Gson gson = new Gson();
        try {
            if(request.getParameter("list")!=null){
                int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
                int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
                int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
                String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
                String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
                ListWord list = wordCollectionService.searchWord(search,order,start,length,draw);
                String json = gson.toJson(list);
                response.getWriter().write(json);
            }else if(request.getParameter("add")!=null){
                String wordAdd = request.getParameter("word");
                WordDTO word = gson.fromJson(wordAdd, WordDTO.class);
                WordDTO dto =  wordCollectionService.addWordPhonemes(word.getWord(), word.getPronunciation(),
                        word.getDefinition(), word.getMp3Path(), word.getPhonemes());
                String json = gson.toJson(dto);
                response.getWriter().write(json);
            }else if(request.getParameter("edit")!=null){
                String phonemes = request.getParameter("word");
                WordDTO word = gson.fromJson(phonemes, WordDTO.class);
                WordDTO dto =  wordCollectionService.updateWordPhonemes(word.getId(),
                        word.getDefinition(), word.getMp3Path(), word.getPhonemes());
                String json = gson.toJson(dto);
                response.getWriter().write(json);
            }
            if(request.getParameter("listPhonemes")!=null){
                String id= (String)StringUtil.isNull(request.getParameter("id"), "");
                WordMappingPhonemesService wmpService = new WordMappingPhonemesService();
                WordDTO dto = new WordDTO();
                dto.setPhonemes(wmpService.getByWordID(id));
                String json = gson.toJson(dto);
                response.getWriter().write(json);

            }else if(request.getParameter("delete")!=null){
                String id= request.getParameter("id");
                WordDTO dto = wordCollectionService.deleteWord(id);
                String json = gson.toJson(dto);
                response.getWriter().write(json);
            }
        }catch (Exception e){
            response.getWriter().print("Error : an error has been occurred in server!");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
