package com.cmg.merchant.servlet;

import com.cmg.merchant.services.CompanyServices;
import com.cmg.merchant.services.CourseServices;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-02.
 */
@WebServlet(name = "SuggestionServlet")
public class SuggestionServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(SuggestionServlet.class
            .getName());
    private static String SUGGESTION_COURSE = "course";
    private static String SUGGESTION_COMPANY = "company";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        String action = (String)StringUtil.isNull(request.getParameter("action"), 0).toString();
        CourseServices cServices = new CourseServices();
        CompanyServices cpServices = new CompanyServices();
        Gson gson = new Gson();
        if(action.equalsIgnoreCase(SUGGESTION_COURSE)){
            String query = (String)StringUtil.isNull(request.getParameter("query"), 0).toString();
            ArrayList<String> listSuggestion = cServices.suggestionCourse(query);
            String json = gson.toJson(listSuggestion);
            response.getWriter().println(json);
        }else if(action.equalsIgnoreCase(SUGGESTION_COMPANY)){
            String query = (String)StringUtil.isNull(request.getParameter("query"), 0).toString();
            ArrayList<String> listSuggestion = cpServices.suggestionCompany(query);
            String json = gson.toJson(listSuggestion);
            response.getWriter().println(json);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
