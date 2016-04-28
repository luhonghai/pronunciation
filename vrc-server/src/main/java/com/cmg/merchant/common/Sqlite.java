package com.cmg.merchant.common;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.vrc.util.PersistenceManagerHelper;

/**
 * Created by lantb on 2016-04-20.
 */
public class Sqlite {
    private String idCourse;

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public Sqlite(String idCourse){
        this.idCourse = idCourse;
    }

    private String deleteCourse = "Delete from COURSE where id !='paramIdCourse'";

    private String deleteCourseMapLevel = "Delete from COURSEMAPPINGLEVEL where idCourse!='paramIdCourse'";

    private String deleteLevel = "Delete from LEVEL where id not in (Select idLevel from COURSEMAPPINGLEVEL)";

    private String deleteCourseMapDetail = "Delete from COURSEMAPPINGDETAIL where idLevel not in (Select id from LEVEL)";

    private String deleteObj = "Delete from OBJECTIVE where id not in " +
            "(Select idChild from COURSEMAPPINGDETAIL where isTest='\\0' )";

    private String deleteTest = "Delete from TEST where id not in " +
            "(Select idChild from COURSEMAPPINGDETAIL where isTest!='\\0' )";

    private String deleteObjMapping = "Delete from OBJECTIVEMAPPING where idObjective not in (Select id from OBJECTIVE)";

    private String deleteTestMapping = "Delete from TESTMAPPING where idTest not in (Select id from TEST)";

    private String deleteLesson = "Delete from LESSONCOLLECTION where id not in (Select idLessonCollection " +
            "from TESTMAPPING UNION ALL Select idLessonCollection from OBJECTIVEMAPPING)";

    private String deleteLessonMap = "Delete from LESSONMAPPINGQUESTION where idLesson not in " +
            "(Select id from LESSONCOLLECTION)";

    private String deleteQuestion = "Delete from QUESTION where id not in (Select idQuestion from LESSONMAPPINGQUESTION)";

    private String deleteWordOfQuestion = "Delete from WORDOFQUESTION where idQuestion not in (Select id from QUESTION)";


    /**
     *
     *
     * @return sqlite query to delete all unnecessary course
     */
    public String deleteCourse(){
        String sqlite = deleteCourse;
        sqlite = sqlite.replaceAll("paramIdCourse",idCourse);
        return sqlite;
    }

    /**
     *
     *
     * @return sqlite query to delete all unnecessary course
     */
    public String deleteCourseMapLevel(){
        String sqlite = deleteCourseMapLevel;
        sqlite = sqlite.replaceAll("paramIdCourse",idCourse);
        return sqlite;
    }

    /**
     *
     *
     * @return sqlite query to delete all unnecessary level
     */
    public String deleteLevel(){
        String sqlite = deleteLevel;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteCourseMapDetail(){
        String sqlite = deleteCourseMapDetail;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteObj(){
        String sqlite = deleteObj;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteTest(){
        String sqlite = deleteTest;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteObjMapping(){
        String sqlite = deleteObjMapping;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteTestMapping(){
        String sqlite = deleteTestMapping;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteLesson(){
        String sqlite = deleteLesson;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteLessonMap(){
        String sqlite = deleteLessonMap;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteQuestion(){
        String sqlite = deleteQuestion;
        return sqlite;
    }

    /**
     *
     * @return
     */
    public String deleteWordOfQuestion(){
        String sqlite = deleteWordOfQuestion;
        return sqlite;
    }

    /**
     *
     * @param table
     * @return sqlite to delete all data unnecessary in table
     */
    public String getSqlite(String table){
        if(table.equalsIgnoreCase(getTableName(Course.class))){
            return deleteCourse();
        }else if(table.equalsIgnoreCase(getTableName(CourseMappingLevel.class))){
            return deleteCourseMapLevel();
        }else if(table.equalsIgnoreCase(getTableName(Level.class))){
            return deleteLevel();
        }else if(table.equalsIgnoreCase(getTableName(Objective.class))){

        }else if(table.equalsIgnoreCase(getTableName(Test.class))){

        }else if(table.equalsIgnoreCase(getTableName(LessonCollection.class))){

        }else if(table.equalsIgnoreCase(getTableName(Question.class))){

        }else if(table.equalsIgnoreCase(getTableName(WordOfQuestion.class))){

        }
        return null;
    }

    /**
     *
     * @param clazz
     * @return
     */
    private String getTableName(Class<?> clazz) {
        return PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(clazz.getCanonicalName()).getTable();
    }
}
