package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.ClassMappingTeacher;
import com.cmg.vrc.data.jdo.CourseMappingClass;
import com.cmg.vrc.data.jdo.StudentMappingClass;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 4/13/15.
 */
public class CourseMappingClassDAO extends DataAccess<CourseMappingClass> {

    public CourseMappingClassDAO() {
        super(CourseMappingClass.class);
    }



    public CourseMappingClass getByClassAndStudent(String idClass, String studentName) throws Exception{
        List<CourseMappingClass> userList = list("WHERE idClass == :1 && studentName == :2", idClass, studentName);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

}
