package com.cmg.merchant.common;

/**
 * Created by lantb on 2016-05-26.
 */
public class SqlReportPhonemes {

    private String SQL_CALCULATE_PHONEME_SCORE_BY_DAY = "select ulh.SERVERTIME, AVG(pls.TOTALSCORE) from USERLESSONHISTORY as ulh " +
            "inner join PHONEMELESSONSCORE as pls " +
            "on ulh.id=pls.IDUSERLESSONHISTORY " +
            "inner join IPAMAPARPABET as map " +
            "on map.ARPABET = pls.PHONEME " +
            "where ulh.username='paramStudent' " +
            "and map.ARPABET='paramIpa' " +
            "and ulh.SERVERTIME BETWEEN ? AND ? order by FROM_UNIXTIME(SERVERTIME/1000)";

    /**
     *
     * @param studentName
     * @param arpabet
     * @return
     */
    public String getSqlCalculatePhoneScoreByDay(String studentName, String arpabet){
        String sql = SQL_CALCULATE_PHONEME_SCORE_BY_DAY;
        sql = sql.replaceAll("paramStudent",studentName);
        sql = sql.replaceAll("paramIpa",arpabet);
        return sql;
    }
}
