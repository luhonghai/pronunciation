package com.cmg.lesson.servlet;

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
        Gson gson = new Gson();
        if(request.getParameter("list")!=null){
            String s = (String)StringUtil.isNull(request.getParameter("start"),"");
            String l = (String)StringUtil.isNull(request.getParameter("length"),"");
            String d = (String)StringUtil.isNull(request.getParameter("draw"), "");
            String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
            String oder = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");

        }
        if(request.getParameter("add")!=null){
            String word= (String)StringUtil.isNull(request.getParameter("word"), "");
            String definition= (String)StringUtil.isNull(request.getParameter("definition"), "");
            String pronunciation= (String)StringUtil.isNull(request.getParameter("pronunciation"), "");
            String mp3Url= (String)StringUtil.isNull(request.getParameter("mp3Url"), "");



            try {


                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }
        if(request.getParameter("edit")!=null){
            String phonemes=request.getParameter("word");
            Object word=gson.toJson(phonemes);
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

    }
}
