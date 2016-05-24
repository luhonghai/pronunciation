package com.cmg.merchant.services;

import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.dao.objective.ODAO;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2016-02-25.
 */
public class OServices {
    private static final Logger logger = Logger.getLogger(OServices.class.getName());
    private String ERROR = "error";
    private String SUCCESS = "success";
    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        ObjectiveDAO dao = new ObjectiveDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("Can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param name
     * @param description
     * @return
     */
    public String addObjToDB(String id,String name, String description){
        ODAO dao = new ODAO();
        String message;
        try {
            Objective obj = new Objective();
            obj.setId(id);
            obj.setName(name);
            obj.setDescription(description);
            obj.setVersion(getMaxVersion());
            obj.setDateCreated(new Date(System.currentTimeMillis()));
            obj.setIsDeleted(false);
            dao.create(obj);
            message = id;
        }catch(Exception e){
            message = ERROR;
            logger.error("Can not add Objective : " + name + " because : " + e.getMessage());
        }

        return message;
    }


    /**
     *
     * @param idLevel
     * @param idObj
     * @param name
     * @param description
     * @return
     */
    public String updateObj(String idLevel, String idObj,String name, String description){
        ODAO dao = new ODAO();
        if(isExistedObjInLv(idLevel,name,idObj)){
            return ERROR + ": You already have an objective with that name in this level";
        }
        try {
            boolean check = dao.updateObjective(idObj,name,description);
            if(!check){
                return ERROR + ": an error has been occurred in server";
            }
        }catch (Exception e){
            return ERROR + ": an error has been occurred in server";
        }
        return SUCCESS;
    }

    /**
     *
     * @param idLevel
     * @param idObj
     * @return
     */
    public String deleteObj(String idLevel, String idObj){
        ODAO dao = new ODAO();
        LevelServices lvServices = new LevelServices();
        try {
            boolean check = lvServices.removeMappingObjToLv(idLevel, idObj);
            if(check){
                check = dao.deletedObjective(idObj);
                if(!check){
                    return ERROR + ": an error has been occurred in server";
                }
            }else{
                return ERROR + ": an error has been occurred in server";
            }



        }catch (Exception e){
            return ERROR + ": an error has been occurred in server";
        }
        return SUCCESS;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public ArrayList<Objective> getAllByLevelId(String idLevel){
        ODAO dao = new ODAO();
        try {
            ArrayList<Objective> list = (ArrayList)dao.getAllByIdLevel(idLevel);
            return list;
        }catch (Exception e){

        }
        return null;
    }


    /**
     *
     * @param idLevel
     * @param nameObj
     * @param idObj
     * @return true if name is existed
     */
    public boolean isExistedObjInLv(String idLevel, String nameObj, String idObj){
        ODAO dao = new ODAO();
        try {
            List<Objective> list = dao.getAllByIdLevel(idLevel);
            if(idObj!=null){
                for(Objective obj : list){
                    if(obj.getName().equalsIgnoreCase(nameObj) && !obj.getId().equals(idObj)){
                        return true;
                    }
                }
            }else{
                for(Objective obj : list){
                    if(obj.getName().equalsIgnoreCase(nameObj)){
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
     * @param idLevelMapping
     * @param idObjNeedDuplicated
     * @return
     */
    public String copyObj(String idLevelMapping, String idObjNeedDuplicated, boolean newName){
        ODAO dao = new ODAO();
        try {
            Objective obj = dao.getById(idObjNeedDuplicated);
            if(obj!=null){
                Objective tmp = new Objective();
                String newId = UUIDGenerator.generateUUID().toString();
                tmp.setId(newId);
                if(newName) {
                    tmp.setName("copy of " + obj.getName());
                    tmp.setIsCopied(true);
                }else{
                    tmp.setName(obj.getName());
                    tmp.setIsCopied(false);
                }
                tmp.setDescription(obj.getDescription());
                tmp.setIsDeleted(false);
                tmp.setVersion(obj.getVersion());
                tmp.setDateCreated(new Date(System.currentTimeMillis()));
                dao.create(tmp);
                LevelServices lvServices = new LevelServices();
                lvServices.addMappingObjToLv(idLevelMapping,newId);
                return newId;
            }
        }catch (Exception e){
            return ERROR;
        }
        return ERROR;
    }
}
