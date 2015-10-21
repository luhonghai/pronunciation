package com.cmg.lesson.services.course;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.course.CourseMappingLevelDAO;
import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import org.apache.log4j.Logger;

/**
 * Created by lantb on 2015-10-21.
 */
public class CourseMappingLevelService {
    private static final Logger logger = Logger.getLogger(CourseMappingLevelService.class
            .getName());
    private String SUCCESS = "success";
    private String ERROR = "error";


    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        CourseMappingLevelDAO dao = new CourseMappingLevelDAO();
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
    public int getMaxIndex(String idCourse){
        int index = 0;
        CourseMappingLevelDAO dao = new CourseMappingLevelDAO();
        try {
            index = dao.getLatestIndex(idCourse);
        }catch (Exception e){
            logger.info("can not get max index in table because : " + e.getMessage());
        }
        return index +1;
    }

    /**
     *
     * @param idCourse
     * @param idLevel
     * @return
     */
    public CourseDTO addMappingLevel(String idCourse, String idLevel){
        CourseDTO dto = new CourseDTO();
        CourseMappingLevelDAO dao = new CourseMappingLevelDAO();
        String message;
        try {
            boolean condition = checkExistedLevel(idCourse,idLevel);
            if(condition){
                CourseMappingLevel cml = new CourseMappingLevel();
                cml.setIdCourse(idCourse);
                cml.setIdLevel(idLevel);
                cml.setVersion(getMaxVersion());
                cml.setIndex(getMaxIndex(idCourse));
                cml.setIsDeleted(false);
                dao.create(cml);
                message = SUCCESS;
            }else{
                message = ERROR +": this level has already in this Course!";
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
     * @param idCourse
     * @param idLevel
     * @return
     */
    public CourseDTO removeLevel(String idCourse, String idLevel){
        CourseDTO dto = new CourseDTO();
        CourseMappingLevelDAO dao = new CourseMappingLevelDAO();
        String message;
        try {
            if(dao.removeLevel(idCourse,idLevel)){
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
     * @param idCourse
     * @param idLevel
     * @return
     */
    public boolean checkExistedLevel(String idCourse, String idLevel){
        CourseMappingLevelDAO dao = new CourseMappingLevelDAO();
        boolean check = false;
        try {
            check = dao.checkExist(idCourse,idLevel);
        }catch (Exception e){
            logger.error("can not check exited level : "  + e);
        }
        return check;
    }
}
