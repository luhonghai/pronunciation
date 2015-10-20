package com.cmg.lesson.services.level;

import com.cmg.lesson.common.DateSearchParse;
import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2015-10-20.
 */
public class LevelService {
    private static final Logger logger = Logger.getLogger(LevelService.class
            .getName());
    private String SUCCESS = "success";
    private String ERROR = "error";
    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        LevelDAO dao = new LevelDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param name, description,color
     * @return true if question was added to table.
     */
    public LevelDTO addLevelToDB(String name, String description, String color, boolean isDemo){
        LevelDAO dao = new LevelDAO();
        LevelDTO dto = new LevelDTO();
        String message;
        try {
            if(!isExistLevelName(name)) {
                if(isDemo && isDemoExisted()){
                    message = ERROR + ":" + "There are only one level to be set to DEMO";
                    dto.setMessage(message);
                    return dto;

                }
                Level lv = new Level();
                lv.setName(name);
                lv.setDescription(description);
                lv.setColor(color);
                lv.setDateCreated(new Date(System.currentTimeMillis()));
                lv.setIsDeleted(false);
                lv.setVersion(getMaxVersion());
                lv.setIsDemo(isDemo);
                dao.create(lv);
                message = SUCCESS;
            }else{
                message = ERROR + ":" + "level name is existed";
            }
        }catch(Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("can not add level : " + name + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param name
     * @return true is exits question name
     */
    public boolean isExistLevelName(String name) throws Exception{
        boolean isExist = false;
        LevelDAO dao = new LevelDAO();
        isExist = dao.checkExist(name);
        return isExist;
    }

    /**
     *
     * @param id
     * @param name
     * @param description
     * @param color
     * @return
     */
    public LevelDTO updateLevel(String id, String name, String description, String color, boolean isDemo,boolean isUpdateLessonName){
        LevelDAO dao = new LevelDAO();
        LevelDTO dto = new LevelDTO();
        String message = null;
        try {
           String oldName = (String)StringUtil.isNull(dao.getById(id).getName(),"");
            if(oldName.equalsIgnoreCase(name)){
                if(isDemo && isDemoExisted()){
                    message = ERROR + ":" + "There are only one level to be set to DEMO";
                    dto.setMessage(message);
                    return dto;

                }
                boolean check = dao.updateLevel(id, name, description, color, isDemo);
                if(check){
                    message = SUCCESS;
                }else{
                    message = ERROR + ":" + "an error has been occurred in server!";
                }
            }else{
                boolean isExistedNewName = isExistLevelName(name);
                if(isExistedNewName){
                    message = ERROR + ":" + "This name already existed!";
                }else{
                    if(isDemo && isDemoExisted()){
                        message = ERROR + ":" + "There are only one level to be set to DEMO";
                        dto.setMessage(message);
                        return dto;

                    }
                    boolean check = dao.updateLevel(id, name, description, color, isDemo);
                    if(check){
                        message = SUCCESS;
                    }else{
                        message = ERROR + ":" + "an error has been occurred in server!";
                    }
                }
            }
        }catch (Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("can not update level : " + name + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     * @return
     */
    public LevelDTO deleteLevelToDB(String id){
        LevelDTO dto = new LevelDTO();
        LevelDAO dao = new LevelDAO();
        String message;
        try{
            boolean isDelete=dao.updateDeleted(id);
            if (isDelete){
                message = SUCCESS;
            }else{
                message = ERROR + ": " + "an error has been occurred in server!";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("can not delete level id: " + id + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }


    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @return List<Question>
     */
    public List<Level> listAll(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo){
        LevelDAO dao = new LevelDAO();
        try{
            return dao.listAll(start, length, search,column, order, createDateFrom, createDateTo);
        }catch (Exception ex){
            logger.error("list all level error, because:" + ex.getMessage());
        }
        return null;
    }

    /**
     *
     * @param search
     * @param createDateFrom
     * @param createDateTo
     * @return total rows
     */
    public double getCount(String search,Date createDateFrom,Date createDateTo, int length, int start){
        LevelDAO dao = new LevelDAO();
        try {
            if (search == null && createDateFrom == null && createDateTo == null){
                return dao.getCount();
            }else {
                return dao.getCountSearch(search, createDateFrom, createDateTo,length,start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @param draw
     * @return
     */
    public LevelDTO search(int start, int length,String search,int column,String order,String createDateFrom,String createDateTo, int draw){
        LevelDTO dto = new LevelDTO();
        try{
            Date dateFrom =  DateSearchParse.parseDate(createDateFrom);
            Date dateTo =  DateSearchParse.parseDate(createDateTo, true);
            double count = getCount(search,dateFrom,dateTo,length,start);
            List<Level> listQuestion = listAll(start,length,search,column,order,dateFrom,dateTo);
            dto.setDraw(draw);
            dto.setRecordsFiltered(count);
            dto.setRecordsTotal(count);
            dto.setData(listQuestion);
        }catch (Exception e){
            dto.setMessage(ERROR + ": " + "search level error, because:" + e.getMessage());
            logger.error("search question error, because:" + e.getMessage());
        }
        return dto;
    }


    /**
     *
     * @param id
     * @return
     */
    public Level getById(String id){
        LevelDAO dao = new LevelDAO();
        try {
            return dao.getById(id);
        }catch (Exception e){
            logger.info("can not get level by id : " + id);
        }
        return null;
    }

    public boolean isDemoExisted(){
        LevelDAO dao = new LevelDAO();
        boolean check = false;
        try {
            check =  dao.checkIsDemoExisted();
        }catch (Exception e){
            logger.info("there are some thing with find level demo in database");
        }
        return check;
    }
}



