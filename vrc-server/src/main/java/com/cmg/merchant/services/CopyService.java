package com.cmg.merchant.services;

import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by lantb on 2016-03-15.
 */
public class CopyService {
    CourseServices courseServices = new CourseServices();
    LevelServices lvServices = new LevelServices();
    OServices objServices = new OServices();
    TestServices testServices = new TestServices();
    public String ERROR = "error";
    public String SUCCESS = "success";
    SessionUtil util = new SessionUtil();

    /**
     *
     * @param idCourse
     * @return
     */
    public String copyAllDataCourse(String idCourse, HttpServletRequest request){
        String newIdCourse = courseServices.copyCourse(idCourse, request);
        if(newIdCourse.contains(ERROR)){
            return ERROR;
        }
        ArrayList<Level> listLevel = lvServices.getAllByCourseId(idCourse);
        if(listLevel!= null &&listLevel.size() > 0){
            for(Level lv : listLevel){
                String newIdLevel = lvServices.copyLevel(newIdCourse, lv.getId());
                if(newIdLevel.contains(ERROR)){
                    continue;
                }
                copyAllObjToLevel(newIdLevel,lv.getId());
                copyTestToLevel(newIdLevel, lv.getId());
            }
        }
        return SUCCESS;
    }

    /**
     *
     * @param levelIdNew
     * @param levelIdOld
     */
    public void copyAllObjToLevel(String levelIdNew, String levelIdOld){
        ArrayList<Objective> listObj = objServices.getAllByLevelId(levelIdOld);
        if(listObj!=null && listObj.size() > 0)
        for(Objective obj : listObj){
            String newIdObj = objServices.copyObj(levelIdNew,obj.getId());
        }
    }

    /**
     *
     * @param levelIdNew
     * @param levelIdOld
     */
    public void copyTestToLevel(String levelIdNew, String levelIdOld){
        Test t = testServices.getTestByLevelId(levelIdOld);
        String newIdTest = testServices.copyTest(levelIdNew, t.getId());
    }


}
