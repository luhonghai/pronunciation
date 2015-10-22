package com.cmg.lesson.dao.course;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.vrc.data.dao.DataAccess;

import java.util.List;

/**
 * Created by lantb on 2015-10-22.
 */
public class CourseMappingDetailDAO extends DataAccess<CourseMappingDetail> {
    public CourseMappingDetailDAO(){super(CourseMappingDetail.class);}

    /**
     *
     * @param idCourse
     * @param idLevel
     * @param isTest
     * @return
     * @throws Exception
     */
    public List<CourseMappingDetail> getBy(String idCourse, String idLevel, boolean isTest) throws Exception{
        List<CourseMappingDetail> list = list("WHERE idCourse == :1 && idLevel == :2 " +
                "&& isTest ==:3 && isDeleted==false", idCourse, idLevel, isTest);
        if(list!=null && list.size()>0){
            return list;
        }
        return null;
    }
}
