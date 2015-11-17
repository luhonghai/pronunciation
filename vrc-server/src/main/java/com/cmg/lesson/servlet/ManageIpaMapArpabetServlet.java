package com.cmg.lesson.servlet;

import com.cmg.lesson.data.dto.ipa.IpaMapDTO;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
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
 * Created by lantb on 2015-10-27.
 */
@WebServlet(name = "ManageIpaMapArpabetServlet")
public class ManageIpaMapArpabetServlet extends BaseServlet {
    private static String PARA_ACTION = "action";
    private String PARA_ID = "id";
    private String PARA_DESCRIPTION = "description";
    private String PARA_TYPE = "type";
    private String PARA_ARPABET = "arpabet";
    private String PARA_IPA = "ipa";
    private String PARA_COLOR = "addColor";
    private String PARA_TIP = "tip";
    private String PARA_WORDS = "words";
    private String PARA_MP3 = "mp3";
    private String PARA_INDEX_TYPE = "index_type";
    private String PARA_TEXT_TONGUE = "tongueText";
    private String PARA_IMG_TONGUE = "imageTongue";
    private String PARA_TEXT_LIP = "lipsText";
    private String PARA_IMG_LIP = "imageLips";
    private String PARA_TEXT_JAW = "jawText";
    private String PARA_IMG_JAW = "imageJaw";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        String loadData= (String)StringUtil.isNull(request.getParameter("loadData"),"");
        String getById= (String)StringUtil.isNull(request.getParameter("getById"),"");
        String delete= (String)StringUtil.isNull(request.getParameter("delete"),"");
        Gson gson = new Gson();
        IpaMapArpabetService service = new IpaMapArpabetService();
        String tmpDir = FileHelper.getTmpSphinx4DataDir().getAbsolutePath();
        try {
            Map<String, String> storePara = getMap(request,tmpDir);
            String action = (String)StringUtil.isNull(storePara.get(PARA_ACTION),"");
            if(loadData.equalsIgnoreCase("list")){
                int start = Integer.parseInt(StringUtil.isNull(request.getParameter("start"), 0).toString());
                int length = Integer.parseInt(StringUtil.isNull(request.getParameter("length"), 0).toString());
                int draw = Integer.parseInt(StringUtil.isNull(request.getParameter("draw"), 0).toString());
                String search = (String)StringUtil.isNull(request.getParameter("search[value]"), "");
                String order = (String)StringUtil.isNull(request.getParameter("order[0][dir]"), "");
                int column = Integer.parseInt(StringUtil.isNull(request.getParameter("order[0][column]"),"").toString());
                String createDateFrom = (String) StringUtil.isNull(request.getParameter("CreateDateFrom"), "");
                String createDateTo = (String) StringUtil.isNull(request.getParameter("CreateDateTo"),"");
                IpaMapDTO dto = service.search(start,length,search,column,order,createDateFrom,createDateTo,draw);
                String json = gson.toJson(dto);
                System.out.println("json form list : " + json);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("add")){
                //String clientData = (String) StringUtil.isNull(request.getParameter("dto"),"");
                //IpaMapArpabet map = gson.fromJson(clientData,IpaMapArpabet.class);
                IpaMapArpabet map = convertByMap(storePara,false);
                IpaMapDTO dto = service.addMapping(map);
                String json = gson.toJson(dto);
                response.getWriter().write(json);
            }else if(action.equalsIgnoreCase("edit")){
                //String clientData = (String) StringUtil.isNull(request.getParameter("dto"),"");
                //IpaMapArpabet map = gson.fromJson(clientData,IpaMapArpabet.class);
                 IpaMapArpabet map = convertByMap(storePara,true);
                 IpaMapDTO dto = service.update(map);
                 String json = gson.toJson(dto);
                 response.getWriter().write(json);
            }else if(getById.equalsIgnoreCase("getById")){
                String id = (String)StringUtil.isNull(request.getParameter("id"),"");
                IpaMapArpabet map = service.getById(id);
                String json = gson.toJson(map);
                response.getWriter().write(json);
            }else if(delete.equalsIgnoreCase("delete")){
                String id = (String)StringUtil.isNull(request.getParameter("id"),"");
                IpaMapDTO dto = service.delete(id);
                String json = gson.toJson(dto);
                response.getWriter().write(json);
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
    private Map<String, String> getMap(HttpServletRequest request,String targetDir){
        Map<String, String> storePara = new HashMap<String, String>();
        ServletFileUpload upload = new ServletFileUpload();
        try {
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(stream, "UTF-8");
                    logger.info(name + "-" + value);
                    storePara.put(name, value);
                }else{
                    String getName = item.getName();
                    logger.info("file name : " + getName);
                    if(getName.endsWith(".png") || getName.endsWith(".jpg")){
                        String filename = UUID.randomUUID().toString() + UUIDGenerator.generateUUID();
                        FileUtils.copyInputStreamToFile(stream, new File(targetDir, filename));
                        logger.info(name + "-" + new File(targetDir, filename).getAbsolutePath());
                        storePara.put(name,new File(targetDir, filename).getAbsolutePath());
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
     * @param map
     * @return
     */
    private IpaMapArpabet convertByMap(Map<String, String> map, boolean acction){
        if(map!=null && map.size() > 0){
            IpaMapArpabet obj = new IpaMapArpabet();
            obj.setIpa((String) StringUtil.isNull(map.get(PARA_IPA), ""));
            obj.setArpabet((String) StringUtil.isNull(map.get(PARA_ARPABET), ""));
            obj.setType((String) StringUtil.isNull(map.get(PARA_TYPE), ""));
            obj.setIndexingType(Integer.parseInt(StringUtil.isNull(map.get(PARA_INDEX_TYPE), "").toString()));
            obj.setDescription((String) StringUtil.isNull(map.get(PARA_DESCRIPTION), ""));
            obj.setColor((String) StringUtil.isNull(map.get(PARA_COLOR), ""));
            obj.setMp3Url((String) StringUtil.isNull(map.get(PARA_MP3), ""));

            if(map.get(PARA_IMG_TONGUE)==null) {
                if(acction){
                    obj.setImgTongue(map.get("imgTongue"));
                }else {
                    obj.setImgTongue(null);
                }
            }else{
                obj.setImgTongue(uploadS3AndGetLink(new File(map.get(PARA_IMG_TONGUE))));

            }

            obj.setTextTongue((String) StringUtil.isNull(map.get(PARA_TEXT_TONGUE), ""));

            if(map.get(PARA_IMG_LIP)==null) {
                if(acction){
                    obj.setImgLip(map.get("imgLips"));
                }else {
                    obj.setImgLip(null);
                }

            }else{
                obj.setImgLip(uploadS3AndGetLink(new File(map.get(PARA_IMG_LIP))));

            }
            obj.setTextLip((String) StringUtil.isNull(map.get(PARA_TEXT_LIP), ""));

            if(map.get(PARA_IMG_JAW)==null) {
                if(acction){
                    obj.setImgJaw(map.get("imgJaw"));
                }else {
                    obj.setImgJaw(null);
                }
            }else {
                obj.setImgJaw(uploadS3AndGetLink(new File(map.get(PARA_IMG_JAW))));
            }

            obj.setTextJaw((String) StringUtil.isNull(map.get(PARA_TEXT_JAW), ""));
            obj.setWords((String) StringUtil.isNull(map.get(PARA_WORDS), ""));
            obj.setTip((String) StringUtil.isNull(map.get(PARA_TIP), ""));
            if(acction) {
                obj.setId(map.get(PARA_ID));
            }
            return obj;
        }
        return null;
    }

}
