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


    private String SQL_DELETE_COURSE_STEP_1 = "update COURSE as c inner join COURSEMAPPINGLEVEL as cml on c.id = cml.idCourse\n" +
            "  LEFT JOIN LEVEL as lv on cml.idLevel = lv.id\n" +
            "  LEFT JOIN COURSEMAPPINGDETAIL as cmd on lv.id = cmd.idLevel\n" +
            "  LEFT JOIN OBJECTIVE as obj on cmd.idChild = obj.id\n" +
            "  LEFT JOIN OBJECTIVEMAPPING as om on om.idObjective = obj.id\n" +
            "  LEFT JOIN LESSONCOLLECTION as ls on ls.id = om.idLessonCollection\n" +
            "  LEFT JOIN LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id\n" +
            "  LEFT JOIN QUESTION as q on q.id = lmq.idQuestion\n" +
            "set c.isDeleted=true, cml.isDeleted=true, lv.isDeleted=true,cmd.isDeleted=true,\n" +
            "  obj.isDeleted=true,om.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true where c.id = 'paramCourseId'";

    private String SQL_DELETE_COURSE_STEP_2 = "update COURSE as c inner join COURSEMAPPINGLEVEL as cml on c.id = cml.idCourse\n" +
            "  LEFT JOIN LEVEL as lv on cml.idLevel = lv.id\n" +
            "  LEFT JOIN COURSEMAPPINGDETAIL as cmd on lv.id = cmd.idLevel\n" +
            "  LEFT JOIN TEST as t on cmd.idChild = t.id\n" +
            "  LEFT JOIN TESTMAPPING as tm on tm.idTest = t.id\n" +
            "  LEFT JOIN LESSONCOLLECTION as ls on ls.id = tm.idLessonCollection\n" +
            "  LEFT JOIN LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id\n" +
            "  LEFT JOIN QUESTION as q on q.id = lmq.idQuestion\n" +
            "set c.isDeleted=true, cml.isDeleted=true, lv.isDeleted=true,cmd.isDeleted=true,\n" +
            "  t.isDeleted=true,tm.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true where c.id = 'paramCourseId'";


    private String SQL_DELETE_LEVEL_STEP_1 = "update LEVEL as lv " +
            "left join COURSEMAPPINGDETAIL as cmd on lv.id = cmd.idLevel\n" +
            "left join OBJECTIVE as obj on cmd.idChild = obj.id\n" +
            "left join OBJECTIVEMAPPING as om on om.idObjective = obj.id\n" +
            "left join LESSONCOLLECTION as ls on ls.id = om.idLessonCollection\n" +
            "left join LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id\n" +
            "left join QUESTION as q on q.id = lmq.idQuestion \n" +
            "set lv.isDeleted=true,cmd.isDeleted=true,\n" +
            "obj.isDeleted=true,om.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true where lv.id = 'paramLevelId'";


    private String SQL_DELETE_LEVEL_STEP_2 = "update LEVEL as lv " +
            "left join COURSEMAPPINGDETAIL as cmd on lv.id = cmd.idLevel\n" +
            "left join TEST as t on cmd.idChild = t.id\n" +
            "left join TESTMAPPING as tm on tm.idObjective = t.id\n" +
            "left join LESSONCOLLECTION as ls on ls.id = tm.idLessonCollection\n" +
            "left join LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id\n" +
            "left join QUESTION as q on q.id = lmq.idQuestion \n" +
            "set lv.isDeleted=true,cmd.isDeleted=true,\n" +
            "t.isDeleted=true,tm.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true where lv.id = 'paramLevelId'";



    /**
     *
     * @param idLevel
     * @return
     */
    public String getSqlDeleteLevel1(String idLevel){
        String sql = SQL_DELETE_LEVEL_STEP_1;
        sql = sql.replaceAll("paramLevelId",idLevel);
        return sql;
    }
    /**
     *
     * @param idLevel
     * @return
     */
    public String getSqlDeleteLevel2(String idLevel){
        String sql = SQL_DELETE_LEVEL_STEP_2;
        sql = sql.replaceAll("paramLevelId",idLevel);
        return sql;
    }




    /**
     *
     * @param idCourse
     * @return
     */
    public String getSqlDeleteCourse1(String idCourse){
        String sql = SQL_DELETE_COURSE_STEP_1;
        sql = sql.replaceAll("paramCourseId",idCourse);
        return sql;
    }
    /**
     *
     * @param idCourse
     * @return
     */
    public String getSqlDeleteCourse2(String idCourse){
        String sql = SQL_DELETE_COURSE_STEP_2;
        sql = sql.replaceAll("paramCourseId",idCourse);
        return sql;
    }
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
