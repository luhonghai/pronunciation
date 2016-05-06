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


    private String SQL_DELETE_COURSE_STEP_1 = "update COURSE as c left join COURSEMAPPINGLEVEL as cml on c.id = cml.idCourse\n" +
            "  LEFT JOIN LEVEL as lv on cml.idLevel = lv.id " +
            "  LEFT JOIN COURSEMAPPINGDETAIL as cmd on lv.id = cmd.idLevel " +
            "  LEFT JOIN OBJECTIVE as obj on cmd.idChild = obj.id " +
            "  LEFT JOIN OBJECTIVEMAPPING as om on om.idObjective = obj.id " +
            "  LEFT JOIN LESSONCOLLECTION as ls on ls.id = om.idLessonCollection " +
            "  LEFT JOIN LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id " +
            "  LEFT JOIN QUESTION as q on q.id = lmq.idQuestion " +
            "  LEFT JOIN WORDOFQUESTION as woq on woq.idQuestion = q.id " +
            "  LEFT JOIN WEIGHTFORPHONEME as wfp on wfp.idQuestion = q.id " +
            "set c.isDeleted=true, cml.isDeleted=true, lv.isDeleted=true,cmd.isDeleted=true, " +
            "  obj.isDeleted=true,om.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true,woq.isDeleted=true,wfp.isDeleted=true where c.id = 'paramCourseId'";

    private String SQL_DELETE_COURSE_STEP_2 = "update COURSE as c left join COURSEMAPPINGLEVEL as cml on c.id = cml.idCourse\n" +
            "  LEFT JOIN LEVEL as lv on cml.idLevel = lv.id " +
            "  LEFT JOIN COURSEMAPPINGDETAIL as cmd on lv.id = cmd.idLevel " +
            "  LEFT JOIN TEST as t on cmd.idChild = t.id " +
            "  LEFT JOIN TESTMAPPING as tm on tm.idTest = t.id " +
            "  LEFT JOIN LESSONCOLLECTION as ls on ls.id = tm.idLessonCollection " +
            "  LEFT JOIN LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id " +
            "  LEFT JOIN QUESTION as q on q.id = lmq.idQuestion " +
            "  LEFT JOIN WORDOFQUESTION as woq on woq.idQuestion = q.id " +
            "  LEFT JOIN WEIGHTFORPHONEME as wfp on wfp.idQuestion = q.id " +
            "set c.isDeleted=true, cml.isDeleted=true, lv.isDeleted=true,cmd.isDeleted=true, " +
            "  t.isDeleted=true,tm.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true,woq.isDeleted=true,wfp.isDeleted=true where c.id = 'paramCourseId'";


    private String SQL_DELETE_LEVEL_STEP_1 = "update LEVEL as lv " +
            "left join COURSEMAPPINGDETAIL as cmd on lv.id = cmd.idLevel " +
            "left join OBJECTIVE as obj on cmd.idChild = obj.id " +
            "left join OBJECTIVEMAPPING as om on om.idObjective = obj.id " +
            "left join LESSONCOLLECTION as ls on ls.id = om.idLessonCollection " +
            "left join LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id " +
            "left join QUESTION as q on q.id = lmq.idQuestion " +
            "LEFT JOIN WORDOFQUESTION as woq on woq.idQuestion = q.id " +
            "LEFT JOIN WEIGHTFORPHONEME as wfp on wfp.idQuestion = q.id " +
            "set lv.isDeleted=true,cmd.isDeleted=true," +
            "obj.isDeleted=true,om.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true,woq.isDeleted=true,wfp.isDeleted=true where lv.id = 'paramLevelId'";


    private String SQL_DELETE_LEVEL_STEP_2 = "update LEVEL as lv " +
            "left join COURSEMAPPINGDETAIL as cmd on lv.id = cmd.idLevel " +
            "left join TEST as t on cmd.idChild = t.id " +
            "left join TESTMAPPING as tm on tm.idTest = t.id " +
            "left join LESSONCOLLECTION as ls on ls.id = tm.idLessonCollection " +
            "left join LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id " +
            "left join QUESTION as q on q.id = lmq.idQuestion " +
            "LEFT JOIN WORDOFQUESTION as woq on woq.idQuestion = q.id " +
            "LEFT JOIN WEIGHTFORPHONEME as wfp on wfp.idQuestion = q.id " +
            "set lv.isDeleted=true,cmd.isDeleted=true, " +
            "t.isDeleted=true,tm.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true,woq.isDeleted=true,wfp.isDeleted=true where lv.id = 'paramLevelId'";

    private String SQL_DELETE_OBJ = "UPDATE OBJECTIVE as obj " +
            "left join OBJECTIVEMAPPING as om on om.idObjective = obj.id " +
            "left join LESSONCOLLECTION as ls on ls.id = om.idLessonCollection " +
            "left join LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id " +
            "left join QUESTION as q on q.id = lmq.idQuestion " +
            "LEFT JOIN WORDOFQUESTION as woq on woq.idQuestion = q.id " +
            "LEFT JOIN WEIGHTFORPHONEME as wfp on wfp.idQuestion = q.id " +
            "set  " +
            "obj.isDeleted=true,om.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true," +
            "q.isDeleted=true,woq.isDeleted=true,wfp.isDeleted=true where obj.id = 'paramObjId'";

    private String SQL_DELETE_TEST =  "Update TEST as t " +
            "left join TESTMAPPING as tm on tm.idTest = t.id " +
            "left join LESSONCOLLECTION as ls on ls.id = tm.idLessonCollection " +
            "left join LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id " +
            "left join QUESTION as q on q.id = lmq.idQuestion " +
            "LEFT JOIN WORDOFQUESTION as woq on woq.idQuestion = q.id " +
            "LEFT JOIN WEIGHTFORPHONEME as wfp on wfp.idQuestion = q.id " +
            "set  " +
            "t.isDeleted=true,tm.isDeleted=true,ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true,woq.isDeleted=true,wfp.isDeleted=true where t.id = 'paramTestId'";


    private String SQL_DELETE_LESSON =  "Update LESSONCOLLECTION as ls  " +
            "left join LESSONMAPPINGQUESTION as lmq on lmq.idLesson = ls.id " +
            "left join QUESTION as q on q.id = lmq.idQuestion " +
            "LEFT JOIN WORDOFQUESTION as woq on woq.idQuestion = q.id " +
            "LEFT JOIN WEIGHTFORPHONEME as wfp on wfp.idQuestion = q.id " +
            "set  " +
            "ls.isDeleted=true,lmq.isDeleted=true,q.isDeleted=true,woq.isDeleted=true,wfp.isDeleted=true " +
            "where ls.id = 'paramLessonId'";

    private String SQL_DELETE_QUESTION =  "Update QUESTION as q " +
            "LEFT JOIN WORDOFQUESTION as woq on woq.idQuestion = q.id " +
            "LEFT JOIN WEIGHTFORPHONEME as wfp on wfp.idQuestion = q.id " +
            "set  " +
            "q.isDeleted=true,woq.isDeleted=true,wfp.isDeleted=true where q.id = 'paramQuestionId'";





    private String SQL_GET_ALL_QUESTION_IN_TEST = "select q.id, q.name,q.description,q.type,lmq.position from QUESTION as q inner join LESSONMAPPINGQUESTION as lmq " +
            "on q.id = lmq.idQuestion inner join LESSONCOLLECTION as l on lmq.idLesson = l.id " +
            "inner join TESTMAPPING as tm on tm.idLESSONCOLLECTION = l.id " +
            "inner join TEST as t on t.id = tm.idTest " +
            "where t.id='paramTestId' and t.isDeleted=false and tm.isDeleted=false and lmq.isDeleted=false " +
            "and l.isDeleted=false and q.isDeleted=false order by lmq.position";



    private String SQL_CHECK_COURSE_ASSIGN_CLASS = "select c.id from COURSE as c inner join COURSEMAPPINGCLASS as cmc on c.id = cmc.idCourse\n" +
            "inner join CLASSMAPPINGTEACHER as cmt on cmc.idClass = cmt.idClass " +
            "where c.id='paramCid' and cmt.teacherName='paramTeacherName' and c.isDeleted=false and cmc.isDeleted=false and cmt.isDeleted=false";

    /**
     *
     * @param idCourse
     * @param teacherName
     * @return
     */
    public String getSqlCheckCourseAssignClass (String idCourse, String teacherName){
        String sql = SQL_CHECK_COURSE_ASSIGN_CLASS;
        sql = sql.replaceAll("paramCid",idCourse);
        sql = sql.replaceAll("paramTeacherName",teacherName);
        return sql;
    }

    /**
     *
     * @param idTest
     */
    public String getSqlDeleteTest(String idTest){
        String sql = SQL_DELETE_OBJ;
        sql = sql.replaceAll("paramTestId",idTest);
        return sql;
    }

    /**
     *
     * @param idObj
     * @return
     */
    public String getSqlDeleteObj(String idObj){
        String sql = SQL_DELETE_OBJ;
        sql = sql.replaceAll("paramObjId",idObj);
        return sql;
    }


    /**
     *
     * @param idLesson
     * @return
     */
    public String getSqlDeleteLesson(String idLesson){
        String sql = SQL_DELETE_LESSON;
        sql = sql.replaceAll("paramLessonId",idLesson);
        return sql;
    }

    /**
     *
     * @param idQuestion
     * @return
     */
    public String getSqlDeleteQuestion(String idQuestion){
        String sql = SQL_DELETE_QUESTION;
        sql = sql.replaceAll("paramQuestionId",idQuestion);
        return sql;
    }


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
     * @param idCourse
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

    public String getSqlQuestionInTest(String idTest){
        String sql = SQL_GET_ALL_QUESTION_IN_TEST;
        sql = sql.replaceAll("paramTestId",idTest);
        return sql;
    }
}
