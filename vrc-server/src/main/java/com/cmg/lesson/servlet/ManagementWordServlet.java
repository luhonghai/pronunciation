package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.WordDTO;
import com.cmg.lesson.services.WordCollectionService;

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
    class admin{
        public int draw;
        public Double recordsTotal;
        public Double recordsFiltered;

        List<WordDTO> data;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WordCollectionService wordCollectionService=new WordCollectionService();
        if(request.getParameter("list")!=null){
            String s = request.getParameter("start");
            String l = request.getParameter("length");
            String d = request.getParameter("draw");
            String search = request.getParameter("search[value]");

        }
        if(request.getParameter("add")!=null){
            String sentence=request.getParameter("sentence");
            String author=request.getSession().getAttribute("username").toString();
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
