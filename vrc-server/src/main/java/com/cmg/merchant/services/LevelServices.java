package com.cmg.merchant.services;

import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.merchant.dao.course.CMLDAO;
import com.cmg.merchant.dao.level.LvDAO;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;

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
     * @param name, description,color
     * @return true if question was added to table.
     */
    public String addLevelToDB(String name, String description){
        LvDAO dao = new LvDAO();
        String message;
        try {
            Level lv = new Level();
            lv.setId(UUIDGenerator.generateUUID().toString());
            lv.setName(name);
            lv.setDescription(description);
            lv.setDateCreated(new Date(System.currentTimeMillis()));
            lv.setIsDeleted(false);
            lv.setVersion(getMaxVersion());
            lv.setIsDemo(false);
            lv.setIsDefaultActivated(false);
            dao.create(lv);
            message = lv.getId();
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
                    if(lv.getName().equalsIgnoreCase(name) && lv.getId().equals(idLevel)){
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
        if(removeMappingLevelFromCourse(idCourse,idLevel)){
            LvDAO dao = new LvDAO();
            if(dao.updateDeleted(idLevel)){
                return SUCCESS;
            }
        }
        return ERROR + ": an error has been occurred in server!";

    }





}
