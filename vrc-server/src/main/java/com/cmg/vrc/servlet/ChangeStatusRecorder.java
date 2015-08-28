package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CMGT400 on 8/28/2015.
 */
public class ChangeStatusRecorder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RecorderDAO recorderDAO = new RecorderDAO();
        RecordedSentence recordedSentence = new RecordedSentence();
        int lastedVersion=recorderDAO.getLatestVersion();

        if(request.getParameter("reject")!=null){
            String id = request.getParameter("id");

            try{
                recordedSentence=recorderDAO.getById(id);
                recordedSentence.setStatus(2);
                recordedSentence.setVersion(lastedVersion + 1);
                recorderDAO.put(recordedSentence);
                response.getWriter().write("success");


            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if(request.getParameter("approved")!=null){
            String id = request.getParameter("id");

            try{
                recordedSentence=recorderDAO.getById(id);
                recordedSentence.setStatus(3);
                recordedSentence.setVersion(lastedVersion + 1);
                recorderDAO.put(recordedSentence);
                response.getWriter().write("success");


            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if(request.getParameter("locked")!=null){
            String id = request.getParameter("id");

            try{
                recordedSentence=recorderDAO.getById(id);
                recordedSentence.setStatus(4);
                recordedSentence.setVersion(lastedVersion+1);
                recorderDAO.put(recordedSentence);
                response.getWriter().write("success");
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }


}
