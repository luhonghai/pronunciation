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
