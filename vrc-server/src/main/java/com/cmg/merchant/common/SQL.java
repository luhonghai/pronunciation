package com.cmg.merchant.common;

/**
 * Created by lantb on 2016-03-07.
 */
public class SQL {

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
}
