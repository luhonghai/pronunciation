package com.cmg.vrc.servlet;

import com.amazonaws.services.s3.model.S3Object;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.DatabaseVersionDAO;
import com.cmg.vrc.data.jdo.DatabaseVersion;
import com.cmg.vrc.service.DatabaseGeneratorService;
import com.cmg.vrc.util.AWSHelper;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cmg on 10/09/15.
 */
public class DatabaseDataHandler extends BaseServlet {

    class ResponseData {

        public int draw;

        public Double recordsTotal;

        public Double recordsFiltered;

        List<DatabaseVersion> data;
    }


    class ResponseStatus {
        boolean running;
        boolean stopping;
        String latestLog;
        int draw;
        int lines;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Expires", "Tue, 03 Jul 2001 06:00:00 GMT");
        response.setHeader("Last-Modified", new Date().toString());
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("text/plain");
        final PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        final String admin = (String) request.getSession().getAttribute("username");
        if (StringUtils.isEmpty(admin)) {
            out.write("Require login");
            out.close();
            return;
        }
        try {
            if (!StringUtils.isEmpty(action)) {
                final DatabaseVersionDAO dao = new DatabaseVersionDAO();
                final AWSHelper awsHelper = new AWSHelper();
                if (action.equalsIgnoreCase("status")) {
                    int draw = Integer.parseInt(request.getParameter("draw"));
                    int lines = Integer.parseInt(request.getParameter("lines"));
                    ResponseStatus status = new ResponseStatus();
                    status.running = DatabaseGeneratorService.getInstance().isRunning();
                    status.stopping = DatabaseGeneratorService.getInstance().isStopping();
                    status.latestLog = DatabaseGeneratorService.getInstance().getCurrentLog(lines);
                    status.draw = draw;
                    status.lines = lines;
                    out.write(new Gson().toJson(status));
                } else if (action.equalsIgnoreCase("stop")){
                    DatabaseGeneratorService.getInstance().forceStop();
                    out.write("done");
                } else if (action.equalsIgnoreCase("load")){
                    String lessonChange=request.getParameter("lessonChange");
                    DatabaseGeneratorService.getInstance().generate(admin,lessonChange);
                    out.write("done");
                } else if (action.equalsIgnoreCase("latest_log")) {
                    InputStream is = null;
                    BufferedReader bufferedReader = null;
                    try {
                        if (DatabaseGeneratorService.getInstance().isRunning()) {
                            if (DatabaseGeneratorService.getInstance().getCurrentLogFile() != null &&
                                    DatabaseGeneratorService.getInstance().getCurrentLogFile().exists())
                                is = new FileInputStream(DatabaseGeneratorService.getInstance().getCurrentLogFile());
                        } else {
                            S3Object s3Object = awsHelper.getS3Object(Constant.FOLDER_DATABASE
                                    + "/" + "latest.running.log");
                            if (s3Object != null) {
                                is = s3Object.getObjectContent();
                            }
                        }
                        if (is != null) {
                            bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                out.write(line + "\n");
                                out.flush();
                            }
                        } else {
                            out.print("No log found");
                        }
                    } catch (Exception e) {
                        out.print("No log found. Error: " + e.getMessage());
                        log("No log found", e);
                    } finally {
                        IOUtils.closeQuietly(is);
                        IOUtils.closeQuietly(bufferedReader);
                    }
                } else if (action.equalsIgnoreCase("link_generate")) {
                    String id = request.getParameter("id");
                    if (!StringUtils.isEmpty(id)) {
                        DatabaseVersion model = dao.getById(id);
                        if (model != null) {
                            out.write(awsHelper.generatePresignedUrl(Constant.FOLDER_DATABASE
                                    + "/"
                                    + model.getFileName()));
                        } else {
                            out.write("No language model found with id " + id);
                        }
                    } else {
                        out.write("No parameter id found");
                    }
                } else if (action.equalsIgnoreCase("select")) {
                    String id = request.getParameter("id");
                    if (!StringUtils.isEmpty(id)) {
                        DatabaseVersion model = dao.getById(id);
                        if (model != null) {
                            dao.removeSelected();
                            model.setSelectedDate(new Date(System.currentTimeMillis()));
                            model.setAdmin(admin);
                            model.setSelected(true);
                            dao.update(model);
                        } else {
                            out.write("No language model found with id " + id);
                        }
                    } else {
                        out.write("No parameter id found");
                    }

                } else if (action.equalsIgnoreCase("list")) {
                    String s = request.getParameter("start");
                    String l = request.getParameter("length");
                    String d = request.getParameter("draw");
                    String search = request.getParameter("search[value]");
                    String column = request.getParameter("order[0][column]");
                    String oder = request.getParameter("order[0][dir]");
                    int start = Integer.parseInt(s);
                    int length = Integer.parseInt(l);
                    int col = Integer.parseInt(column);
                    int draw = Integer.parseInt(d);
                    double count;
                    Gson gson = new Gson();
                    ResponseData responseData = new ResponseData();
                    List<DatabaseVersion> databaseVersions= dao.listAll(start, length, search, col, oder);
                    responseData.draw = draw;
                    try {
                        if (search.length() > 0) {
                            count = dao.getCountSearch(search);
                        } else {
                            count = dao.getCount();
                        }
                        responseData.recordsFiltered = count;
                        responseData.recordsTotal = count;
                        if(databaseVersions!=null && databaseVersions.size()>0) {
                            responseData.data = dao.listAll(start, length, search, col, oder);
                        }else{
                            responseData.data=new ArrayList<DatabaseVersion>();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        out.write(gson.toJson(responseData));
                    }
                } else {
                    out.write("No action found");
                }
            } else {
                out.write("No action found");
            }
        } catch (Exception e) {
            out.write("Could not complete request. Error: " + e.getMessage());
        } finally {
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void printException(final PrintWriter out , String message, Throwable e) {
        out.write("<p style='color:red'>ERROR: " +StringEscapeUtils.escapeHtml(message) + "</p>");
        if (e != null) {
            out.write("<p style='color:red'>");
            String exception = ExceptionUtils.getStackTrace(e);
            String[] rows = exception.split("\n");
            if (rows.length > 0) {
                for (String row : rows) {
                    out.write(StringEscapeUtils.escapeHtml(row) + "<br/>");
                }
            } else {
                out.write(exception + "<br/>");
            }
            out.write("</p>");
        }
    }
}
