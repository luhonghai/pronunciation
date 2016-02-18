package com.cmg.merchant.services;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
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

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        CDAO dao = new CDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }


    /**
     *
     * @param course
     * @return
     */
    public ArrayList<String> suggestionCourse(String course){
        CDAO dao = new CDAO();
        ArrayList<String> listSuggestion = new ArrayList<>();
        try {
            List<Course> courses = dao.suggestionCourse(0,3,course);
            if(courses!=null && courses.size() > 0){
                for(Course c : courses){
                    listSuggestion.add(c.getName());
                }
            }
        }catch (Exception e){
            logger.error("can not retrieve data for suggestion course : " + e);
        }
        return listSuggestion;
    }

    /**
     *
     * @param name
     * @param description
     * @param share
     * @return
     */
    public String addCourse(String name, String description, String share, HttpServletRequest request){
        String result = "success";
        CDAO cDao = new CDAO();
        CMTDAO cmtDao = new CMTDAO();
        SessionUtil util = new SessionUtil();
        try {
            String cId = UUIDGenerator.generateUUID().toString();
            Course c = new Course(cId,name,description,false,
                    getMaxVersion(),new Date(System.currentTimeMillis()));
            boolean check = cDao.create(c);
            if(check){
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
                if(!check){
                    result = "error:An error has been occurred in server";
                }
            }else{
                result = "error:An error has been occurred in server";
            }
        }catch (Exception e){
            logger.error("can not add course : " + e);
            result = "error:An error has been occurred in server";
        }
        return result;
    }

}

