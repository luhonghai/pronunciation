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
        return SUCCESS + ":" + newIdCourse;
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
                String newIdLevel = lvServices.copyLevel(idCourseMapping, lv.getId(),false);
                if(newIdLevel.contains(ERROR)){
                    continue;
                }
                copyAllObjToLevel(newIdLevel,lv.getId());
                copyAllTestToLevel(newIdLevel, lv.getId());
            }
        }
    }

    /**
     *
     * @param idCourse
     * @param idLevel
     */
    public String copyLevelToCourse(String idCourse, String idLevel){
        String newIdLevel = lvServices.copyLevel(idCourse, idLevel, true);
        if(newIdLevel.contains(ERROR)){
            return ERROR;
        }
        copyAllObjToLevel(newIdLevel, idLevel);
        copyAllTestToLevel(newIdLevel, idLevel);
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
            String newIdObj = objServices.copyObj(levelIdMapping,obj.getId(),false);
            copyAllLessonsToObj(newIdObj,obj.getId());
        }
    }

    /**
     *
     * @param idLevel
     * @param idObj
     * @return
     */
    public String copyObjToLevel(String idLevel, String idObj){
        String newIdObj = objServices.copyObj(idLevel, idObj, true);
        copyAllLessonsToObj(newIdObj, idObj);
        return SUCCESS;
    }

    /**
     *
     * @param levelIdMapping
     * @param levelIdGetData
     */
    public void copyAllTestToLevel(String levelIdMapping, String levelIdGetData){
        Test t = testServices.getTestByLevelId(levelIdGetData);
        if(t!=null){
            String newIdTest = testServices.copyTest(levelIdMapping, t.getId());
            copyLessonsToTest(newIdTest, t.getId());
        }

    }

    /**
     *
     * @param idLevel
     * @param idTest
     * @return
     */
    public String copyTestToLevel(String idLevel, String idTest){
        String newIdTest = testServices.copyTest(idLevel, idTest);
        copyLessonsToTest(newIdTest, idTest);
        return SUCCESS;
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
                String newLessonId = lessonServices.copyLessonInObj(objIdMapping,lesson.getId(),false);
                copyAllQuestionToLessons(newLessonId,lesson.getId());
            }
        }
    }

    /**
     *
     * @param idObj
     * @param idLesson
     * @return
     */
    public String CopyLessonToObj(String idObj, String idLesson){
        String newLessonId = lessonServices.copyLessonInObj(idObj, idLesson, true);
        copyAllQuestionToLessons(newLessonId,idLesson);
        return SUCCESS;
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
     * @param idLesson
     * @param idQuestion
     * @return
     */
    public String copyQuestionToLessons(String idLesson, String idQuestion){
        String newIdQuestion = questionServices.copyQuestion(idLesson,idQuestion);
        copyAllWordsToQuestion(newIdQuestion,idQuestion);
        return SUCCESS;
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
