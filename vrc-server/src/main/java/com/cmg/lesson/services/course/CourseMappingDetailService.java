package com.cmg.lesson.services.course;


import com.cmg.lesson.dao.course.CourseMappingDetailDAO;
import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.services.level.LevelService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-10-21.
 */
public class CourseMappingDetailService {
    private static final Logger logger = Logger.getLogger(CourseMappingDetailService.class
            .getName());
    private String SUCCESS = "success";
    private String ERROR = "error";


    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param idObjective
     * @param idLevel
     * @return
     */
    public String addMappingDetail(String idObjective, String idLevel,int index, boolean isTest){
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        String message;
        try {
            boolean condition = checkExist(idObjective, idLevel);
            if(!condition){
                CourseMappingDetail obj = new CourseMappingDetail();
                //obj.setIdCourse(idCourse);
                obj.setIdLevel(idLevel);
                obj.setIdChild(idObjective);
                obj.setIndex(index);
                obj.setVersion(getMaxVersion());
                obj.setIsTest(isTest);
                obj.setIsDeleted(false);
                dao.create(obj);
                message = SUCCESS;
            }else{
                message = ERROR +": can not add MappingDetail!";
            }
        }catch (Exception e){
            message = ERROR +": An error has been occurred in server!" + e.getMessage();
            logger.error("can not add mapping because : " + e.getMessage());
        }

        return message;
    }

    public String updateMappingDetail(String idObjective, String idLevel,int index){
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        String message;
        try {
            boolean condition = checkExistisUpdate(idObjective, idLevel);
            if(!condition){
                CourseMappingDetail obj =dao.getByIdObjAndIdLevel(idObjective,idLevel);
                //obj.setIdCourse(idCourse);
                obj.setIndex(index);
                dao.put(obj);
                message = SUCCESS;
            }else{
                message = ERROR +": can not update MappingDetail!";
            }
        }catch (Exception e){
            message = ERROR +": An error has been occurred in server!" + e.getMessage();
            logger.error("can not add mapping because : " + e.getMessage());
        }

        return message;
    }

    /**
     *
     * @param idCourse
     * @param idLevel
     * @return
     */
    public CourseDTO removeDetail(String idCourse, String idObjective, String idLevel){
        CourseDTO dto = new CourseDTO();
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        String message;
        try {
            if(dao.updateDeleted(idCourse, idObjective, idLevel)){
                message = SUCCESS;
            }else{
                message = ERROR +": An error has been occurred in server!";
            }
        }catch (Exception e){
            message = ERROR +": An error has been occurred in server!";
            logger.error("can not add mapping because : " + e);
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param idLevel
     * @param idObjective
     * @return
     */
    public String removeDetail(String idObjective, String idLevel){
        CourseDTO dto = new CourseDTO();
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        String message;
        try {
            if(dao.updateDeleted(idObjective, idLevel)){
                message = SUCCESS;
            }else{
                message = ERROR +": An error has been occurred in server!";
            }
        }catch (Exception e){
            message = ERROR +": An error has been occurred in server!";
            logger.error("can not add mapping because : " + e);
        }
        return message;
    }


    /**
     *
     * @param idLevel
     * @return
     */
    public String removeDetail( String idLevel){
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        String message;
        try {
            if(dao.updateDeleted(idLevel)){
                message = SUCCESS;
            }else{
                message = ERROR +": An error has been occurred in server!";
            }
        }catch (Exception e){
            message = ERROR +": An error has been occurred in server!" + e.getMessage();
            logger.error("can not add mapping because : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idChild
     * @return
     */
    public String removeDetailByIdChild( String idChild){
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        String message;
        try {
            if(dao.updateDeletedByIdidChild(idChild)){
                message = SUCCESS;
            }else{
                message = ERROR +": An error has been occurred in server!";
            }
        }catch (Exception e){
            message = ERROR +": An error has been occurred in server!" + e.getMessage();
            logger.error("can not add mapping because : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idObjective
     * @param idLevel
     * @return
     */
    public boolean checkExist(String idObjective, String idLevel){
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        boolean check = false;
        try {
            check = dao.checkExist(idObjective, idLevel);
        }catch (Exception e){
            logger.error("can not check exited: "  + e.getMessage());
        }
        return check;
    }

    public boolean checkExistisUpdate(String idObjective, String idLevel){
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        boolean check = false;
        try {
            check = dao.checkExistisUpdate(idObjective, idLevel);
        }catch (Exception e){
            logger.error("can not check exited: "  + e.getMessage());
        }
        return check;
    }


    public boolean checkExistTest(String idLevel){
        CourseMappingDetailDAO dao = new CourseMappingDetailDAO();
        try {
            return dao.checkExistTest(idLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
