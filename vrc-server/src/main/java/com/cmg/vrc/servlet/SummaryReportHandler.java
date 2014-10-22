package com.cmg.vrc.servlet;

import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SummaryReport;
import com.cmg.vrc.util.FileHelper;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by luhonghai on 10/17/14.
 */
public class SummaryReportHandler extends HttpServlet {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        String key = request.getParameter("key");
        if (key == null || !key.trim().equalsIgnoreCase(Configuration.getValue(Configuration.API_KEY))) {
            printMessage(out, "Missing key");
            return;
        }
        printMessage(out, "Start analyzing ...");
        boolean status = SummaryReport.analyze(new SummaryReport.MessageListener() {
            @Override
            public void onMessage(String message) {
                printMessage(out, message);
            }

            @Override
            public void onError(String error) {
                printError(out, error);
            }
        });
        printMessage(out, status ? "Done" : "Skip");
    }

    private void printMessage(final PrintWriter writer, final String message) {
        print(writer, "<div style='width:100%;overflow: hidden;'>"
                +sdf.format(new Date(System.currentTimeMillis()))
                + " | " + message + "</div>");
    }

    private void printError(final PrintWriter writer, final String message) {
        print(writer, "<div style='width:100%;overflow: hidden;color:red;'>"
                + sdf.format(new Date(System.currentTimeMillis()))
                + " | " +  message + "</div>");
    }

    private void print(final PrintWriter writer, final String out) {

        writer.print(out);
        writer.flush();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
