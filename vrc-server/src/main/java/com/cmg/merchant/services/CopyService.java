package com.cmg.merchant.services;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.lesson.data.jdo.word.WordCollection;
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
    QuestionServices questionServices = new QuestionServices();
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
        copyAllLevelToCourse(newIdCourse,idCourse);
        return SUCCESS;
    }


    /**
     *
     * @param idCourseMapping
     * @param idCourseGetData
     */
    public void copyAllLevelToCourse(String idCourseMapping, String idCourseGetData){
        ArrayList<Level> listLevel = lvServices.getAllByCourseId(idCourseGetData);
        if(listLevel!= null &&listLevel.size() > 0){
            for(Level lv : listLevel){
                String newIdLevel = lvServices.copyLevel(idCourseMapping, lv.getId());
                if(newIdLevel.contains(ERROR)){
                    continue;
                }
                copyAllObjToLevel(newIdLevel,lv.getId());
                copyTestToLevel(newIdLevel, lv.getId());
            }
        }
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
        copyLessonsToTest(newIdTest, t.getId());
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


    public void copyLessonsToTest(String testIdMapping, String testIdGetData){
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
         ArrayList<Question> list = questionServices.getQuestionByIdLesson(lessonIdGetData);
         if(list!=null && list.size()>0){
             for(Question q : list){
                 String newIdQuestion = questionServices.copyQuestion(lessonIdMapping,q.getId());
                 copyAllWordsToQuestion(newIdQuestion,q.getId());
             }
         }
    }

    /**
     *
     * @param questionIdMapping
     * @param questionIdGetData
     */
    public void copyAllWordsToQuestion(String questionIdMapping, String questionIdGetData){
        ArrayList<WordCollection> list = questionServices.getWordsByIdQuestion(questionIdGetData);
        if(list!=null && list.size() > 0){
            for(WordCollection word : list){
                questionServices.copyWords(questionIdMapping,word.getId());
                questionServices.copyWeight(questionIdMapping,word.getId(),questionIdGetData);
            }
        }
    }
}
