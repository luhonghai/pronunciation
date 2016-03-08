package com.cmg.merchant.common;

/**
 * Created by lantb on 2016-03-07.
 */
public class SQL {

    private String SQL_GET_COURSE_SHARE_IN_COMPANY = "Select c.id,c.name, c.description, cp.companyName, " +
            "m.state, m.dateCreated from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.deleted=false and m.deleted=false and m.status='paramStatus' " +
            "and m.tId !='paramTeacherId' and m.cpID ='paramCompanyId' and m.sr='paramSharing'";

    private String SQL_GET_COURSE_CREATE_BY_TEACHER = "Select c.name, c.description, cp.companyName, m.state, m.dateCreated from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.deleted=false and m.deleted=false and m.status='paramStatus' " +
            "and m.tId ='paramTeacherId' and m.cpID ='paramCompanyId' and m.sr='paramSharing'";

    private String SQL_GET_COURSE_SHARE_ALL = "Select c.name, c.description, cp.companyName, m.state, m.dateCreated from COURSE as c " +
            "inner join COURSEMAPPINGTEACHER as m on c.id = m.cID " +
            "inner join CLIENTCODE as cp on m.cpID = cp.id " +
            "where c.deleted=false and m.deleted=false and m.status='paramStatus' " +
            "and m.tId !='paramTeacherId' and m.cpID !='paramCompanyId' and m.sr='paramSharing'";

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
    public String getSqlShareIN(String status, String tId, String cpId, String sr){
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
    public String getSqlCreatedByTeacher(String status, String tId, String cpId, String sr){
        String sql = SQL_GET_COURSE_CREATE_BY_TEACHER;
        sql = sql.replaceAll("paramStatus", status);
        sql = sql.replaceAll("paramTeacherId", tId);
        sql = sql.replaceAll("paramCompanyId", cpId);
        sql = sql.replaceAll("paramSharing", sr);
        return sql;
    }
}
