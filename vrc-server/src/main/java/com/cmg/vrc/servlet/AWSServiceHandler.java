package com.cmg.vrc.servlet;

import com.amazonaws.services.elasticbeanstalk.model.EnvironmentDescription;
import com.cmg.vrc.util.AWSHelper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by cmg on 6/8/15.
 */
public class AWSServiceHandler extends BaseServlet {

    private static class ResponseData<T> {
        String message;
        boolean status;
        List<T> data;
    }

    private static final Logger logger = Logger.getLogger(AuthHandler.class
            .getName());


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = request.getSession().getAttribute("role").toString();
        if (role.equals("1")) {
            String action = request.getParameter("action");
            String target = request.getParameter("target");
            String data = request.getParameter("data");
            ResponseData<Object> rd = new ResponseData<Object>();
            Gson gson = new Gson();
            try {
                if (!StringUtils.isEmpty(action) && !StringUtils.isEmpty(target)) {
                    AWSHelper helper = new AWSHelper();

                    if (target.equalsIgnoreCase("environment")) {
                        if (action.equalsIgnoreCase("list")) {
                            ResponseData<EnvironmentDescription> responseData = new ResponseData<EnvironmentDescription>();
                            responseData.data = helper.getEnvironments();
                            responseData.status = true;
                            printMessage(response, gson.toJson(responseData));
                        } else if (action.equalsIgnoreCase("restart")) {
                            helper.restartBeanstalkApp(data);
                            rd.status = true;
                            rd.message = "Successfully";
                            printMessage(response, gson.toJson(rd));
                        } else if (action.equalsIgnoreCase("rebuild")) {
                            helper.rebuildEnvironment(data);
                            rd.status = true;
                            rd.message = "Successfully";
                            printMessage(response, gson.toJson(rd));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Could not complete request", e);
                rd.status = false;
                rd.message = "An error occurs while complete request. Message: " + e.getMessage();
                printMessage(response, gson.toJson(rd));
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
