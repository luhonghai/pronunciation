package com.cmg.vrc.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmgvn on 4/27/16.
 */
@WebServlet(name = "InvitationServlet", urlPatterns = {"/InvitationServlet"})
public class InvitationServlet extends HttpServlet {

    public static class InvitationData {
        public String id;
        public String studentName;
        public String teacherName;
        public String firstTeacherName;
        public String lastTeacherName;
        public String companyName;
        public String status;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ResponseData<List<InvitationData>> responseData = new ResponseData<>();
        responseData.setStatus(true);
        responseData.setMessage("success");
        responseData.setData(new ArrayList<InvitationData>());
        out.print(new Gson().toJson(responseData));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
