package com.cmg.lesson.services.course;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by lantb on 2015-10-21.
 */
public class CourseService {
    private static final Logger logger = Logger.getLogger(CourseService.class
            .getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        CourseDAO dao = new CourseDAO();
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
    public CourseDTO addCourseToDB(String name, String description){
        CourseDAO dao = new CourseDAO();
        CourseDTO dto = new CourseDTO();
        String message;
        try {
            if(!isExistCourseName(name)) {
                Course course = new Course();
                course.setName(name);
                course.setDescription(description);
                course.setVersion(getMaxVersion());
                course.setIsDeleted(false);
                dao.create(course);
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
    public boolean isExistCourseName(String name) throws Exception{
        boolean isExist = false;
        CourseDAO dao = new CourseDAO();
        isExist = dao.checkExist(name);
        return isExist;
    }


    /**
     *
     * @param id
     * @param name
     * @param description
     * @return
     */
    public CourseDTO updateCourse(String id, String name, String description){
        CourseDAO dao = new CourseDAO();
        CourseDTO dto = new CourseDTO();
        String message = null;
        try {
            String oldName = (String) StringUtil.isNull(dao.getById(id).getName(), "");
            if(oldName.equalsIgnoreCase(name)){
                boolean check = dao.updateCourse(id, name, description);
                if(check){
                    message = SUCCESS;
                }else{
                    message = ERROR + ":" + "an error has been occurred in server!";
                }
            }else{
                boolean isExistedNewName = isExistCourseName(name);
                if(isExistedNewName){
                    message = ERROR + ":" + "This name already existed!";
                }else{
                    boolean check = dao.updateCourse(id, name, description);
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

}
