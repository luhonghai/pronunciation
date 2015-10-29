package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.country.CountryDTO;
import com.cmg.lesson.data.dto.ipa.IpaMapDTO;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.services.country.CountryService;
import com.cmg.lesson.services.ipa.IpaMapArpabetService;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.servlet.BaseServlet;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lantb on 2015-10-27.
 */
@WebServlet(name = "ManageCountryServlet")
public class ManageCountryServlet extends BaseServlet {
    private static String PARA_NAME = "country_name";
    private static String PARA_DESCRIPTION = "description";
    private static String PARA_COURSE_ID = "course";
    private static String PARA_COUNTRY_ID = "idCountry";
    private static String PARA_IS_DEFAULT= "default";
    private static String PARA_ACTION = "action";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        Gson gson = new Gson();
        CountryService service = new CountryService();
        String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
        String fileName = UUID.randomUUID().toString() + UUIDGenerator.generateUUID();
        try {
            Map<String, String> storePara = getMap(request,tmpDir,fileName);
            String action = (String)StringUtil.isNull(storePara.get(PARA_ACTION),"");
            if(action.equalsIgnoreCase("add")){
                String name = (String)StringUtil.isNull(storePara.get(PARA_NAME),"");
                String description = (String)StringUtil.isNull(storePara.get(PARA_DESCRIPTION),"");
                String idCourse = (String)StringUtil.isNull(storePara.get(PARA_COURSE_ID),"");
                String linkImageS3 = uploadS3AndGetLink(new File(storePara.get("file")));
                boolean isDefault = parseIsDefault((String)StringUtil.isNull(storePara.get(PARA_IS_DEFAULT),""));
                CountryDTO dto = service.addCountry(name,description,idCourse,linkImageS3,isDefault);
                String json = gson.toJson(dto);
                response.getWriter().print(json);
            }else if(action.equalsIgnoreCase("edit")){

            }else if(action.equalsIgnoreCase("delete")){

            }
        }catch (Exception e){
            response.getWriter().print("Error : " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
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
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(stream);
                    logger.info(name + "-" + value);
                    storePara.put(name, value);
                }else{
                    String getName = item.getName();
                    logger.info("file name : " + getName);
                    if(getName.endsWith(".png") || getName.endsWith(".jpg")){
                        FileUtils.copyInputStreamToFile(stream, new File(targetDir, tempName));
                        storePara.put("file",new File(targetDir, tempName).getAbsolutePath());
                    }
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
            logger.info("link download s3 : " + link);
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return link;
        }
        return null;
    }

    /**
     *
     * @param isDefault
     * @return
     */
    private boolean parseIsDefault(String isDefault){
        if(isDefault == "" || !isDefault.equalsIgnoreCase("on")){
            return false;
        }
        return true;
    }
}
