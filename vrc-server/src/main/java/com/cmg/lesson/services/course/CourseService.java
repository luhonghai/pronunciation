package com.cmg.lesson.services.course;

import com.cmg.lesson.common.DateSearchParse;
import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.course.CourseMappingLevelDAO;
import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public CourseDTO listAll(){
        CourseDAO dao = new CourseDAO();
        CourseDTO dto = new CourseDTO();
        try {
            List<Course> list = dao.listAll();
            if(list!=null && list.size()>0){
                dto.setMessage(SUCCESS);
                dto.setData(list);
            }else{
                dto.setMessage(SUCCESS);
               dto.setData(new ArrayList<Course>());
            }
        }catch (Exception e){
            dto.setMessage(ERROR + ": can not list all because " + e.getMessage());
            logger.error("can not list all : " + e);
        }
        dto.setMessage(SUCCESS);
        return dto;
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
                course.setDateCreated(new Date(System.currentTimeMillis()));
                dao.create(course);
                message = SUCCESS;
            }else{
                message = ERROR + ":" + "This level has already been added";
            }
        }catch(Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("can not add course : " + name + " because : " + e.getMessage());
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
            Course course=dao.getById(id);
            if(course!=null) {
            String oldName = (String) StringUtil.isNull(dao.getById(id).getName(), "");
                if (oldName.equalsIgnoreCase(name)) {
                    boolean check = dao.updateCourse(id, name, description);
                    if (check) {
                        message = SUCCESS;
                    } else {
                        message = ERROR + ":" + " an error has been occurred in server!";
                    }
                } else {
                    boolean isExistedNewName = isExistCourseName(name);
                    if (isExistedNewName) {
                        message = ERROR + ":" + " This name already existed!";
                    } else {
                        boolean check = dao.updateCourse(id, name, description);
                        if (check) {
                            message = SUCCESS;
                        } else {
                            message = ERROR + ":" + " an error has been occurred in server!";
                        }
                    }
                }
            }else {
                message = "deleted";
            }
        }catch (Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("can not update level : " + name + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }


    /**
     *
     * @param id
     * @return
     */
    public CourseDTO deleteCourseToDB(String id){
        CourseDTO dto = new CourseDTO();
        CourseDAO dao = new CourseDAO();
        String message;
        try{
            Course course=dao.getById(id);
            if(course!=null) {
            boolean isDelete = dao.updateDeleted(id);
                if (isDelete) {
                    //need to be update to all mapping table
                    CourseMappingLevelDAO cmd = new CourseMappingLevelDAO();
                    cmd.removeCourseMapping(id);
                    message = SUCCESS;
                } else {
                    message = ERROR + ": " + "an error has been occurred in server!";
                }
            }else {
                message = "deleted";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("can not delete course id: " + id + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }



    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @param draw
     * @return
     */
    public CourseDTO search(int start, int length,String search,int column,String order,String course, String createDateFrom,String createDateTo, int draw){
        CourseDTO dto = new CourseDTO();
        try{
            Date dateFrom =  DateSearchParse.parseDate(createDateFrom);
            Date dateTo =  DateSearchParse.parseDate(createDateTo, true);
            double count = getCount(search,course,dateFrom,dateTo,length,start);
            List<Course> listCourse = listAll(start,length,search,column,order,course,dateFrom,dateTo);
            dto.setDraw(draw);
            dto.setRecordsFiltered(count);
            dto.setRecordsTotal(count);
            dto.setData(listCourse);
        }catch (Exception e){
            dto.setMessage(ERROR + ": " + "search course error, because:" + e.getMessage());
            logger.error("search course error, because:" + e.getMessage());
        }
        return dto;
    }



    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @return List<Question>
     */
    public List<Course> listAll(int start, int length,String search,int column,String order, String course, Date createDateFrom,Date createDateTo){
        CourseDAO dao = new CourseDAO();
        try{
            return dao.listAll(start, length, search,column, order,course, createDateFrom, createDateTo);
        }catch (Exception ex){
            logger.error("list all course error, because:" + ex.getMessage());
        }
        return null;
    }

    /**
     *
     * @param search
     * @param createDateFrom
     * @param createDateTo
     * @return total rows
     */
    public double getCount(String search,String course, Date createDateFrom,Date createDateTo, int length, int start){
        CourseDAO dao = new CourseDAO();
        try {
            if (search == null && course==null && createDateFrom == null && createDateTo == null){
                return dao.getCount();
            }else {
                return dao.getCountSearch(search, course, createDateFrom, createDateTo,length,start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }



}
