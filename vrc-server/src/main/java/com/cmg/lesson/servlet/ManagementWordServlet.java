package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.WordDTO;
import com.cmg.lesson.services.WordCollectionService;
import com.cmg.vrc.util.StringUtil;

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
        if(request.getParameter("list")!=null){
            String s = (String)StringUtil.isNull(request.getParameter("start"),"");
            String l = (String)StringUtil.isNull(request.getParameter("length"),"");
            String d = (String)StringUtil.isNull(request.getParameter("draw"), "");
            String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");

        }
        if(request.getParameter("add")!=null){
            String word= (String)StringUtil.isNull(request.getParameter("sentence"),"");
            String author=(String)StringUtil.isNull( request.getSession().getAttribute("username").toString(),"");
            try {


                response.getWriter().write("success");
            }catch (Exception e){
                response.getWriter().write("error");
                e.printStackTrace();
            }
        }
        if(request.getParameter("edit")!=null){
            String id=request.getParameter("id");
            String sentence=request.getParameter("sentence");
            String author=request.getSession().getAttribute("username").toString();
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
