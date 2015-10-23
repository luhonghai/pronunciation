package com.cmg.lesson.services.objectives;

import com.cmg.lesson.common.DateSearchParse;
import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.services.course.CourseMappingDetailService;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/14/2015.
 */
public class ObjectiveService {
    private static final Logger logger = Logger.getLogger(ObjectiveService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

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
     * @param id
     * @param name
     * @param description
     * @return
     */
    public ObjectiveDTO updateObjective(String id, String name, String description, boolean isUpdateLessonName){
        ObjectiveDTO dto = new ObjectiveDTO();
        ObjectiveDAO dao = new ObjectiveDAO();
        String message;
        try{
            if(isUpdateLessonName) {
                if (!isExistLessonName(name)) {
                    boolean isUpdate = dao.updateObjective(id, name, description);
                    if (isUpdate) {
                        message = SUCCESS;
                    } else {
                        message = ERROR + ":" + "An error has been occurred in server!";
                    }
                } else {
                    message = ERROR + ":" + "Objective name is existed";
                }
            }else {
                boolean isUpdate = dao.updateDescription(id, description);
                if (isUpdate) {
                    message = SUCCESS;
                } else {
                    message = ERROR + ":" + "An error has been occurred in server!";
                }
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("Can not update Objective : " + name + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param name
     * @return true if question was added to table.
     */
    public String addObjective(String id,String name, String description){
        ObjectiveDAO dao = new ObjectiveDAO();
        String message;
        try {
            if(!isExistLessonName(name)) {
                Objective obj = new Objective();
                obj.setId(id);
                obj.setName(name);
                obj.setDescription(description);
                obj.setVersion(getMaxVersion());
                obj.setDateCreated(new Date(System.currentTimeMillis()));
                obj.setIsDeleted(false);
                dao.create(obj);
                message = SUCCESS;
            }else{
                message = ERROR + ":" + "Objective name is existed";
            }
        }catch(Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("Can not add Objective : " + name + " because : " + e.getMessage());
        }

        return message;
    }


    /**
     *
     * @param dto
     * @return
     */
    public ObjectiveMappingDTO addObjective(ObjectiveMappingDTO dto){
        String idObjective = UUIDGenerator.generateUUID();
        String message = addObjective(idObjective,dto.getNameObj(),dto.getDescriptionObj());
        if(message.equalsIgnoreCase(SUCCESS)){
            dto.setIdObjective(idObjective);
            ObjectiveMappingService objMapSer = new ObjectiveMappingService();
            message =  objMapSer.addObjMapLesson(dto.getIdLessons(),dto.getIdObjective());
            if(message.equalsIgnoreCase(SUCCESS)){
                CourseMappingDetailService cmdSer = new CourseMappingDetailService();
                message = cmdSer.addMappingDetail(dto.getIdCourse(),dto.getIdObjective(),dto.getIdLevel(),false);
            }
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     * @return
     */
    public ObjectiveDTO deleteObjective(String id){
        ObjectiveDTO dto = new ObjectiveDTO();
        ObjectiveDAO dao = new ObjectiveDAO();
        String message;
        try{
            boolean isDelete=dao.deletedObjective(id);
            if (isDelete){
                message = SUCCESS;
            }else{
                message = ERROR + ": " + "An error has been occurred in server!";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("Can not delete Objective id: " + id + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param name
     * @return true is exits question name
     */
    public boolean isExistLessonName(String name) throws Exception{
        boolean isExist = false;
        ObjectiveDAO dao = new ObjectiveDAO();
        isExist = dao.checkExist(name);
        return isExist;
    }

    /**
     *
     * @param id
     * @return
     */
    public Objective getById(String id){
        ObjectiveDAO dao = new ObjectiveDAO();
        try {
            return dao.getById(id);
        }catch (Exception e){
            logger.info("can not get objective by id : " + id);
        }
        return null;
    }

    /**
     *
     * @return total rows
     */
    public double getCount(){
        ObjectiveDAO dao = new ObjectiveDAO();
        try{
            return dao.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }


    public CourseDTO getAllObj(String idCourse, String idLevel){
        CourseDTO dto = new CourseDTO();
        return dto;
    }

}
