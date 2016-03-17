package com.cmg.merchant.services;

import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2016-03-16.
 */
public class QuestionServices {
    private static final Logger logger = Logger.getLogger(LessonServices.class.getName());
    private String ERROR = "error";
    private String SUCCESS = "success";
    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("Can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    public String addQuestionToLesson(String idObj, String name, String description,String type,String detail){
        String idLesson = UUIDGenerator.generateUUID().toString();
        if(isExistedLessonInObj(idObj, name, null)){
            return ERROR + ": name already existed in objective!";
        }
        String message = addQuestionToDB(idLesson, name, description, type, detail);
        if(message.equalsIgnoreCase(ERROR)){
            return ERROR + ": an error has been occurred in server!";
        }
        message = addMappingQuestionToLesson(idObj, idLesson);
        return message;
    }

    public String addQuestionToDB(String id, String name, String description,String type,String detail){
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


    public String addMappingQuestionToLesson(String idObj, String idLesson ){
        OMLDAO dao = new OMLDAO();
        try {
            ObjectiveMapping objectiveMapping = new ObjectiveMapping();
            objectiveMapping.setId(UUIDGenerator.generateUUID().toString());
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
}

