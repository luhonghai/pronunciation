package com.cmg.merchant.services;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
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
    LessonServices lessonServices = new LessonServices();
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
     * @param levelIdMapping
     * @param levelIdGetData
     */
    public void copyAllObjToLevel(String levelIdMapping, String levelIdGetData){
        ArrayList<Objective> listObj = objServices.getAllByLevelId(levelIdGetData);
        if(listObj!=null && listObj.size() > 0)
        for(Objective obj : listObj){
            String newIdObj = objServices.copyObj(levelIdMapping,obj.getId());
            copyAllLessonsToObj(newIdObj,obj.getId());
        }
    }

    /**
     *
     * @param levelIdMapping
     * @param levelIdGetData
     */
    public void copyTestToLevel(String levelIdMapping, String levelIdGetData){
        Test t = testServices.getTestByLevelId(levelIdGetData);
        String newIdTest = testServices.copyTest(levelIdMapping, t.getId());
        copyAllLessonsToTest(newIdTest,t.getId());
    }


    /**
     *
     * @param objIdMapping
     * @param objIdGetData
     */
    public void copyAllLessonsToObj(String objIdMapping, String objIdGetData){
        ArrayList<LessonCollection> listLessons = lessonServices.getAllByObjId(objIdGetData);
        if(listLessons!=null && listLessons.size() > 0){
            for(LessonCollection lesson : listLessons){
                String newLessonId = lessonServices.copyLessonInObj(objIdMapping,lesson.getId());
                copyAllQuestionToLessons(newLessonId,lesson.getId());
            }
        }
    }


    public void copyAllLessonsToTest(String testIdMapping, String testIdGetData){
        LessonCollection lesson = lessonServices.getByTestId(testIdGetData);
        if(lesson!=null){
            String newLessonId = lessonServices.copyLessonInTest(testIdMapping,lesson.getId());
            copyAllQuestionToLessons(newLessonId,lesson.getId());
        }
    }

    /**
     *
     * @param lessonIdMapping
     * @param lessonIdGetData
     */
    public void copyAllQuestionToLessons(String lessonIdMapping, String lessonIdGetData){

    }
}
