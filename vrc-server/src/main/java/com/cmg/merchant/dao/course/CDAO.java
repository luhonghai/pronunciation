package com.cmg.merchant.dao.course;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.vrc.data.dao.DataAccess;

import java.util.List;

/**
 * Created by lantb on 2016-01-26.
 */
public class CDAO extends DataAccess<Course> {
    public CDAO(){super(Course.class);}

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Course getById(String id) throws Exception{
        boolean isExist = false;
        List<Course> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
