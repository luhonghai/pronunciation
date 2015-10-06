package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.ListWord;
import com.cmg.lesson.data.dto.WordDTO;
import com.cmg.lesson.services.WordCollectionService;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by CMGT400 on 10/5/2015.
 */
@WebServlet(name = "ManagementWordServlet")
public class ManagementWordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WordCollectionService wordCollectionService=new WordCollectionService();
        WordDTO wordDTO=new WordDTO();
        Gson gson = new Gson();
        if(request.getParameter("list")!=null){
            int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
            int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
            int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
            String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
            String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
            ListWord list = wordCollectionService.searchWord(search,order,start,length,draw);
            try {
                String json = gson.toJson(list);
                response.getWriter().write(json);
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }
        if(request.getParameter("add")!=null){
            String wordAdd=request.getParameter("word");
            WordDTO word=gson.fromJson(wordAdd, WordDTO.class);

            try {


                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }
        if(request.getParameter("edit")!=null){
            String phonemes=request.getParameter("word");
            WordDTO word=gson.fromJson(phonemes, WordDTO.class);
            wordDTO.setId(word.getId());

            try{

                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }


        }
        if(request.getParameter("listPhonemes")!=null){
            String id= (String)StringUtil.isNull(request.getParameter("id"), "");
            try{

                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }


        }


        if(request.getParameter("delete")!=null){
            String id= request.getParameter("id");
            try {

                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
