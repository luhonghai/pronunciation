package com.cmg.vrc.servlet;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.LanguageModelVersionDAO;
import com.cmg.vrc.data.jdo.LanguageModelVersion;
import com.cmg.vrc.service.LanguageModelService;
import com.cmg.vrc.util.AWSHelper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by cmg on 10/09/15.
 */
public class LanguageModelHandler extends BaseServlet {

    class ResponseData {

        public int draw;

        public Double recordsTotal;

        public Double recordsFiltered;

        List<LanguageModelVersion> data;
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
                final LanguageModelVersionDAO dao = new LanguageModelVersionDAO();
                final AWSHelper awsHelper = new AWSHelper();
                if (action.equalsIgnoreCase("load")) {
                    response.setContentType("text/html; charset=utf-8");
                    long start = System.currentTimeMillis();
                    LanguageModelService languageModelService = new LanguageModelService(new LanguageModelService.TrainingListener() {
                        @Override
                        public void onMessage(String message) {
                            out.write(StringEscapeUtils.escapeHtml(message) + "<br/>");
                            out.flush();
                        }

                        @Override
                        public void onError(String message, Throwable e) {
                            printException(out, message, e);
                            out.flush();
                        }

                        @Override
                        public void onSuccess(File languageModel) {
                            try {
                                int version = dao.getMaxVersion();
                                version++;
                                String fileName = "version-" +version +".lm";
                                out.write("Upload " + languageModel + " to AWS S3 " + fileName + "<br/>");
                                awsHelper.upload(Constant.FOLDER_LANGUAGE_MODEL
                                        + "/" + fileName, languageModel);
                                Date now = new Date(System.currentTimeMillis());
                                LanguageModelVersion languageModelVersion = new LanguageModelVersion();
                                languageModelVersion.setVersion(version);
                                languageModelVersion.setAdmin(admin);
                                languageModelVersion.setCreatedDate(now);
                                languageModelVersion.setFileName(fileName);
                                languageModelVersion.setSelected(true);
                                languageModelVersion.setSelectedDate(now);
                                out.write("Insert information to database<br/>");
                                dao.removeSelected();
                                dao.createObj(languageModelVersion);
                                out.write("Successfully!<br/>");
                            } catch (Exception e) {
                                printException(out, "Could not insert information to database", e);
                            }
                            out.flush();
                        }
                    });
                    languageModelService.training();
                    out.write("Execution time: " + (System.currentTimeMillis() - start) + "ms");
                    out.flush();
                } else if (action.equalsIgnoreCase("link_generate")) {
                    String id = request.getParameter("id");
                    if (!StringUtils.isEmpty(id)) {
                        LanguageModelVersion languageModelVersion = dao.getById(id);
                        if (languageModelVersion != null) {
                            out.write(awsHelper.generatePresignedUrl(Constant.FOLDER_LANGUAGE_MODEL
                                    + "/"
                                    + languageModelVersion.getFileName()));
                        } else {
                            out.write("No language model found with id " + id);
                        }
                    } else {
                        out.write("No parameter id found");
                    }
                } else if (action.equalsIgnoreCase("select")) {
                    String id = request.getParameter("id");
                    if (!StringUtils.isEmpty(id)) {
                        LanguageModelVersion languageModelVersion = dao.getById(id);
                        if (languageModelVersion != null) {
                            dao.removeSelected();
                            languageModelVersion.setSelectedDate(new Date(System.currentTimeMillis()));
                            languageModelVersion.setAdmin(admin);
                            languageModelVersion.setSelected(true);
                            dao.update(languageModelVersion);
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
