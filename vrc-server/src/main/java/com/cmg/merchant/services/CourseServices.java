package com.cmg.merchant.services;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.course.CourseMappingLevelDAO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.company.CPDAO;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.course.CMLDAO;
import com.cmg.merchant.dao.lessons.LMODAO;
import com.cmg.merchant.dao.level.LvDAO;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.dao.objective.ODAO;
import com.cmg.merchant.dao.test.TDAO;
import com.cmg.merchant.dao.test.TMLDAO;
import com.cmg.merchant.data.dto.CourseDTO;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
import com.cmg.merchant.services.treeview.DataServices;
import com.cmg.merchant.util.SessionUtil;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2016-02-02.
 */
public class CourseServices {
    private static final Logger logger = Logger.getLogger(CourseServices.class
            .getName());
    public String SUCCESS = "success";
    public String ERROR = "error";

    /**
     * use for get max version
     *
     * @return max version in table
     */
    public int getMaxVersion() {
        int version = 0;
        CDAO dao = new CDAO();
        try {
            version = dao.getLatestVersion();
        } catch (Exception e) {
            logger.info("can not get max version in table because : " + e.getMessage());
        }
        return version + 1;
    }

    /**
     * @param idCourse
     * @param nameLv
     * @param descriptionLv
     * @return
     */
    public String addLevelToCourse(String idCourse, String nameLv, String descriptionLv) {
        LevelServices lvServices = new LevelServices();
        if(lvServices.existedName(idCourse,null,nameLv)){
           return ERROR + " : You already have a level with this name in your course";
        }
        String idLevel = UUIDGenerator.generateUUID().toString();
        String message = lvServices.addLevelToDB(idLevel,nameLv, descriptionLv);
        if (message.indexOf(ERROR) != -1) {
            return ERROR + " : an error has been occurred in server";
        }
        message = addMappingLevel(idCourse, idLevel);
        if(message.equalsIgnoreCase(SUCCESS)){
            message = SUCCESS + ":" + idLevel;
        }
        return message;
    }

    /**
     * @param idCourse
     * @param idLevel
     * @return
     */
    public String addMappingLevel(String idCourse, String idLevel) {
        CMLDAO dao = new CMLDAO();
        String message;
        try {
            CourseMappingLevel cml = new CourseMappingLevel();
            cml.setIdCourse(idCourse);
            cml.setIdLevel(idLevel);
            cml.setVersion(getMaxVersionMappingLevel());
            cml.setIndex(getMaxIndexMappingLevel(idCourse));
            cml.setIsDeleted(false);
            dao.create(cml);
            message = SUCCESS;
        } catch (Exception e) {
            message = ERROR + ": An error has been occurred in server!";
            logger.error("can not add mapping because : " + e);
        }
        return message;
    }

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersionMappingLevel(){
        int version = 0;
        CMLDAO dao = new CMLDAO();
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
    public int getMaxIndexMappingLevel(String idCourse){
        int index = 0;
        CMLDAO dao = new CMLDAO();
        try {
            index = dao.getLatestIndex(idCourse);
        }catch (Exception e){
            logger.info("can not get max index in table because : " + e.getMessage());
        }
        return index +1;
    }


    /**
     * @param course
     * @return
     */
    public ArrayList<String> suggestionCourse(String course) {
        CMTDAO dao = new CMTDAO();
        ArrayList<String> listSuggestion = new ArrayList<>();
        try {
            List<CourseDTO> courses = dao.suggestCourse(Constant.STATUS_PUBLISH, course);
            if (courses != null && courses.size() > 0) {
                for (CourseDTO c : courses) {
                    listSuggestion.add(c.getNameCourse());
                }
            }
        } catch (Exception e) {
            logger.error("can not retrieve data for suggestion course : " + e);
        }
        return listSuggestion;
    }

    /**
     * @param name
     * @param description
     * @param share
     * @return
     */
    public String addCourse(String name, String description, String share, HttpServletRequest request) {
        String result = SUCCESS;
        CDAO cDao = new CDAO();
        CMTDAO cmtDao = new CMTDAO();
        SessionUtil util = new SessionUtil();
        String cId = UUIDGenerator.generateUUID().toString();
        try {
            Course c = new Course(cId, name, description, false,
                    getMaxVersion(), new Date(System.currentTimeMillis()));
            boolean check = cDao.create(c);
            if (check) {
                CourseMappingTeacher cmt = new CourseMappingTeacher();
                cmt.setCpID(util.getCpId(request));
                cmt.settID(util.getTid(request));
                cmt.setcID(cId);
                cmt.setSr(share);
                cmt.setStatus(Constant.STATUS_NOT_PUBLISH);
                cmt.setState(Constant.STATE_CREATED);
                cmt.setDateCreated(new Date(System.currentTimeMillis()));
                cmt.setIsDeleted(false);
                cmt.setCpIdClone(Constant.DEFAULT_VALUE_CLONE);
                cmt.setcIdClone(Constant.DEFAULT_VALUE_CLONE);
                check = cmtDao.create(cmt);
                if (!check) {
                    result = ERROR + ": An error has been occurred in server";
                }
            } else {
                result = ERROR + ": An error has been occurred in server";
            }
        } catch (Exception e) {
            logger.error("can not add course : " + e);
            result = ERROR + ": An error has been occurred in server";
        }
        return cId;
    }

    /**
     * @param idCourse
     * @return
     */
    public String getCourseName(String idCourse) {
        CDAO cDao = new CDAO();
        String name = null;
        try {
            name = cDao.getById(idCourse).getName();
        } catch (Exception e) {
            logger.error("can not get name of course : " + e);
        }
        return name;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public String getCompanyCreatedCourse(String idCourse){
        CMTDAO dao = new CMTDAO();
        CPDAO cpDao = new CPDAO();
        try {
            CourseMappingTeacher cmt = dao.getByIdCourse(idCourse);
            String idCompany = cmt.getCpID();
            return cpDao.getById(idCompany).getCompanyName();
        }catch (Exception e){}
        return null;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public boolean isPublishCourse(String idCourse){
        CMTDAO dao = new CMTDAO();
        try {
            String status = dao.getByIdCourse(idCourse).getStatus();
            if(status.equalsIgnoreCase(Constant.STATUS_PUBLISH)){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public String deleteCourse(String idCourse){
        CDAO cDao = new CDAO();
        CMTDAO mapDao = new CMTDAO();
        try {
            if(mapDao.removeMappingCourse(idCourse)){
                cDao.deleteCourseStep1(idCourse);
                cDao.deleteCourseStep2(idCourse);
                return SUCCESS;
            }
        } catch (Exception e) {
            logger.error("can not get name of course : " + e);
        }
        return ERROR + ": an error has been occurred in server";
    }

    /**
     *
     * @param idCourse
     * @param name
     * @param description
     * @return
     */
    public String updateCourse(String idCourse, String name, String description){
        CDAO cDao = new CDAO();
        try {
            boolean check = cDao.updateCourse(idCourse, name, description);
            if(check){
                return SUCCESS;
            }
        } catch (Exception e) {
            logger.error("can not get name of course : " + e);
        }
        return ERROR + ": an error has been occurred in server";
    }

    /**
     *
     * @param idCourseNeedDuplicated
     * @return
     */
    public String copyCourse(String idCourseNeedDuplicated, HttpServletRequest request){
        CDAO cDao = new CDAO();
        CMTDAO cmtDao = new CMTDAO();
        SessionUtil util = new SessionUtil();
        try {
            Course c = cDao.getById(idCourseNeedDuplicated);
            CourseMappingTeacher cmtNeedDuplicated = cmtDao.getByIdCourse(idCourseNeedDuplicated);
            if(c!=null && cmtNeedDuplicated !=null){
                String newId = UUIDGenerator.generateUUID().toString();
                String newName = "Copy of " + c.getName();
                Course newCourse = new Course(newId, newName, c.getDescription(), false,
                        getMaxVersion(), new Date(System.currentTimeMillis()));
                boolean check = cDao.create(newCourse);
                if (check) {
                    CourseMappingTeacher cmt = new CourseMappingTeacher();
                    cmt.setCpID(util.getCpId(request));
                    cmt.settID(util.getTid(request));
                    cmt.setcID(newId);
                    cmt.setSr(Constant.SHARE_PRIVATE);
                    cmt.setStatus(Constant.STATUS_NOT_PUBLISH);
                    cmt.setState(Constant.STATE_DUPLICATED);
                    cmt.setDateCreated(new Date(System.currentTimeMillis()));
                    cmt.setIsDeleted(false);
                    cmt.setCpIdClone(cmtNeedDuplicated.getCpID());
                    cmt.setcIdClone(cmtNeedDuplicated.getcID());
                    check = cmtDao.create(cmt);
                    if (!check) {
                        return ERROR + ": An error has been occurred in server";
                    }
                } else {
                    return  ERROR + ": An error has been occurred in server";
                }
                return newId;
            }
        }catch (Exception e){
            return ERROR;
        }
        return ERROR;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public String enablePublishButton(String idCourse){
        LvDAO dao = new LvDAO();
        try {
            ArrayList<Level> listLv = (ArrayList<Level>) dao.listIn(idCourse);
            if(listLv!=null && listLv.size()>0){
                for(Level lv : listLv){
                    boolean existedInTest = dao.checkQuestionTestInLevel(lv.getId());
                    boolean existedInObj = dao.checkQuestionObjInLevel(lv.getId());
                    if(!existedInObj || !existedInTest ){
                        return ERROR;
                    }
                }
            }else{
                return ERROR;
            }

        }catch (Exception e){
            return ERROR;
        }
        return SUCCESS;
    }

    public String enableAddLvButton(String idCourse){
        LvDAO dao = new LvDAO();
        DataServices services = new DataServices();
        try {
            ArrayList<Level> listLv = (ArrayList<Level>) dao.listIn(idCourse);
            if(listLv!=null && listLv.size()>0){
                for(Level lv : listLv){
                    Test t = services.getTestDB(lv.getId());
                    if(t == null){
                        return ERROR;
                    }
                }
            }else{
                return SUCCESS;
            }

        }catch (Exception e){
            return ERROR;
        }
        return SUCCESS;
    }

}

