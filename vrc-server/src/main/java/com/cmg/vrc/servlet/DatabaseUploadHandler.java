package com.cmg.vrc.servlet;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.DatabaseVersionDAO;
import com.cmg.vrc.data.dao.impl.DictionaryVersionDAO;
import com.cmg.vrc.data.jdo.DatabaseVersion;
import com.cmg.vrc.data.jdo.DictionaryVersion;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
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

/**
 * Created by cmg on 10/09/15.
 */
public class DatabaseUploadHandler extends BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        final String admin = (String) request.getSession().getAttribute("username");
        if (StringUtils.isEmpty(admin)) {
            out.write("Require login");
            out.close();
            return;
        }
        AWSHelper awsHelper = new AWSHelper();
        DatabaseVersionDAO databaseVersionDAO = new DatabaseVersionDAO();
        try {
            //create a new Map<String,String> to store all parameter
            Map<String, String> storePara = new HashMap<String, String>();
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();
            // Parse the request
            FileItemIterator iter = null;
            iter = upload.getItemIterator(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            File voiceRecordDir = new File(FileHelper.getTmpSphinx4DataDir(), "database_data");
            if (!voiceRecordDir.exists() || !voiceRecordDir.isDirectory()) {
                voiceRecordDir.mkdirs();
            }

            String tmpFile = UUID.randomUUID().toString() + UUIDGenerator.generateUUID();
            String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
            File dictFile = new File(tmpDir, tmpFile);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    logger.info(name);
                    String value = Streams.asString(stream);
                    storePara.put(name, value);
                }else{
                    String getName = item.getName();
                    FileUtils.copyInputStreamToFile(stream, dictFile);
                }
            }
            if (dictFile.exists()) {
                int version = databaseVersionDAO.getMaxVersion();
                version++;
                String fileName = "database-v" + version + ".dict";
                DatabaseVersion databaseVersion = new DatabaseVersion();
                databaseVersion.setSelected(true);
                databaseVersion.setAdmin(admin);
                Date now = new Date(System.currentTimeMillis());
                databaseVersion.setSelectedDate(now);
                databaseVersion.setCreatedDate(now);
                databaseVersion.setFileName(fileName);
                databaseVersion.setVersion(version);
                awsHelper.upload(Constant.FOLDER_DATABASE + "/" + fileName, dictFile);
                databaseVersionDAO.removeSelected();
                databaseVersionDAO.createObj(databaseVersion);
                out.print("success");
            } else {
                out.print("no file found");
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            out.print("Error when upload file. FileUploadException, message: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            out.print("Error when upload file. Message: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
