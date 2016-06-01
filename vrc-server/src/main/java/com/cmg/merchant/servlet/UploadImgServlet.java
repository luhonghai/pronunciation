package com.cmg.merchant.servlet;

import com.cmg.merchant.services.CourseServices;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lantb on 2016-06-01.
 */
@WebServlet(name = "UploadImgServlet")
public class UploadImgServlet extends HttpServlet {
    private static String PARA_ACTION = "action";
    private static String PARA_ID_COURSE = "idCourse";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
        String fileName = UUID.randomUUID().toString() + UUIDGenerator.generateUUID();
        CourseServices service = new CourseServices();
        try {
            Map<String, String> storePara = getMap(request,tmpDir,fileName);
            String action = (String) StringUtil.isNull(storePara.get(PARA_ACTION), "");
            if(action.equalsIgnoreCase("uploadImg")){
                String idCourse = (String)StringUtil.isNull(storePara.get(PARA_ID_COURSE),"");
                String linkImageS3 = uploadS3AndGetLink(new File(storePara.get("file")));
                String result = service.updateImgCourse(idCourse,linkImageS3);
                response.getWriter().print(result);
            }
        }catch (Exception e){
            response.getWriter().print("Error : " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    /**
     *
     * @param request
     * @return
     */
    private Map<String, String> getMap(HttpServletRequest request,String targetDir, String tempName){
        Map<String, String> storePara = new HashMap<String, String>();
        ServletFileUpload upload = new ServletFileUpload();
        try {
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream =null;
                try{
                    stream=item.openStream();
                    if (item.isFormField()) {
                        String value = Streams.asString(stream, "UTF-8");
                        storePara.put(name, value);
                    }else{
                        String getName = item.getName();
                        if(getName.endsWith(".png") || getName.endsWith(".jpg")){
                            FileUtils.copyInputStreamToFile(stream, new File(targetDir, tempName));
                            storePara.put("file",new File(targetDir, tempName).getAbsolutePath());
                        }
                    }
                }finally {
                    IOUtils.closeQuietly(stream);
                }

            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storePara;
    }



    /**
     *
     * @param file
     * @return
     */
    private String uploadS3AndGetLink(File file){
        if(file!=null){
            AWSHelper awsHelper = new AWSHelper();
            String uuid = UUIDGenerator.generateUUID();
            String fileTempName  = uuid + file.getName();
            String link  = awsHelper.uploadAndGenerateURL(Constant.FOLDER_IMAGE_COUNTRY + "/" + fileTempName,
                    file);
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return link;
        }
        return null;
    }
}
