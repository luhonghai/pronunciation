package com.cmg.merchant.common;

/**
 * Created by lantb on 2016-04-05.
 */
public class SqlReport {


    private String SQL_LIST_CLASS_BY_TEACHER = "Select c.id, c.className from CLASSJDO as c " +
            "inner join CLASSMAPPINGTEACHER as m on c.id = m.idClass " +
            "where m.teacherName='paramTName' and c.isDeleted=false and m.isDeleted=false";

    private String SQL_LIST_STUDENT_IN_CLASS = "select id,STUDENTNAME from STUDENTMAPPINGCLASS where IDCLASS='paramCid' and isDeleted=false order by STUDENTNAME ASC";



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
