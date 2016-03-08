package com.cmg.merchant.services;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.mapping.CMTDAO;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-03-07.
 * this services use for get all course base on the mapping between course and teacher
 */
public class CMTSERVICES {
    /**
     *
     * @param cpId
     * @param tId
     * @param status
     * @param sr
     * @return
     */
    public ArrayList<Course> shareAllList(String cpId, String tId,
                                          String status, String sr){
        CMTDAO mDao = new CMTDAO();
        CDAO cDao = new CDAO();

        return null;
    }

    /**
     *
     * @param cpId
     * @param tId
     * @param status
     * @param sr
     * @return
     */
    public ArrayList<Course> shareInCompany(String cpId, String tId,
                                            String status, String sr){
        return null;
    }

    /**
     *
     * @param cpId
     * @param tId
     * @param status
     * @param sr
     * @return
     */
    public ArrayList<Course> createByTeacher(String cpId, String tId,
                                             String status, String sr){
        return null;
    }


    /**
     *
     * @param cpId
     * @param tId
     * @param status
     * @param sr
     * @return
     */
    public ArrayList<Course> getCoursesForMainPage(String cpId, String tId,
                                                   String status, String sr){
        return null;

    }
}
