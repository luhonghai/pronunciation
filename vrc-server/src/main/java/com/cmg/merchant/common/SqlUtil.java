package com.cmg.merchant.common;

/**
 * Created by lantb on 2016-03-15.
 */
public class SqlUtil {

    /**
     *
     */
    private String SQL_CHECK_COURSE_CREATED_BY_TEACHER = "Select * from COURSEMAPPINGTEACHER where cid='pCId' " +
            "and cpID ='pcpID' and tID='ptID' and cpIdClone='pcpIdClone' and cIdClone='pcIdClone'";


    private String SQL_CHECK_COURSE_ALREADY_COPIED_BY_TEACHER = "Select * from COURSEMAPPINGTEACHER where  " +
            "and cpID ='pcpID' and tID='ptID' and cpIdClone !='pcpIdClone' and cIdClone ='cId'";


    private String SQL_CHECK_QUESTION_TEST_IN_LEVEL = "SELECT q.id, q.name " +
            "FROM TEST AS t INNER JOIN COURSEMAPPINGDETAIL AS cmd ON t.id = cmd.idChild " +
            "  INNER JOIN TESTMAPPING AS tm ON t.id = tm.idTest " +
            "  INNER JOIN LESSONCOLLECTION AS les ON les.id = tm.idLessonCollection " +
            "  INNER JOIN LESSONMAPPINGQUESTION AS lmq ON les.id = lmq.idLesson " +
            "  INNER JOIN QUESTION AS q ON lmq.idQuestion = q.id " +
            "WHERE cmd.idLevel = 'paramLvId' AND t.isDeleted = FALSE AND cmd.isDeleted = FALSE AND tm.isDeleted = FALSE AND " +
            "      les.isDeleted = FALSE AND lmq.isDeleted = FALSE AND q.isDeleted = FALSE";


    private String SQL_CHECK_QUESTION_OBJ_IN_LEVEL = "SELECT q.id, q.name " +
            "FROM OBJECTIVE AS obj INNER JOIN COURSEMAPPINGDETAIL AS cmd ON obj.id = cmd.idChild " +
            "  INNER JOIN OBJECTIVEMAPPING AS om ON obj.id = om.idObjective " +
            "  INNER JOIN LESSONCOLLECTION AS les ON les.id = om.idLessonCollection " +
            "  INNER JOIN LESSONMAPPINGQUESTION AS lmq ON les.id = lmq.idLesson " +
            "  INNER JOIN QUESTION AS q ON lmq.idQuestion = q.id " +
            "WHERE cmd.idLevel = 'paramLvId' AND obj.isDeleted = FALSE AND cmd.isDeleted = FALSE AND om.isDeleted = FALSE AND " +
            "      les.isDeleted = FALSE AND lmq.isDeleted = FALSE AND q.isDeleted = FALSE";

    /**
     *
     * @param idCourse
     * @param tId
     * @param cpId
     * @return
     */
    public String getSqlCheckCourseCreatedByTeacher(String idCourse, String tId, String cpId){
        String sql = SQL_CHECK_COURSE_CREATED_BY_TEACHER;
        sql = sql.replaceAll("pCId",idCourse);
        sql = sql.replaceAll("pcpID",cpId);
        sql = sql.replaceAll("ptID",tId);
        sql = sql.replaceAll("pcpIdClone",Constant.DEFAULT_VALUE_CLONE);
        sql = sql.replaceAll("pcIdClone",Constant.DEFAULT_VALUE_CLONE);
        return sql;
    }

    /**
     *
     * @param idCourse
     * @param tId
     * @param cpID
     * @return
     */
    public String getSQLCheckCourseAlreadyCopied(String idCourse, String tId, String cpId){
        String sql = SQL_CHECK_COURSE_CREATED_BY_TEACHER;
        sql = sql.replaceAll("pcpID",cpId);
        sql = sql.replaceAll("ptID",tId);
        sql = sql.replaceAll("pcpIdClone",Constant.DEFAULT_VALUE_CLONE);
        sql = sql.replaceAll("pcIdClone",idCourse);
        return sql;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public String getSqlCheckQuestionTestInLevel(String idLevel){
        String sql = SQL_CHECK_QUESTION_TEST_IN_LEVEL;
        sql = sql.replaceAll("paramLvId",idLevel);
        return sql;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public String getSqlCheckQuestionObjInLevel(String idLevel){
        String sql = SQL_CHECK_QUESTION_OBJ_IN_LEVEL;
        sql = sql.replaceAll("paramLvId",idLevel);
        return sql;
    }
}
