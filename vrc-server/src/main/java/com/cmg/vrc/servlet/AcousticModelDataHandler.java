package com.cmg.vrc.servlet;

import com.amazonaws.services.s3.model.S3Object;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.AcousticModelVersionDAO;
import com.cmg.vrc.data.dao.impl.DictionaryVersionDAO;
import com.cmg.vrc.data.jdo.AcousticModelVersion;
import com.cmg.vrc.data.jdo.DictionaryVersion;
import com.cmg.vrc.service.AcousticModelTrainingService;
import com.cmg.vrc.sphinx.training.AcousticModelTraining;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cmg on 10/09/15.
 */
public class AcousticModelDataHandler extends BaseServlet {

    class ResponseData {

        public int draw;

        public Double recordsTotal;

        public Double recordsFiltered;

        List<AcousticModelVersion> data;
    }

    class ResponseStatus {
        boolean running;
        boolean stopping;
        String latestLog;
        int draw;
        int lines;
    }

    class TrainingRequest {

        boolean extra;

        Map<String, String> configuration;

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                final AcousticModelVersionDAO dao = new AcousticModelVersionDAO();
                final AWSHelper awsHelper = new AWSHelper();
                if (action.equalsIgnoreCase("status")) {
                    int draw = Integer.parseInt(request.getParameter("draw"));
                    int lines = Integer.parseInt(request.getParameter("lines"));
                    ResponseStatus status = new ResponseStatus();
                    status.running = AcousticModelTrainingService.getInstance().isRunning();
                    status.stopping = AcousticModelTrainingService.getInstance().isStopping();
                    status.latestLog = AcousticModelTrainingService.getInstance().getCurrentLog(lines);
                    status.draw = draw;
                    status.lines = lines;
                    out.write(new Gson().toJson(status));
                } else if (action.equalsIgnoreCase("stop")){
                    AcousticModelTrainingService.getInstance().forceStop();
                    out.write("done");
                } else if (action.equalsIgnoreCase("train")){
                    String data = request.getParameter("data");
                    Gson gson = new Gson();
                    TrainingRequest trainingRequest = gson.fromJson(data, TrainingRequest.class);
                    AcousticModelTrainingService.getInstance().train(admin,
                            trainingRequest.extra,
                            trainingRequest.configuration);
                    out.write("done");
                } else if (action.equalsIgnoreCase("latest_log")) {
                    InputStream is = null;
                    BufferedReader bufferedReader = null;
                    try {
                        if (AcousticModelTrainingService.getInstance().isRunning()) {
                            if (AcousticModelTrainingService.getInstance().getCurrentLogFile() != null &&
                                    AcousticModelTrainingService.getInstance().getCurrentLogFile().exists())
                                is = new FileInputStream(AcousticModelTrainingService.getInstance().getCurrentLogFile());
                        } else {
                            S3Object s3Object = awsHelper.getS3Object(AcousticModelTraining.getS3KeyLatestRunningLog());
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
                        try {
                            if (is != null)
                                is.close();
                            if (bufferedReader != null)
                                bufferedReader.close();
                        } catch (Exception e) {}
                    }
                } else if (action.equalsIgnoreCase("link_generate")) {
                    String id = request.getParameter("id");
                    if (!StringUtils.isEmpty(id)) {
                        String key = Constant.FOLDER_ACOUSTIC_MODEL + "/" + id;
                        if (awsHelper.getS3Object(key) != null) {
                            out.write(awsHelper.generatePresignedUrl(key));
                        } else {
                            out.write("No file found with key " + id);
                        }
                    } else {
                        out.write("No parameter id found");
                    }
                } else if (action.equalsIgnoreCase("select")) {
                    String id = request.getParameter("id");
                    if (!StringUtils.isEmpty(id)) {
                        AcousticModelVersion model = dao.getById(id);
                        if (model != null) {
                            dao.removeSelected();
                            model.setSelectedDate(new Date(System.currentTimeMillis()));
                            model.setAdmin(admin);
                            model.setSelected(true);
                            dao.update(model);
                        } else {
                            out.write("No acoustic model found with id " + id);
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
                    responseData.draw = draw;
                    try {
                        if (search.length() > 0) {
                            count = dao.getCountSearch(search);
                        } else {
                            count = dao.getCount();
                        }
                        responseData.recordsFiltered = count;
                        responseData.recordsTotal = count;
                        responseData.data = dao.listAll(start, length, search, col, oder);
                    } catch (Exception e) {
                        log("Could not list acoustic model", e);
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
