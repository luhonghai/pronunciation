package com.cmg.merchant.services;

import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.merchant.dao.lessons.LDAO;
import com.cmg.merchant.dao.objective.ODAO;
import com.cmg.merchant.dao.objective.OMLDAO;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2016-02-25.
 */
public class LessonServices {
    private static final Logger logger = Logger.getLogger(LessonServices.class.getName());
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


    public String addLessonToObj(String idObj, String name, String description,String type,String detail){
        String idLesson = UUIDGenerator.generateUUID().toString();
        if(isExistedLessonInObj(idObj, name, null)){
            return ERROR + ": You already have a lesson with this name in your objective";
        }
        String message = addLessonToDB(idLesson,name,description,type,detail);
        if(message.equalsIgnoreCase(ERROR)){
            return ERROR + ": an error has been occurred in server";
        }
        message = addMappingLessonToObj(idObj, idLesson);
        return message;
    }

    public String addLessonToDB(String id, String name, String description,String type,String detail){
        LDAO dao = new LDAO();
        String message;
        try {
            LessonCollection lesson = new LessonCollection();
            lesson.setId(id);
            lesson.setName(name);
            lesson.setNameUnique(detail);
            lesson.setType(type);
            lesson.setDescription(description);
            lesson.setVersion(getMaxVersion());
            lesson.setDateCreated(new Date(System.currentTimeMillis()));
            lesson.setIsDeleted(false);
            dao.create(lesson);
            message = id;
        }catch(Exception e){
            message = ERROR;
            logger.error("Can not add Objective : " + name + " because : " + e.getMessage());
        }

        return message;
    }


    public String addMappingLessonToObj(String idObj, String idLesson ){
        OMLDAO dao = new OMLDAO();
        try {
            ObjectiveMapping objectiveMapping = new ObjectiveMapping();
            objectiveMapping.setId(UUIDGenerator.generateUUID().toString());
            objectiveMapping.setVersion(getVersionForLesson());
            objectiveMapping.setIdObjective(idObj);
            objectiveMapping.setIdLessonCollection(idLesson);
            objectiveMapping.setIndex(getIndexForLesson(idObj));
            objectiveMapping.setIsDeleted(false);
            boolean check = dao.create(objectiveMapping);
            if(!check){
                return ERROR + ": an error has been occurred in server!";
            }
        }catch (Exception e){e.printStackTrace();
            return ERROR + ": an error has been occurred in server!";
        }
        return SUCCESS;
    }
    public int getVersionForLesson(){
        int version = 0;
        OMLDAO dao = new OMLDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){

        }
        return version +1;
    }

    public int getIndexForLesson(String idObj){
        int index = 0;
        OMLDAO dao = new OMLDAO();
        try {
            index = dao.getMaxIndex(idObj);
        }catch (Exception e){
            //e.printStackTrace();
            logger.error(e);
        }
        return index +1;
    }





    /**
     *
     * @param idLesson
     * @param idObj
     * @param name
     * @param description
     * @return
     */
    public String updateLesson(String idObj,String idLesson,String name, String description,String type,String detail){
        LDAO dao = new LDAO();
        LessonCollection lessonCollection=new LessonCollection();
        try {
            lessonCollection=dao.getById(idLesson);
            if(lessonCollection!=null) {
                if (isExistedLessonInObj(idLesson, name, idObj)) {
                    return ERROR + ": You already have a lesson with that name in this objective'";
                }
                boolean check = dao.updateLesson(idLesson, name, description, type, detail);
                if (!check) {
                    return ERROR + ": an error has been occurred in server";
                }
            }else {
                return ERROR + ": This lesson has been already deleted";
            }
        }catch (Exception e){
            return ERROR + ": an error has been occurred in server";
        }
        return SUCCESS;
    }

    /**
     *
     * @param idLesson
     * @param idObj
     * @return
     */
    public String deleteLesson(String idObj, String idLesson ){
        LDAO dao = new LDAO();
        try {
            boolean check = dao.removeMappingLessonWithObj(idObj, idLesson);
            if(!check){
                return ERROR + ": an error has been occurred in server";
            }

            check = dao.deletedLesson(idLesson);
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
     * @param idLesson
     * @param name
     * @param idObj
     * @return true if name is existed
     */
    public boolean isExistedLessonInObj(String idLesson, String name, String idObj){
        LDAO dao = new LDAO();
        try {
            List<LessonCollection> list = dao.getAllByIdObj(idObj);
            if(idLesson!=null){
                for(LessonCollection lesson : list){
                    if(lesson.getName().equalsIgnoreCase(name) && !lesson.getId().equals(idLesson)){
                        return true;
                    }
                }
            }else{
                for(LessonCollection lesson : list){
                    if(lesson.getName().equalsIgnoreCase(name)){
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
     * @param objId
     * @return
     */
    public ArrayList<LessonCollection> getAllByObjId(String objId){
        LDAO dao = new LDAO();
        try {
            ArrayList<LessonCollection> list = (ArrayList<LessonCollection>) dao.getAllByIdObj(objId);
            return list;
        }catch (Exception e){

        }
        return null;
    }

    /**
     *
     * @param testId
     * @return
     */
    public LessonCollection getByTestId(String testId){
        LDAO dao = new LDAO();
        try {
            LessonCollection lesson  =  dao.getAllByIdTest(testId);
            return lesson;
        }catch (Exception e){

        }
        return null;
    }
    /**
     *
     * @param idObjMapping
     * @return
     */
    public String copyLessonInObj(String idObjMapping, String idLessonNeedDuplicated, boolean newName){
        LDAO ldao = new LDAO();
        try {
            LessonCollection lesson = ldao.getById(idLessonNeedDuplicated);
            if(lesson!=null){
                LessonCollection tmp = new LessonCollection();
                String newId = UUIDGenerator.generateUUID().toString();
                tmp.setId(newId);
                if(newName){
                    tmp.setName("copy of " + lesson.getName());
                }else{
                    tmp.setName(lesson.getName());
                }
                tmp.setDescription(lesson.getDescription());
                tmp.setNameUnique(lesson.getNameUnique());
                tmp.setTitle(lesson.getTitle());
                tmp.setType(lesson.getType());
                tmp.setIsDeleted(false);
                tmp.setVersion(lesson.getVersion());
                tmp.setDateCreated(new Date(System.currentTimeMillis()));
                ldao.create(tmp);
                addMappingLessonToObj(idObjMapping,newId);
                return newId;
            }
        }catch (Exception e){
            return ERROR;
        }
        return ERROR;
    }


    /**
     *
     * @param idTestMapping
     * @param idLessonNeedDuplicated
     * @return
     */
    public String copyLessonInTest(String idTestMapping, String idLessonNeedDuplicated){
        LDAO ldao = new LDAO();
        try {
            LessonCollection lesson = ldao.getById(idLessonNeedDuplicated);
            if(lesson!=null){
                LessonCollection tmp = new LessonCollection();
                String newId = UUIDGenerator.generateUUID().toString();
                tmp.setId(newId);
                tmp.setNameUnique("");
                tmp.setTitle("");
                tmp.setName("");
                tmp.setDescription("");
                tmp.setVersion(lesson.getVersion());
                tmp.setDateCreated(new Date(System.currentTimeMillis()));
                tmp.setIsDeleted(false);
                ldao.create(tmp);
                TestServices testServices = new TestServices();
                testServices.addTESTMappingLESSON(idTestMapping,newId);
                return newId;
            }
        }catch (Exception e){
            return ERROR;
        }
        return ERROR;
    }

}
