package com.cmg.merchant.dao.report;

import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.merchant.common.SqlReport;
import com.cmg.merchant.util.DateUtil;
import com.cmg.vrc.util.PersistenceManagerHelper;
import com.cmg.vrc.util.StringUtil;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-04-14.
 */
public class ReportPhoneDao {


    public class ScorePhoneme{
        long serverTime;
        int score;

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
    /**
     *
     * @param teacherName
     * @return
     */
    public ArrayList<String> getListStudentByTeacher(String teacherName){
        ArrayList<String> listStudent = new ArrayList<>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlListStudentByTeacher(teacherName);
        System.out.println("sql generate list student by teacher : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for(Object obj : tmp){
                    String s = (String) StringUtil.isNull(obj, "null");
                    if(s!="null"){
                        listStudent.add(s);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return listStudent;
    }


    /**
     *
     * @param student
     * @param arpabet
     * @param startDate
     * @param endDate
     * @return
     */
    public ArrayList<ScorePhoneme> getScorePhonemeByStudent(String student, String arpabet, String startDate, String endDate){
        ArrayList<ScorePhoneme> listScore = new ArrayList<>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculatePhoneScoreByUser(student, arpabet, startDate, endDate);
        System.out.println("sql get score of phoneme by student : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for(Object obj : tmp){
                    Object[] data = (Object[]) obj;
                    ScorePhoneme sp = new ScorePhoneme();
                    if(data[1]!=null){
                        Double value = (Double)data[1];
                        int score = (int) Math.round(value);
                        sp.setScore(score);
                    }else{
                        sp.setScore(0);
                    }
                    if(data[0]!=null){
                        long value = (long)data[0];
                        sp.setServerTime(value);
                    }
                    System.out.println(sp.getServerTime() + "-" + sp.getScore());
                    listScore.add(sp);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return listScore;
    }

    /**
     *
     * @return
     */
    public ArrayList<IpaMapArpabet> listPhonemes(){
        ArrayList<IpaMapArpabet> listPhonemes = new ArrayList<>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlListPhonemes();
        System.out.println("sql generate list phoneme : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for(Object obj : tmp){
                    Object[] data = (Object[]) obj;
                    IpaMapArpabet map = new IpaMapArpabet();
                    if(data[0]!=null){
                        String ipa = (String) StringUtil.isNull(data[0], "null");
                        map.setIpa(ipa);
                    }
                    if(data[1]!=null){
                        String arpabet = (String) StringUtil.isNull(data[1], "null");
                        map.setArpabet(arpabet);
                    }
                    listPhonemes.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return listPhonemes;
    }

}
