package com.cmg.merchant.common;

/**
 * Created by lantb on 2016-03-07.
 */
public class SQL {

    private String SQL_GET_COURSE_IN_MY_COURSE = "Select c.id,c.name, c.description, " +
            "cp.companyName, m.state, m.dateCreated,cp.id,m.cpIdClone,m.sr,m.status from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false " +
            "and m.tId ='paramTeacherId' and m.cpID ='paramCompanyId'";

    private String SQL_GET_COURSE_SEARCH_HEADER_IN_MY_COURSE = "Select c.id,c.name, c.description, " +
            "cp.companyName, m.state, m.dateCreated,cp.id,m.cpIdClone,m.sr,m.status from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false " +
            "and m.tId ='paramTeacherId' and m.cpID ='paramCompanyId' and LCASE(C.NAME) like '%paramCname%'";

    private String SQL_SEARCH_COURSE_DETAIL_IN_MY_COURSE = "Select c.id,c.name, c.description, " +
            "cp.companyName, m.state, m.dateCreated,cp.id,m.cpIdClone,m.sr,m.status from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpIdClone = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false and LCASE(C.NAME) like '%paramCourseName%' " +
            "and LCASE(cp.companyName) like '%paramCompanyName%' and m.tId='paramTeacherId' and m.cpID ='paramCompanyId' " +
            "and (m.dateCreated between 'paramDateFrom' and 'paramDateTo') order by c.name";

    private String SQL_GET_COURSE_MAPPING_TEACHER = "Select c.id,c.name, c.description " +
            "from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false " +
            "and m.tId ='paramTeacherId' and m.cpID ='paramCompanyId' and m.status='paramStatus'";



    private String SQL_GET_COURSE_SHARE_IN_COMPANY = "Select c.id,c.name, c.description, cp.companyName, " +
            "m.state, m.dateCreated,cp.id from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false and m.status='paramStatus' " +
            "and m.tId !='paramTeacherId' and m.cpID ='paramCompanyId' and m.sr='paramSharing'";

    private String SQL_GET_COURSE_CREATE_BY_TEACHER = "Select c.id,c.name, c.description, " +
            "cp.companyName, m.state, m.dateCreated,cp.id from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false and m.status='paramStatus' " +
            "and m.tId ='paramTeacherId' and m.cpID ='paramCompanyId'";

    private String SQL_GET_COURSE_SHARE_ALL = "Select c.id,c.name, c.description, cp.companyName, " +
            "m.state, m.dateCreated,cp.id from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false and m.status='paramStatus' " +
            "and m.tId !='paramTeacherId' and m.sr='paramSharing'";


    private String SQL_SUGGEST_COURSE = "Select c.id,c.name, c.description, cp.companyName, " +
            "m.state, m.dateCreated,cp.id,m.sr from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false and LCASE(C.NAME) like '%paramName%' and m.status='paramStatus'";

    private String SQL_SUGGEST_COMPANY = "Select companyName " +
            "from CLIENTCODE " +
            "where isDeleted=false and LCASE(companyName) like '%paramName%'";


    private String SQL_SEARCH_COURSE_HEADER = "Select c.id,c.name, c.description, cp.companyName, " +
            "m.state, m.dateCreated,m.sr,cp.id,m.tId from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false and LCASE(C.NAME) like '%paramName%' and m.status='paramStatus' order by c.name";

    private String SQL_SEARCH_COURSE_DETAIL = "Select c.id,c.name, c.description, cp.companyName, " +
            "m.state, m.dateCreated,m.sr,cp.id,m.tId from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.isDeleted=false and m.isDeleted=false and LCASE(C.NAME) like '%paramCourseName%' " +
            "and LCASE(cp.companyName) like '%paramCompanyName%' and m.status='paramStatus' " +
            "and (m.dateCreated between 'paramDateFrom' and 'paramDateTo') order by c.name";

    private String SQL_LESSON_MAPPING_OBJECTIVE="Select l.id,l.name,l.description,l.nameUnique,l.type,o.index FROM LESSONCOLLECTION l " +
            "inner join OBJECTIVEMAPPING o on l.id=o.idLessonCollection " +
            "WHERE o.idObjective='paramMappingId' and l.isDeleted=false and o.isDeleted=false order by o.index";

    private String SQL_QUESTION_MAPPING_LESSON="Select question.id,question.name,question.description FROM QUESTION question " +
            "inner join LESSONMAPPINGQUESTION mapping on question.id=mapping.idQuestion " +
            "WHERE mapping.idLesson='paramMappingId' and question.isDeleted=false and mapping.isDeleted=false";


    private String SQL_COURSE_FOR_STUDENT="Select c.id,c.name FROM COURSE as c " +
            "inner join COURSEMAPPINGCLASS as cmc on c.id=cmc.idCourse " +
            "inner join STUDENTMAPPINGCLASS as smc on cmc.idClass=smc.idClass "+
            "inner join CLASSMAPPINGTEACHER as cmt on cmc.idClass=cmt.idClass "+
            "WHERE smc.studentName='paramStudentName' and cmt.teacherName='paramTeacherName' and c.isDeleted=false and cmc.isDeleted=false and smc.isDeleted=false and cmt.isDeleted=false";
    private String SQL_LEVEL_FROM_COURSE="Select l.id,l.name FROM LEVEL as l " +
            "inner join COURSEMAPPINGLEVEL as cml on l.id=cml.idLevel " +
            "WHERE cml.idCourse='paramIdCourse' and l.isDeleted=false and cml.isDeleted=false";

    private String SQL_OBJ_FROM_LEVEL="Select obj.id,obj.name FROM OBJECTIVE as obj " +
            "inner join COURSEMAPPINGDETAIL as cmd on obj.id=cmd.idChild " +
            "WHERE cmd.idLevel='paramIdLevel' and obj.isDeleted=false and cmd.isDeleted=false";

    private String SQL_LESSON_FROM_OBJ="Select lesson.ID,lesson.NAME,lesson.DATECREATED FROM LESSONCOLLECTION as lesson " +
            "inner join OBJECTIVEMAPPING as mapping on lesson.ID=mapping.IDLESSONCOLLECTION " +
            "WHERE mapping.IDOBJECTIVE='paramIdObj' and DATEDIFF(NOW(),lesson.DATECREATED) < 90 and lesson.isDeleted=false and mapping.isDeleted=false ORDER BY lesson.DATECREATED ASC";

    private String SQL_GET_SCORE_FOR_LESSON="SELECT AVG(score.SCORE) FROM USERLESSONHISTORY as score " +
            "INNER JOIN USER as u on score.USERNAME=u.USERNAME " +
            "inner join SESSIONSCORE as session on session.IDUSERLESSONHISTORY = score.id " +
            "WHERE score.WORD IN " +
            "(SELECT word.WORD FROM WORDCOLLECTION as word " +
            "INNER JOIN WORDOFQUESTION as wordMapping on word.id=wordMapping.IDWORDCOLLECTION " +
            "INNER JOIN LESSONMAPPINGQUESTION as mappingLesson on wordMapping.IDQUESTION= mappingLesson.IDQUESTION " +
            "INNER JOIN LESSONCOLLECTION as lesson on lesson.ID=mappingLesson.IDLESSON " +
            "WHERE lesson.ID='paramIdLesson' and word.isDeleted=false and wordMapping.isDeleted=false " +
            "and mappingLesson.isDeleted=false and lesson.isDeleted=false " +
            "AND (DATEDIFF(NOW(),lesson.DATECREATED) > 1) " +
            "ORDER BY lesson.DATECREATED ASC) " +
            "AND u.USERNAME='paramStudentName' and session.IDLESSONCOLLECTION='paramIdLesson' and score.TYPE='Q' and score.ISDELETED=FALSE";

    private String SQL_GET_LIST_SCORE_STUDENT_WORD="SELECT score.SCORE FROM USERLESSONHISTORY as score INNER JOIN USER as u on score.USERNAME=u.USERNAME " +
            "WHERE score.WORD IN " +
            "(SELECT word.WORD FROM WORDCOLLECTION as word " +
            "INNER JOIN WORDOFQUESTION as wordMapping on word.id=wordMapping.IDWORDCOLLECTION " +
            "INNER JOIN LESSONMAPPINGQUESTION as mappingLesson on wordMapping.IDQUESTION=mappingLesson.IDQUESTION " +
            "INNER JOIN LESSONCOLLECTION as lesson on lesson.ID=mappingLesson.IDLESSON " +
            "WHERE lesson.ID='paramIdLesson' and word.isDeleted=false and wordMapping.isDeleted=false and mappingLesson.isDeleted=false and lesson.isDeleted=false " +
            "AND (DATEDIFF(NOW(),lesson.DATECREATED) > 1) " +
            "ORDER BY lesson.DATECREATED ASC) " +
            "AND u.USERNAME='paramStudentName' and score.TYPE='Q' and score.ISDELETED=FALSE";
    private String SQL_GET_LIST_WORD_LESSON="SELECT score.WORD FROM USERLESSONHISTORY as score INNER JOIN USER as u on score.USERNAME=u.USERNAME " +
            "WHERE score.WORD IN " +
            "(SELECT word.WORD FROM WORDCOLLECTION as word " +
            "INNER JOIN WORDOFQUESTION as wordMapping on word.id=wordMapping.IDWORDCOLLECTION " +
            "INNER JOIN LESSONMAPPINGQUESTION as mappingLesson on wordMapping.IDQUESTION=mappingLesson.IDQUESTION " +
            "INNER JOIN LESSONCOLLECTION as lesson on lesson.ID=mappingLesson.IDLESSON " +
            "WHERE lesson.ID='paramIdLesson' and word.isDeleted=false and wordMapping.isDeleted=false and mappingLesson.isDeleted=false and lesson.isDeleted=false " +
            "AND (DATEDIFF(NOW(),lesson.DATECREATED) > 1) " +
            "ORDER BY lesson.DATECREATED ASC) " +
            "AND u.USERNAME='paramStudentName' and score.TYPE='Q' and score.ISDELETED=FALSE";

    private String SQL_GET_LIST_PHONEME_LESSON="SELECT phoneme.PHONEME FROM PHONEMELESSONSCORE as phoneme INNER JOIN USERLESSONHISTORY as score on score.ID=phoneme.IDUSERLESSONHISTORY " +
            "INNER JOIN USER as u on u.USERNAME=score.USERNAME " +
            "WHERE score.WORD IN " +
            "(SELECT word.WORD FROM WORDCOLLECTION as word " +
            "INNER JOIN WORDOFQUESTION as wordMapping on word.id=wordMapping.IDWORDCOLLECTION " +
            "INNER JOIN LESSONMAPPINGQUESTION as mappingLesson on wordMapping.IDQUESTION=mappingLesson.IDQUESTION " +
            "INNER JOIN LESSONCOLLECTION as lesson on lesson.ID=mappingLesson.IDLESSON " +
            "WHERE lesson.ID='paramIdLesson' and word.isDeleted=false and wordMapping.isDeleted=false and mappingLesson.isDeleted=false and lesson.isDeleted=false " +
            "AND (DATEDIFF(NOW(),lesson.DATECREATED) > 1) " +
            "ORDER BY lesson.DATECREATED ASC) " +
            "AND u.USERNAME='paramStudentName' and score.TYPE='Q'  and score.ISDELETED=FALSE and phoneme.ISDELETED=FALSE";

    private String SQL_GET_LIST_SCORE_PHONEME_STUDENT="SELECT phoneme.TOTALSCORE FROM PHONEMELESSONSCORE as phoneme INNER JOIN USERLESSONHISTORY as score on score.ID=phoneme.IDUSERLESSONHISTORY " +
            "INNER JOIN USER as u on u.USERNAME=score.USERNAME " +
            "WHERE score.WORD IN " +
            "(SELECT word.WORD FROM WORDCOLLECTION as word " +
            "INNER JOIN WORDOFQUESTION as wordMapping on word.id=wordMapping.IDWORDCOLLECTION " +
            "INNER JOIN LESSONMAPPINGQUESTION as mappingLesson on wordMapping.IDQUESTION=mappingLesson.IDQUESTION " +
            "INNER JOIN LESSONCOLLECTION as lesson on lesson.ID=mappingLesson.IDLESSON " +
            "WHERE lesson.ID='paramIdLesson' and word.isDeleted=false and wordMapping.isDeleted=false and mappingLesson.isDeleted=false and lesson.isDeleted=false " +
            "AND (DATEDIFF(NOW(),lesson.DATECREATED) > 1) " +
            "ORDER BY lesson.DATECREATED ASC) " +
            "AND u.USERNAME='paramStudentName' and score.TYPE='Q'  and score.ISDELETED=FALSE and phoneme.ISDELETED=FALSE";


    /**
     *
     * @param tId
     * @param cpId
     * @param cName
     * @return
     */
    public String getSqlDisplayCourseSearchHeaderInMyCourse(String tId, String cpId, String cName){
        String sql = SQL_GET_COURSE_SEARCH_HEADER_IN_MY_COURSE;
        sql = sql.replaceAll("paramTeacherId", tId);
        sql = sql.replaceAll("paramCompanyId", cpId);
        sql = sql.replaceAll("paramCname", cName);
        return sql;
    }

    /**
     *
     * @param tId
     * @param cpId
     * @return
     */
    public String getSqlDisplayCourseSearchDetailInMyCourse(String tId, String cpId,
                                                            String companyName ,String courseName, String dateFrom, String dateTo){
        String sql = SQL_SEARCH_COURSE_DETAIL_IN_MY_COURSE;
        sql = sql.replaceAll("paramCourseName", courseName);
        sql = sql.replaceAll("paramCompanyName", companyName);
        sql = sql.replaceAll("paramTeacherId", tId);
        sql = sql.replaceAll("paramCompanyId", cpId);
        sql = sql.replaceAll("paramDateFrom", dateFrom);
        sql = sql.replaceAll("paramDateTo", dateTo);
        if(companyName==""){
            sql = sql.replaceAll("m.cpIdClone","m.cpId");
        }
        return sql;
    }


    /**
     *
     * @param tId
     * @param cpId
     * @param status
     * @return
     */
    public String getSqlDisplayCourseMappingClass(String tId, String cpId, String status){
        String sql = SQL_GET_COURSE_MAPPING_TEACHER;
        sql = sql.replaceAll("paramTeacherId", tId);
        sql = sql.replaceAll("paramCompanyId", cpId);
        sql = sql.replaceAll("paramStatus", status);
        return sql;
    }

    /**
     *
     *
     * @param tId
     * @param cpId
     * @return
     */
    public String getSqlDisplayCourseInMyCourse( String tId, String cpId){
        String sql = SQL_GET_COURSE_IN_MY_COURSE;
        sql = sql.replaceAll("paramTeacherId", tId);
        sql = sql.replaceAll("paramCompanyId", cpId);
        return sql;
    }

    /**
     *
     * @param status
     * @param tId
     * @param cpId
     * @return
     */
    public String getSqlShareAll(String status, String tId, String cpId, String sr){
        String sql = SQL_GET_COURSE_SHARE_ALL;
        sql = sql.replaceAll("paramStatus", status);
        sql = sql.replaceAll("paramTeacherId", tId);
        sql = sql.replaceAll("paramCompanyId", cpId);
        sql = sql.replaceAll("paramSharing", sr);
        return sql;
    }



    /**
     *
     * @param status
     * @param tId
     * @param cpId
     * @return
     */
    public String getSqlShareIN( String cpId,String tId, String sr,String status){
        String sql = SQL_GET_COURSE_SHARE_IN_COMPANY;
        sql = sql.replaceAll("paramStatus", status);
        sql = sql.replaceAll("paramTeacherId", tId);
        sql = sql.replaceAll("paramCompanyId", cpId);
        sql = sql.replaceAll("paramSharing", sr);
        return sql;
    }


    /**
     *
     * @param status
     * @param tId
     * @param cpId
     * @return
     */
    public String getSqlCreatedByTeacher(String status, String tId, String cpId){
        String sql = SQL_GET_COURSE_CREATE_BY_TEACHER;
        sql = sql.replaceAll("paramStatus", status);
        sql = sql.replaceAll("paramTeacherId", tId);
        sql = sql.replaceAll("paramCompanyId", cpId);
        return sql;
    }


    public String getSqlSuggestCourse(String status, String courseName){
        String sql = SQL_SUGGEST_COURSE;
        sql = sql.replaceAll("paramStatus", status);
        sql = sql.replaceAll("paramName", courseName);
        return sql;
    }

    public String getSqlSuggestCompany(String status, String companyName){
        String sql = SQL_SUGGEST_COMPANY;
        sql = sql.replaceAll("paramName", companyName);
        return sql;
    }

    public String getSqlSearchCourseHeader(String status, String courseName){
        String sql = SQL_SEARCH_COURSE_HEADER;
        sql = sql.replaceAll("paramStatus", status);
        sql = sql.replaceAll("paramName", courseName);
        return sql;
    }


    public String getSqlSearchCourseDetail(String status,String companyName ,String courseName, String dateFrom, String dateTo){
        String sql = SQL_SEARCH_COURSE_DETAIL;
        sql = sql.replaceAll("paramStatus", status);
        sql = sql.replaceAll("paramCourseName", courseName);
        sql = sql.replaceAll("paramCompanyName", companyName);
        sql = sql.replaceAll("paramDateFrom", dateFrom);
        sql = sql.replaceAll("paramDateTo", dateTo);
        return sql;
    }
    public String getSqlLessonMappingObjective(String idObjective){
        String sql=SQL_LESSON_MAPPING_OBJECTIVE;
        sql=sql.replaceAll("paramMappingId",idObjective);
        return sql;
    }

    public String getSqlQuestionMappingLesson(String idLesson){
        String sql=SQL_QUESTION_MAPPING_LESSON;
        sql=sql.replaceAll("paramMappingId",idLesson);
        return sql;
    }
    public String getSqlCourseForStudent(String teacher, String student){
        String sql = SQL_COURSE_FOR_STUDENT;
        sql = sql.replaceAll("paramTeacherName", teacher);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }
    public String getSqlLevelFromCourse(String idCourse){
        String sql = SQL_LEVEL_FROM_COURSE;
        sql = sql.replaceAll("paramIdCourse", idCourse);
        return sql;
    }
    public String getSqlOBJFromLevel(String idLevel){
        String sql = SQL_OBJ_FROM_LEVEL;
        sql = sql.replaceAll("paramIdLevel", idLevel);
        return sql;
    }
    public String getSqlLessonFromObj(String idObj){
        String sql = SQL_LESSON_FROM_OBJ;
        sql = sql.replaceAll("paramIdObj", idObj);
        return sql;
    }

    public String getSqlScoreStudent(String idLesson,String student){
        String sql = SQL_GET_SCORE_FOR_LESSON;
        sql = sql.replaceAll("paramIdLesson", idLesson);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }

    public String getSqlScoreClass(String idLesson,String student){
        String sql = SQL_LESSON_FROM_OBJ;
        sql = sql.replaceAll("paramIdLesson", idLesson);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }
    public String getSqlListWordLesson(String idLesson,String student){
        String sql = SQL_GET_LIST_WORD_LESSON;
        sql = sql.replaceAll("paramIdLesson", idLesson);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }
    public String getSqlListPhonemeLesson(String idLesson,String student){
        String sql = SQL_GET_LIST_PHONEME_LESSON;
        sql = sql.replaceAll("paramIdLesson", idLesson);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }
    public String getSqlWordStudentScore(String idLesson,String student){
        String sql = SQL_GET_LIST_SCORE_STUDENT_WORD;
        sql = sql.replaceAll("paramIdLesson", idLesson);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }
    public String getSqlWordClassScore(String idLesson,String student){
        String sql = SQL_LESSON_FROM_OBJ;
        sql = sql.replaceAll("paramIdLesson", idLesson);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }
    public String getSqlPhonemesClassScore(String idLesson,String student){
        String sql = SQL_LESSON_FROM_OBJ;
        sql = sql.replaceAll("paramIdLesson", idLesson);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }
    public String getSqlPhonemesStudentScore(String idLesson,String student){
        String sql = SQL_GET_LIST_SCORE_PHONEME_STUDENT;
        sql = sql.replaceAll("paramIdLesson", idLesson);
        sql = sql.replaceAll("paramStudentName", student);
        return sql;
    }
}
