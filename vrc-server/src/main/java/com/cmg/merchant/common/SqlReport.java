package com.cmg.merchant.common;

/**
 * Created by lantb on 2016-04-05.
 */
public class SqlReport {


    private String SQL_LIST_CLASS_BY_TEACHER = "Select c.id, c.className from CLASSJDO as c " +
            "inner join CLASSMAPPINGTEACHER as m on c.id = m.idClass " +
            "where m.teacherName='paramTName' and c.isDeleted=false and m.isDeleted=false";

    private String SQL_LIST_STUDENT_IN_CLASS = "select id,STUDENTNAME from STUDENTMAPPINGCLASS " +
            "where IDCLASS='paramCid' and isDeleted=false order by STUDENTNAME ASC";


    private String SQL_LIST_COURSE_IN_CLASS = "select c.id, c.name from COURSE " +
            "as c inner join COURSEMAPPINGCLASS as cmc " +
            "on c.id = cmc.idCourse " +
            "where cmc.idClass='paramIdClass' and cmc.isDeleted=false and c.isDeleted=false";



    private String SQL_CHECK_USER_COMPLETED_LESSON="select q.id from QUESTION as q inner join LESSONMAPPINGQUESTION as lmq " +
            "on lmq.idQuestion = q.id " +
            "where q.id not in (select s.idQuestion from SESSIONSCORE as s inner join USERLESSONHISTORY as ulh " +
            "on s.IDUSERLESSONHISTORY = ulh.ID " +
            "inner join LESSONCOLLECTION as lmq " +
            "on s.IDLESSONCOLLECTION = lmq.id " +
            "where ulh.USERNAME='paramstudent' and lmq.id='paramIdLesson' and lmq.isDeleted=false) " +
            "and lmq.IDLESSON='paramIdLesson' and q.isDeleted=false and lmq.isDeleted=false;";

    private String SQL_CALCULATE_USER_SCORE_LESSON="select AVG(ulh.SCORE),ulh.SERVERTIME,s.sessionId from SESSIONSCORE as s inner join USERLESSONHISTORY as ulh " +
            "    on s.IDUSERLESSONHISTORY = ulh.ID " +
            "  inner join LESSONCOLLECTION as lmq " +
            "    on s.IDLESSONCOLLECTION = lmq.id " +
            "where ulh.USERNAME='paramstudent' and lmq.id='paramIdLesson' and lmq.isDeleted=false";


    private String SQL_LIST_WORD_IN_LESSON = "select w.word from LESSONMAPPINGQUESTION as lmq " +
            "  inner join QUESTION as q " +
            "  on q.id = lmq.IDQUESTION " +
            "inner join WORDOFQUESTION as woq " +
            "on lmq.IDQUESTION = woq.IDQUESTION " +
            "inner join WORDCOLLECTION as w on w.id = woq.IDWORDCOLLECTION " +
            "where lmq.IDLESSON='paramLessonId' " +
            "and lmq.ISDELETED=false and q.ISDELETED=false and woq.ISDELETED=false and w.ISDELETED=false";

    private String SQL_GET_SCORE_IN_WORD = "select s.SESSIONID,AVG(ulh.SCORE) from SESSIONSCORE as s " +
            "  inner join USERLESSONHISTORY as ulh " +
            "    on s.IDUSERLESSONHISTORY = ulh.ID " +
            "  inner join LESSONCOLLECTION as ls " +
            "    on ls.id = s.IDLESSONCOLLECTION " +
            "where ulh.USERNAME='paramStudent' " +
            "and ls.id='paramLessonId' and ulh.WORD='paramWord'" +
            "and ls.isDeleted=false";

    /**
     *
     * @param student
     * @param idLesson
     * @param word
     * @return
     */
    public String getSqlCalculateScoreWord(String student, String idLesson, String word){
        String sql = SQL_GET_SCORE_IN_WORD;
        sql = sql.replaceAll("paramStudent",student);
        sql = sql.replaceAll("paramLessonId",idLesson);
        sql = sql.replaceAll("paramWord",word);
        return sql;
    }

    /**
     *
     * @param student
     * @param idLesson
     * @return
     */
    public String getSqlListWordInLesson(String idLesson){
        String sql = SQL_LIST_WORD_IN_LESSON;
        sql = sql.replaceAll("paramLessonId",idLesson);
        return sql;
    }
    /**
     *
     * @param student
     * @param idLesson
     * @return
     */
    public String getSqlCalculateUserScoreLesson(String student, String idLesson){
        String sql = SQL_CALCULATE_USER_SCORE_LESSON;
        sql = sql.replaceAll("paramstudent",student);
        sql = sql.replaceAll("paramIdLesson",idLesson);
        return sql;
    }


    /**
     *
     * @param student
     * @param idLesson
     * @return
     */
    public String getSqlCheckUserCompletedLesson(String student, String idLesson){
        String sql = SQL_CHECK_USER_COMPLETED_LESSON;
        sql = sql.replaceAll("paramstudent",student);
        sql = sql.replaceAll("paramIdLesson",idLesson);
        return sql;
    }


    /**
     *
     * @param idClass
     * @return
     */
    public String getSqlListCourseInClass(String idClass){
        String sql = SQL_LIST_COURSE_IN_CLASS;
        sql = sql.replaceAll("paramIdClass",idClass);
        return sql;
    }
    /**
     *
     * @param tName
     * @return
     */
    public String getStudentInClass(String classId, String tName){
        String sql = SQL_LIST_STUDENT_IN_CLASS;
        sql = sql.replaceAll("paramCid",classId);
        return sql;
    }


    /**
     *
     * @param tName
     * @return
     */
    public String getSqlListClassByTeacher(String tName){
        String sql = SQL_LIST_CLASS_BY_TEACHER;
        sql = sql.replaceAll("paramTName",tName);
        return sql;
    }

}
