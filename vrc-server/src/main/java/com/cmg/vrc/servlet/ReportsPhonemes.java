package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.StudentMappingTeacherDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelPhonemeDAO;
import com.cmg.vrc.data.jdo.Phoneme;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by CMGT400 on 6/9/2015.
 */
public class ReportsPhonemes extends HttpServlet {
    class Score{
        String mess;
        List<List<Object>> sc;
    }




    private static final Logger logger = Logger.getLogger(FeedbackHandler.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentMappingTeacherDAO studentMappingTeacherDAO=new StudentMappingTeacherDAO();
        UserVoiceModelPhonemeDAO userVoiceModelPhonemeDAO=new UserVoiceModelPhonemeDAO();
        Gson gson=new Gson();
        Score score=new Score();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String action=request.getParameter("action");
        String teacherName=request.getSession().getAttribute("username").toString();
        String teacherID=request.getSession().getAttribute("id").toString();
        if (action.equalsIgnoreCase("loadInfo")) {
            String studentName=request.getParameter("studentName");
            String phoneme=request.getParameter("phoneme");
            String dateFrom=request.getParameter("dateFrom");
            String dateTo=request.getParameter("dateTo");
            Date dateFrom1=null;
            Date dateTo1=null;
            if(dateFrom.length()>0){
                try {
                    dateFrom1=df.parse(dateFrom);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            if(dateTo.length()>0){
                try {
                    dateTo1=df.parse(dateTo);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            try {

                List<Phoneme> scores1 = userVoiceModelPhonemeDAO.getListPhonemeByPhonemeAndStudent(studentName,phoneme,dateFrom1,dateTo1);
                List<List<Object>> list = new ArrayList<List<Object>>();
                if(scores1!=null) {
                    for (int i = 0; i < scores1.size(); i++) {
                        List<Object> item = new ArrayList<>();
                        item.add(scores1.get(i).getServerTime());
                        item.add(scores1.get(i).getScore());
                        list.add(item);
                    }
                    score.sc = list;
                }else {
                    score.sc = null;
                }
                score.mess="success";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(action.equalsIgnoreCase("loadInfo")){
            String studentName=request.getParameter("studentName");

            try{

            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
