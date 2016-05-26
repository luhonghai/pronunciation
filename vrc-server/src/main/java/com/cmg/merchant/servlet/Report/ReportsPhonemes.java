package com.cmg.merchant.servlet.Report;

import com.cmg.merchant.services.Report.ReportPhonemeService;
import com.cmg.vrc.data.dao.impl.StudentMappingTeacherDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelPhonemeDAO;
import com.cmg.vrc.data.jdo.Phoneme;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.servlet.FeedbackHandler;
import com.cmg.vrc.util.StringUtil;
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
public class ReportsPhonemes extends BaseServlet {

    private static final Logger logger = Logger.getLogger(ReportsPhonemes.class
            .getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        ReportPhonemeService service = new ReportPhonemeService();
        String teacherName= (String) StringUtil.isNull(request.getSession().getAttribute("username").toString(),"");
        String action = (String) StringUtil.isNull(request.getParameter("action"),"");
        String result = "error";
        if(action.equalsIgnoreCase("load")){
            result = service.loadData(teacherName);
            response.getWriter().println(result);
        }else  if(action.equalsIgnoreCase("loadStudent")){
            //result = service.generateListStudent(teacherName);
            response.getWriter().println(result);
        }else if(action.equalsIgnoreCase("loadPhonemes")){
            //result = service.generatePhonemeList();
            response.getWriter().println(result);
        }else if(action.equalsIgnoreCase("getReportData")){
            String studentName = (String) StringUtil.isNull(request.getParameter("studentName"),"");
            String arpabet = (String) StringUtil.isNull(request.getParameter("phoneme"),"");
            String startDate = (String) StringUtil.isNull(request.getParameter("dateFrom"),"");
            String type = (String) StringUtil.isNull(request.getParameter("type"),"");
            result = service.generateDataDrawLineChart(studentName,arpabet,startDate,type);
            response.getWriter().println(result);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
