package com.cmg.merchant.services;

import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.services.course.CourseService;
import com.cmg.merchant.dao.course.CMLDAO;
import com.cmg.merchant.dao.level.LVMODAO;
import com.cmg.merchant.dao.level.LvDAO;
import com.cmg.merchant.dao.objective.ODAO;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2016-02-23.
 */
public class LevelServices {
    private static final Logger logger = Logger.getLogger(LevelServices.class
            .getName());
    private String SUCCESS = "success";
    private String ERROR = "error";
    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        LvDAO dao = new LvDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public ArrayList<Level> getAllByCourseId(String idCourse){
        LvDAO dao = new LvDAO();
        try {
            ArrayList<Level> listAll = (ArrayList<Level>) dao.listIn(idCourse);
            return listAll;
        }catch (Exception e){

        }
        return null;
    }

    /**
     *
     * @param name, description,color
     * @return true if question was added to table.
     */
    public String addLevelToDB(String id,String name, String description){
        LvDAO dao = new LvDAO();
        String message;
        try {
            Level lv = new Level();
            lv.setId(id);
            lv.setName(name);
            lv.setDescription(description);
            lv.setDateCreated(new Date(System.currentTimeMillis()));
            lv.setIsDeleted(false);
            lv.setVersion(getMaxVersion());
            lv.setIsDemo(false);
            lv.setIsDefaultActivated(false);
            dao.create(lv);
            message = SUCCESS;
        }catch(Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("can not add level : " + name + " because : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idCourse
     * @param idLevel
     * @param name
     * @param description
     * @return
     */
    public String updateLevel(String idCourse, String idLevel, String name, String description){
        if(existedName(idCourse,idLevel,name)){
           return ERROR + ": name already existed!";
        }
        LvDAO dao = new LvDAO();
        try {
            boolean check  = dao.updateLevel(idLevel,name,description);
            if(!check){
                return ERROR + ": an error has been occurred in server!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR + ": an error has been occurred in server!";
        }
        return SUCCESS;
    }

    /**
     *
     * @param idCourse
     * @param idLevel
     * @param name
     * @return true if name is already added in the course
     */
    public boolean existedName (String idCourse, String idLevel, String name){
        LvDAO dao = new LvDAO();
        try {
            ArrayList<Level> listAll = (ArrayList<Level>) dao.listIn(idCourse);
            if(idLevel!=null){
                for(Level lv : listAll){
                    if(lv.getName().equalsIgnoreCase(name) && !lv.getId().equals(idLevel)){
                        return true;
                    }
                }
            }else{
                for(Level lv : listAll){
                    if(lv.getName().equalsIgnoreCase(name)){
                        return true;
                    }
                }
            }
        }catch (Exception e){
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean removeMappingLevelFromCourse(String idCourse, String idLevel){
        CMLDAO dao = new CMLDAO();
        try {
            boolean check = dao.removeLevel(idCourse,idLevel);
            if(check){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;

    }

    /**
     *
     * @param idCourse
     * @param idLevel
     * @return
     */
    public String deleteLevel(String idCourse, String idLevel){
        LvDAO dao = new LvDAO();
        try {
            removeMappingLevelFromCourse(idCourse, idLevel);
            dao.deleteStep1(idLevel);
            dao.deleteStep2(idLevel);
            return SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ERROR + ": an error has been occurred in server!";

    }

    /**
     *
     * @param idLevel
     * @param nameObj
     * @param descriptionObj
     * @return
     */
    public String addObjToLv(String idLevel, String nameObj, String descriptionObj){
        OServices oServices = new OServices();
        if(oServices.isExistedObjInLv(idLevel, nameObj, null)){
            return ERROR + ": name already existed in level!";
        }
        String idObj = UUIDGenerator.generateUUID().toString();
        String message = oServices.addObjToDB(idObj,nameObj,descriptionObj);
        if(message.equalsIgnoreCase(ERROR)){
            return ERROR + ": an error has been occurred in server!";
        }
        message = addMappingObjToLv(idLevel,idObj);
        return message;
    }

    /**
     *
     * @param idLevel
     * @param idObj
     * @return
     */
    public String addMappingObjToLv(String idLevel, String idObj){
        LVMODAO dao = new LVMODAO();
        try {
            CourseMappingDetail cmd = new CourseMappingDetail();
            cmd.setIsTest(false);
            cmd.setId(UUIDGenerator.generateUUID().toString());
            cmd.setVersion(getVersionForObj());
            cmd.setIdChild(idObj);
            cmd.setIdLevel(idLevel);
            cmd.setIndex(getIndexForObj(idLevel));
            cmd.setIsDeleted(false);
            boolean check = dao.create(cmd);
            if(!check){
                return ERROR + ": an error has been occurred in server!";
            }
        }catch (Exception e){e.printStackTrace();
            return ERROR + ": an error has been occurred in server!";
        }
        return SUCCESS;
    }


    /**
     *
     *
     * @return
     */
    public int getVersionForObj(){
        int version = 0;
        LVMODAO dao = new LVMODAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){

        }
        return version +1;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public int getIndexForObj(String idLevel){
        int index = 0;
        LVMODAO dao = new LVMODAO();
        try {
            index = dao.getMaxIndex(idLevel);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
        }
        return index +1;
    }

    /**
     *
     * @param idLevel
     * @param idObj
     * @return
     */
    public boolean removeMappingObjToLv(String idLevel, String idObj){
        LVMODAO dao = new LVMODAO();
        try {
            boolean check = dao.updateDeleted(idObj,idLevel);
            return check;
        }catch (Exception e){
            return false;
        }
    }

    /**
     *
     * @param idCourseMapping
     * @param idLevelNeedDuplicated
     * @return
     */
    public String copyLevel(String idCourseMapping,String idLevelNeedDuplicated, boolean newName){
        LvDAO dao = new LvDAO();
        CourseServices courseService = new CourseServices();
        try {
            Level lv = dao.getById(idLevelNeedDuplicated);
            if(lv!=null){
                Level tmp = new Level();
                String newId = UUIDGenerator.generateUUID().toString();
                tmp.setId(newId);
                if(newName){
                    tmp.setName("copy of " + lv.getName());
                }else{
                    tmp.setName(lv.getName());
                }
                tmp.setDateCreated(new Date(System.currentTimeMillis()));
                tmp.setVersion(getMaxVersion());
                tmp.setDescription(lv.getDescription());
                tmp.setIsDemo(false);
                tmp.setIsDefaultActivated(false);
                dao.create(tmp);
                courseService.addMappingLevel(idCourseMapping,newId);
                return newId;
            }
        }catch (Exception e){
            return ERROR;
        }
        return ERROR;
    }






}
