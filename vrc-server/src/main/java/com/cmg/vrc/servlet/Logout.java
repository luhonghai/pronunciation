package com.cmg.vrc.servlet;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by luhonghai on 2014-04-22.
 */
public class Logout extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Logout.class
            .getName());
    private static String PARA_PROFILE = "profile";
    private static String PARA_TYPE = "type";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
