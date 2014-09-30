package com.cmg.vrc.servlet;

import com.cmg.vrc.data.ContactModel;
import com.cmg.vrc.service.ContactMailService;
import com.cmg.vrc.util.MailUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by Hai Lu on 2014-04-22.
 */
public class ContactHandler extends BaseServlet {
    private static final Logger logger = Logger.getLogger(ContactHandler.class
            .getName());
    private static String POST_DATA = "postData";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Enumeration<String> parameterNames = request.getParameterNames();
            PrintWriter out = response.getWriter();
            boolean sended = false;
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement().trim();
                out.println(paramName);
                logger.info(paramName);
                if (paramName.contains("message")) {
                    Gson gson = new Gson();
                    ContactModel contact = gson.fromJson(paramName, ContactModel.class);
                    ContactMailService cms = new ContactMailService(MailUtil.getBodyContactMail(contact));
                    cms.start();
                    sended = true;
                }
            }
            if (sended) {
                printMessage(response, "success");
            } else {
                printMessage(response, "Missing parameter!");
            }

        }catch (Exception e){
            printMessage(response, "fail. Message: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
