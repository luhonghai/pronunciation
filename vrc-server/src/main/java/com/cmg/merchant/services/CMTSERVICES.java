package com.cmg.merchant.services;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.common.Color;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.data.dto.CourseDTO;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
import com.cmg.vrc.data.dao.impl.ClientCodeDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by lantb on 2016-03-07.
 * this services use for get all course base on the mapping between course and teacher
 */
public class CMTSERVICES {

    public String ERROR = "error";
    public String SUCCESS = "success";

    /**
     *
     * @param idCourse
     * @return
     */
    public String publishCourse(String idCourse){
        CMTDAO dao = new CMTDAO();
        try {
            CourseMappingTeacher cmt = dao.getByIdCourse(idCourse);
            dao.updateStatus(cmt.getId(), Constant.STATUS_PUBLISH);
            return SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ERROR;
    }


    /**
     *
     * @param idCourse
     * @return
     */
    public String publishCourseCopy(String idCourse, String state){
        CMTDAO dao = new CMTDAO();
        try {
            CourseMappingTeacher cmt = dao.getByIdCourse(idCourse);
            dao.updateStatusAndState(cmt.getId(), Constant.STATUS_PUBLISH, state);
            return SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ERROR;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public String UpdateStateCourseCopy(String idCourse, String state){
        CMTDAO dao = new CMTDAO();
        try {
            CourseMappingTeacher cmt = dao.getByIdCourse(idCourse);
            dao.updateState(cmt.getId(), state);
            return SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return ERROR;
    }

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
            return dao.getCoursesShareInCompany(cpId, tId, status, sr);
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
        list.addAll(shareAllList(cpId, tId, Constant.STATUS_PUBLISH, Constant.SHARE_ALL));
        list.addAll(shareInCompany(cpId, tId, Constant.STATUS_PUBLISH, Constant.SHARE_IN_COMPANY));
        list.addAll(createByTeacher(cpId, tId, Constant.STATUS_PUBLISH));
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
                dto.setPageLink("/review-course.jsp?idCourse=" +dto.getIdCourse());
            }
        }
        return list;
    }

    /**
     *
     * @param cpId
     * @param tId
     * @return
     */
    public ArrayList<CourseDTO> getCoursesForMyCourses(String cpId, String tId, String teacherName){
        ArrayList<CourseDTO> list = new ArrayList<>();
        try {
            CMTDAO dao = new CMTDAO();
            ClientCodeDAO cpDao = new ClientCodeDAO();
            list = dao.getCoursesInMyCourses(cpId, tId);
            if(list.size() > 0 ){
                for(CourseDTO dto : list){
                    if(dto.getState().equalsIgnoreCase(Constant.STATE_EDITED) ||
                            dto.getState().equalsIgnoreCase(Constant.STATE_DUPLICATED)){
                        String cpCloneName = cpDao.getById(dto.getCpCloneId()).getCompanyName();
                        dto.setCompanyName(cpCloneName);
                        dto.setBackgroundColor(Color.MY_COURSE_DUPLICATE_COLOR);
                        dto.setTextColor(Color.TEXT_COLOR);
                        if(dto.getState().equalsIgnoreCase(Constant.STATE_DUPLICATED)){
                            dto.setPageLink("/edit-copy-course.jsp?idCourse=" + dto.getIdCourse());
                        }else{
                            dto.setPageLink("/course-details.jsp?idCourse=" + dto.getIdCourse());
                        }
                    }else{
                        dto.setBackgroundColor(Color.MY_COURSE_CREATED_BY_TEACHER);
                        dto.setTextColor(Color.TEXT_COLOR);
                        dto.setPageLink("/course-details.jsp?idCourse=" + dto.getIdCourse());
                    }
                    if(isAssignToClass(dto.getIdCourse(),teacherName)){
                        dto.setPageLink("/review-course.jsp?idCourse=" + dto.getIdCourse());
                    }
                }
            }
        }catch (Exception e){

        }
        return list;
    }

    /**
     *
     * @param cId
     * @param tName
     * @return
     */
    public boolean isAssignToClass(String cId, String tName){
        CDAO dao = new CDAO();
        try {
            return dao.isAssignToClass(cId,tName);
        }catch (Exception e){
        }
        return false;
    }

    /**
     *
     * @param cpId
     * @param tId
     * @return
     */
    public ArrayList<CourseDTO> searchHeaderMyCourse(String cpId, String tId, String cName){
        ArrayList<CourseDTO> list = new ArrayList<>();
        try {
            CMTDAO dao = new CMTDAO();
            ClientCodeDAO cpDao = new ClientCodeDAO();
            list = dao.searchHeaderMyCourse(cpId, tId, cName);
            if(list.size() > 0 ) {
                for (CourseDTO dto : list) {
                    if (dto.getState().equalsIgnoreCase(Constant.STATE_EDITED) ||
                            dto.getState().equalsIgnoreCase(Constant.STATE_DUPLICATED)) {
                        String cpCloneName = cpDao.getById(dto.getCpCloneId()).getCompanyName();
                        dto.setCompanyName(cpCloneName);
                        dto.setBackgroundColor(Color.MY_COURSE_DUPLICATE_COLOR);
                        dto.setTextColor(Color.TEXT_COLOR);
                        dto.setPageLink("/course-details.jsp?idCourse=" + dto.getIdCourse());
                    } else {
                        dto.setBackgroundColor(Color.MY_COURSE_CREATED_BY_TEACHER);
                        dto.setTextColor(Color.TEXT_COLOR);
                        dto.setPageLink("/course-details.jsp?idCourse=" + dto.getIdCourse());
                    }
                }
            }
        }catch (Exception e){

        }
        return list;
    }

    /**
     *
     * @param cpName
     * @param cName
     * @param dateFrom
     * @param dateTo
     * @param cpId
     * @param tId
     * @return
     */
    public ArrayList<CourseDTO> searchCourseDetailMyCourse(String cpName,String cName,String dateFrom, String dateTo ,String cpId, String tId){
        CMTDAO dao = new CMTDAO();
        ClientCodeDAO cpDao = new ClientCodeDAO();
        ArrayList<CourseDTO> list = new ArrayList<>();
        if(dateFrom == ""){
            dateFrom = "1999-01-01";
        }
        if(dateTo == ""){
            dateTo = "2100-01-01";
        }
        try {
            list = dao.searchDetailMyCourse(cpId, tId, cName, cpName, dateFrom, dateTo);
            if(list!=null && list.size()>0){
                for (CourseDTO dto : list) {
                    if (dto.getState().equalsIgnoreCase(Constant.STATE_EDITED) ||
                            dto.getState().equalsIgnoreCase(Constant.STATE_DUPLICATED)) {
                        String cpCloneName = cpDao.getById(dto.getCpCloneId()).getCompanyName();
                        dto.setCompanyName(cpCloneName);
                        dto.setBackgroundColor(Color.MY_COURSE_DUPLICATE_COLOR);
                        dto.setTextColor(Color.TEXT_COLOR);
                        dto.setPageLink("/edit-copy-course.jsp?idCourse=" + dto.getIdCourse());
                    } else {
                        dto.setBackgroundColor(Color.MY_COURSE_CREATED_BY_TEACHER);
                        dto.setTextColor(Color.TEXT_COLOR);
                        dto.setPageLink("/course-details.jsp?idCourse=" + dto.getIdCourse());
                    }
                }
            }
        }catch (Exception e){
        }

        return list;
    }


    /**
     *
     * @param name
     * @param cpId
     * @param tId
     * @return
     */
    public ArrayList<CourseDTO> searchCourseHeader(String name, String cpId, String tId){
        CMTDAO dao = new CMTDAO();
        ArrayList<CourseDTO> list = new ArrayList<CourseDTO>();
        ArrayList<CourseDTO> dbList = dao.searchCourseHeader(Constant.STATUS_PUBLISH, name);
        if(dbList.size() > 0){
            for(CourseDTO dto : dbList){
                if(dto.getSr().equalsIgnoreCase(Constant.SHARE_ALL)){
                    list.add(dto);
                    continue;
                }
                if(dto.getIdCompany().equalsIgnoreCase(cpId) && dto.getSr().equalsIgnoreCase(Constant.SHARE_IN_COMPANY)){
                    list.add(dto);
                    continue;
                }
                if(dto.getIdCompany().equalsIgnoreCase(cpId) && dto.getIdTeacher().equalsIgnoreCase(tId)){
                    list.add(dto);
                    continue;
                }
            }
        }
        if(list.size() > 0) {
            for (CourseDTO dto : list) {
                if (dto.getIdCompany().equalsIgnoreCase(cpId)) {
                    dto.setBackgroundColor(Color.COURSE_CREATE_BY_COMPANY);
                } else {
                    dto.setBackgroundColor(Color.COURSE_CREATE_BY_OTHER_COMPANY);
                }
                dto.setTextColor(Color.TEXT_COLOR);
            }
        }
        return list;
    }



    /**
     *
     * @param cpName
     * @param cName
     * @param dateFrom
     * @param dateTo
     * @param cpId
     * @param tId
     * @return
     */
    public ArrayList<CourseDTO> searchCourseDetail(String cpName,String cName,String dateFrom, String dateTo ,String cpId, String tId){
        CMTDAO dao = new CMTDAO();
        ArrayList<CourseDTO> list = new ArrayList<CourseDTO>();
        if(dateFrom == ""){
            dateFrom = "1999-01-01";
        }
        if(dateTo == ""){
            dateTo = "2100-01-01";
        }
        ArrayList<CourseDTO> dbList = dao.searchCourseDetail(Constant.STATUS_PUBLISH, cpName,cName,dateFrom,dateTo);
        if(dbList.size() > 0){
            for(CourseDTO dto : dbList){
                if(dto.getSr().equalsIgnoreCase(Constant.SHARE_ALL)){
                    list.add(dto);
                    continue;
                }
                if(dto.getIdCompany().equalsIgnoreCase(cpId) && dto.getSr().equalsIgnoreCase(Constant.SHARE_IN_COMPANY)){
                    list.add(dto);
                    continue;
                }
                if(dto.getIdCompany().equalsIgnoreCase(cpId) && dto.getIdTeacher().equalsIgnoreCase(tId)){
                    list.add(dto);
                    continue;
                }
            }
        }
        if(list.size() > 0) {
            for (CourseDTO dto : list) {
                if (dto.getIdCompany().equalsIgnoreCase(cpId)) {
                    dto.setBackgroundColor(Color.COURSE_CREATE_BY_COMPANY);
                } else {
                    dto.setBackgroundColor(Color.COURSE_CREATE_BY_OTHER_COMPANY);
                }
                dto.setTextColor(Color.TEXT_COLOR);
            }
        }
        return list;
    }



}
