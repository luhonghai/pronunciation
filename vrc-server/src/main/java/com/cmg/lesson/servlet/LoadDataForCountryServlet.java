package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.country.CountryDTO;
import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.services.country.CountryService;
import com.cmg.lesson.services.course.CourseService;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lantb on 2015-10-27.
 */
@WebServlet(name = "LoadDataForCountryServlet")
public class LoadDataForCountryServlet extends BaseServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        Gson gson = new Gson();
        CountryService countryService = new CountryService();
        String action = (String) StringUtil.isNull(request.getParameter("action"),"");
        try{
            if(action.equalsIgnoreCase("getAllCourse")){
                CourseService service = new CourseService();
                CourseDTO dto = service.listAll();
                String json = gson.toJson(dto);
                response.getWriter().print(json);
            }else if(action.equalsIgnoreCase("listCountry")){
                int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
                int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
                int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
                String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
                String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
                int column = Integer.parseInt(StringUtil.isNull(request.getParameter("order[0][column]"),"").toString());
                String createDateFrom = (String) StringUtil.isNull(request.getParameter("CreateDateFrom"), "");
                String createDateTo = (String) StringUtil.isNull(request.getParameter("CreateDateTo"),"");
                CountryDTO countryDTO = countryService.search(start, length, search, column, order, createDateFrom, createDateTo, draw);
                String json = gson.toJson(countryDTO);
                response.getWriter().write(json);
            }
        }catch (Exception e){
            response.getWriter().print("Error : " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


}
