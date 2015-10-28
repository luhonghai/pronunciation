package com.cmg.lesson.services.objectives;

import com.cmg.lesson.dao.course.CourseMappingDetailDAO;
import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveDTO;
import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.services.course.CourseMappingDetailService;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
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
                if (!isExistObjectiveName(name)) {
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
     * @param dto
     * @return
     */
    public ObjectiveMappingDTO updateObjective(ObjectiveMappingDTO dto){
        String idObjective = dto.getIdObjective();
        ObjectiveDAO dao = new ObjectiveDAO();
        String message="";
        try {
            boolean isUpdate = dao.updateObjective(idObjective, dto.getNameObj(), dto.getDescriptionObj());
            if (isUpdate){
                ObjectiveMappingService objMapSer = new ObjectiveMappingService();
                objMapSer.updateDeleted(idObjective);
                message =  objMapSer.addObjMapLesson(dto.getIdLessons(),dto.getIdObjective());
            }
        } catch (Exception e) {
            message = ERROR + ": "+ e.getMessage();
            logger.error("Can not update Objective : " +  dto.getNameObj() + " because : " + e.getMessage());
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
            //if(!isExistObjectiveName(name)) {
                Objective obj = new Objective();
                obj.setId(id);
                obj.setName(name);
                obj.setDescription(description);
                obj.setVersion(getMaxVersion());
                obj.setDateCreated(new Date(System.currentTimeMillis()));
                obj.setIsDeleted(false);
                dao.create(obj);
                message = SUCCESS;
            //}else{
                //message = ERROR + ":" + "Objective name is existed";
            //}
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
     * @param id
     * @return
     */
    public ObjectiveDTO deleteObjectiveAndLesson(String id){
        ObjectiveDTO dto = deleteObjective(id);
        if (dto.getMessage().equalsIgnoreCase("success")){
            CourseMappingDetailService courseMappingDetailService = new CourseMappingDetailService();
            String message = courseMappingDetailService.removeDetailByIdChild(id);
            if(message.equalsIgnoreCase("success")){
                ObjectiveMappingService objectiveMappingService = new ObjectiveMappingService();
                boolean isDelete = objectiveMappingService.updateDeleted(id);
                if (isDelete) {
                    dto.setMessage(SUCCESS);
                }else {
                    dto.setMessage(ERROR + ": can not delete lesson of objective id, "+id);
                }
            }else {
                dto.setMessage(message);
            }
        }
        return dto;
    }

    /**
     *
     * @param name
     * @return true is exits question name
     */
    public boolean isExistObjectiveName(String name) throws Exception{
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


    public LevelDTO getAllObjAndTest(String idCourse, String idLevel){
        LevelDTO levelDTO = new LevelDTO();
        CourseMappingDetailDAO courseMappingDetailDAO = new CourseMappingDetailDAO();
        ObjectiveDAO objectiveDAO = new ObjectiveDAO();
        List<String> lstObjId = new ArrayList<String>();
        List<String> lstTestId = new ArrayList<String>();
        try {
            List<CourseMappingDetail> listCourseMappingDetails = courseMappingDetailDAO.getAllByLevel(idCourse, idLevel);
            if(listCourseMappingDetails!=null && listCourseMappingDetails.size()>0){
                for(CourseMappingDetail cmp : listCourseMappingDetails){
                    if (cmp.isTest()){
                        lstTestId.add(cmp.getIdChild());
                    }else{
                        lstObjId.add(cmp.getIdChild());
                    }
                }
                List<Objective> listObjective = objectiveDAO.listIn(lstObjId);
                if(listObjective!=null && listObjective.size()>0){
                    List<ObjectiveMappingDTO> listObjectiveMappingDTO = new ArrayList<ObjectiveMappingDTO>();
                    ObjectiveMappingDTO objectiveMappingDTO;
                    for(Objective obj : listObjective){
                        objectiveMappingDTO = new  ObjectiveMappingDTO();
                        objectiveMappingDTO.setIdObjective(obj.getId());
                        objectiveMappingDTO.setIdLevel(idLevel);
                        objectiveMappingDTO.setIdCourse(idCourse);
                        objectiveMappingDTO.setNameObj(obj.getName());
                        objectiveMappingDTO.setDescriptionObj(obj.getDescription());
                        listObjectiveMappingDTO.add(objectiveMappingDTO);
                    }
                    levelDTO.setListObjMap(listObjectiveMappingDTO);
                    levelDTO.setMessage(SUCCESS);
                }
            }else{
                levelDTO.setMessage(SUCCESS);
                levelDTO.setListObjMap(new ArrayList<ObjectiveMappingDTO>());
            }
        } catch (Exception e) {
            levelDTO.setMessage(ERROR + " : can not get list objectives by level because " + e.getMessage());
            logger.error("List object by id level : " + idLevel + " false because : " + e.getMessage());
            e.printStackTrace();
        }
        return levelDTO;
    }

}
