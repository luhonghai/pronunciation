package com.cmg.merchant.services;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.common.Color;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.data.dto.CourseDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    public ArrayList<CourseDTO> shareAllList(String cpId, String tId,
                                          String status, String sr){
        CMTDAO dao = new CMTDAO();
        try {
            return dao.getCoursesShareAll(cpId, tId, status, sr);
        }catch (Exception e){
            e.printStackTrace();
        }
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
    public ArrayList<CourseDTO> shareInCompany(String cpId, String tId,
                                            String status, String sr){
        CMTDAO dao = new CMTDAO();
        try {
            return dao.getCoursesShareInCompany(cpId,tId,status,sr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param cpId
     * @param tId
     * @param status
     * @return
     */
    public ArrayList<CourseDTO> createByTeacher(String cpId, String tId,
                                             String status){
        CMTDAO dao = new CMTDAO();
        try {
            return dao.getCoursesCreateByTeacher(cpId, tId, status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param cpId
     * @param tId
     * @return
     */
    public ArrayList<CourseDTO> getCoursesForMainPage(final String cpId, String tId){
        ArrayList<CourseDTO> list = new ArrayList<>();
        list.addAll(shareAllList(cpId, tId, Constant.STATUS_NOT_PUBLISH, Constant.SHARE_ALL));
        list.addAll(shareInCompany(cpId, tId, Constant.STATUS_NOT_PUBLISH, Constant.SHARE_IN_COMPANY));
        list.addAll(createByTeacher(cpId, tId, Constant.STATUS_NOT_PUBLISH));
        if(list.size() > 0 ){
            Collections.sort(list,
                    new Comparator<CourseDTO>() {
                        @Override
                        public int compare(final CourseDTO a, final CourseDTO d) {
                            return (a.getNameCourse().compareTo(d.getNameCourse()));
                        }
                    });
            for(CourseDTO dto : list){
                if(dto.getIdCompany().equalsIgnoreCase(cpId)){
                    dto.setBackgroundColor(Color.COURSE_CREATE_BY_COMPANY);
                }else{
                    dto.setBackgroundColor(Color.COURSE_CREATE_BY_OTHER_COMPANY);
                }
                dto.setTextColor(Color.TEXT_COLOR);
            }
        }
        return list;
    }
}
