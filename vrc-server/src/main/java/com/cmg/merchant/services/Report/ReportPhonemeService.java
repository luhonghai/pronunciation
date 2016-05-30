package com.cmg.merchant.services.Report;

import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.merchant.dao.report.ReportLessonDAO;
import com.cmg.merchant.dao.report.ReportPhoneDao;
import com.cmg.merchant.util.DateUtil;
import com.cmg.vrc.data.jdo.Reports;
import com.cmg.vrc.data.jdo.StudentMappingClass;
import com.google.gson.Gson;

import java.util.*;

/**
 * Created by lantb on 2016-04-14.
 */
public class ReportPhonemeService {

    class DataLoading{
        private String message;
        private ArrayList<String> listStudent;
        private ArrayList<IpaMapArpabet> listPhonemes;
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<String> getListStudent() {
            return listStudent;
        }

        public void setListStudent(ArrayList<String> listStudent) {
            this.listStudent = listStudent;
        }

        public ArrayList<IpaMapArpabet> getListPhonemes() {
            return listPhonemes;
        }

        public void setListPhonemes(ArrayList<IpaMapArpabet> listPhonemes) {
            this.listPhonemes = listPhonemes;
        }
    }

    /**
     *
     * @param teacherName
     * @return
     */
    public String loadData(String teacherName){
        DataLoading list = new DataLoading();
        Gson gson = new Gson();
        ReportPhoneDao dao = new ReportPhoneDao();
        try {
            ArrayList<String> tmp =dao.getListStudentByTeacher(teacherName);
            ArrayList<IpaMapArpabet> ipa = dao.listPhonemes();
            list.setListStudent(tmp);
            list.setListPhonemes(ipa);
            list.setMessage("success");
        }catch (Exception e){
            list.setMessage("error");
        }
        return gson.toJson(list);
    }

 /*   *//**
     *
     * @param student
     * @param ipa
     * @param startDate
     * @param endDate
     * @return
     *//*
    public String generateDataDrawLineChart(String student, String ipa, String startDate, String endDate){
        Gson gson = new Gson();
        ReportPhoneDao dao = new ReportPhoneDao();
        DateUtil dateUtil = new DateUtil();
        Reports report = new Reports();
        try {
            startDate = dateUtil.convertDate(startDate);
            endDate = dateUtil.convertDate(endDate);
            ArrayList<ReportPhoneDao.ScorePhoneme> temp = dao.getScorePhonemeByStudent(student,ipa,startDate,endDate);
            report.setListScorePhonemes(temp);
        }catch (Exception e){
            report.setListScorePhonemes(null);
        }
        return gson.toJson(report);
    }*/

    /**
     *
     * @param student
     * @param ipa
     * @param startDate
     * @param type
     * @return
     */
    public String generateDataDrawLineChart(String student, String ipa, String startDate, String type){
        switch(type)
        {
            case "day" :
                return generateReportDay(student, ipa, startDate);
            case "week" :
                return generateReportWeek(student, ipa, startDate);
            case "month" :
                return generateReportMonth(student,ipa,startDate);
            case "year" :
               return generateReportYear(student,ipa,startDate);

        }
        return null;
    }

    /**
     *
     * @param student
     * @param ipa
     * @param startDate
     * @return
     */
    public String generateReportDay(String student, String ipa, String startDate){
        Gson gson = new Gson();
        Reports report = new Reports();
        DateUtil util = new DateUtil();
        ReportPhoneDao dao = new ReportPhoneDao();
        ArrayList<Date> list = util.initHourInDay(startDate);
        ArrayList<ReportPhoneDao.ScorePhoneme> temp = new ArrayList<>();
        for(int i = 0 ; i < list.size()-1;i++){
            System.out.println("============================");
            String sDate = util.convertDate(list.get(i));
            String fDate = util.convertDate(list.get(i+1));
            ReportPhoneDao.ScorePhoneme sp = dao.getScorePhonemeByDay(student,ipa,list.get(i).getTime(),list.get(i+1).getTime());
            System.out.println("start date : " + sDate);
            System.out.println("end date : " + fDate);
            if(sp!=null) {
                temp.add(sp);
            }else{
                ReportPhoneDao.ScorePhoneme scp = dao.initSp();
                scp.setServerTime(list.get(i).getTime());
                scp.setScore(0);
                //temp.add(scp);
            }
            System.out.println("============================");
        }
        report.setListScorePhonemes(temp);
        report.setDateStart(util.convertDate(list.get(0)));
        report.setDateEnd(util.convertDate(list.get(list.size()-1)));
        return gson.toJson(report);
    }
    /**
     *
     * @param student
     * @param ipa
     * @param startDate
     * @return
     */
    public String generateReportWeek(String student, String ipa, String startDate){
        Gson gson = new Gson();
        Reports report = new Reports();
        DateUtil util = new DateUtil();
        ReportPhoneDao dao = new ReportPhoneDao();
        ArrayList<Date> list = util.initDayInWeek(startDate);
        ArrayList<ReportPhoneDao.ScorePhoneme> temp = new ArrayList<>();
        for(int i = 0 ; i < list.size()-1;i++){
            System.out.println("============================");
            String sDate = util.convertDate(list.get(i));
            String fDate = util.convertDate(list.get(i+1));
            ReportPhoneDao.ScorePhoneme sp = dao.getScorePhonemeByDay(student,ipa,list.get(i).getTime(),list.get(i+1).getTime());
            System.out.println("start date : " + sDate);
            System.out.println("end date : " + fDate);
            if(sp!=null) {
                System.out.println("start date : " + sDate);
                System.out.println("end date : " + fDate);
                temp.add(sp);
                System.out.println("add sp :" + sp.getScore());
            }else{
                ReportPhoneDao.ScorePhoneme scp = dao.initSp();
                scp.setServerTime(list.get(i).getTime());
                scp.setScore(0);
               // temp.add(scp);
            }
            System.out.println("============================");
        }
        report.setListScorePhonemes(temp);
        report.setDateStart(util.convertDate(list.get(0)));
        report.setDateEnd(util.convertDate(list.get(list.size()-1)));
        return gson.toJson(report);
    }

    /**
     *
     * @param student
     * @param ipa
     * @param startDate
     * @return
     */
    public String generateReportMonth(String student, String ipa, String startDate){
        Gson gson = new Gson();
        Reports report = new Reports();
        DateUtil util = new DateUtil();
        ReportPhoneDao dao = new ReportPhoneDao();
        ArrayList<Date> list = util.initDayInMonth(startDate);
        ArrayList<ReportPhoneDao.ScorePhoneme> temp = new ArrayList<>();
        for(int i = 0 ; i < list.size()-1;i++){
            System.out.println("============================");
            String sDate = util.convertDate(list.get(i));
            String fDate = util.convertDate(list.get(i+1));
            ReportPhoneDao.ScorePhoneme sp = dao.getScorePhonemeByDay(student,ipa,list.get(i).getTime(),list.get(i+1).getTime());
            if(sp!=null) {
                System.out.println("start date : " + sDate);
                System.out.println("end date : " + fDate);
                temp.add(sp);
                System.out.println("add sp :" + sp.getScore());
            }else{
                ReportPhoneDao.ScorePhoneme scp = dao.initSp();
                scp.setServerTime(list.get(i).getTime());
                scp.setScore(0);
                //temp.add(scp);
            }
            System.out.println("============================");
        }
        report.setListScorePhonemes(temp);
        report.setDateStart(util.convertDate(list.get(0)));
        report.setDateEnd(util.convertDate(list.get(list.size() - 1)));
        return gson.toJson(report);
    }

    /**
     *
     * @param student
     * @param ipa
     * @param startDate
     * @return
     */
    public String generateReportYear(String student, String ipa, String startDate){
        Gson gson = new Gson();
        Reports report = new Reports();
        DateUtil util = new DateUtil();
        ReportPhoneDao dao = new ReportPhoneDao();
        ArrayList<Date> list = util.initDayInNextYear(startDate);
        ArrayList<ReportPhoneDao.ScorePhoneme> temp = new ArrayList<>();
        for(int i = 0 ; i < list.size()-1;i++){
            System.out.println("============================");
            String sDate = util.convertDate(list.get(i));
            String fDate = util.convertDate(list.get(i+1));
            ReportPhoneDao.ScorePhoneme sp = dao.getScorePhonemeByDay(student,ipa,list.get(i).getTime(),list.get(i+1).getTime());
            if(sp!=null) {
                System.out.println("start date : " + sDate);
                System.out.println("end date : " + fDate);
                temp.add(sp);
                System.out.println("add sp :" + sp.getScore());
            }else{
                ReportPhoneDao.ScorePhoneme scp = dao.initSp();
                scp.setServerTime(list.get(i).getTime());
                scp.setScore(0);
                //temp.add(scp);
            }
            System.out.println("============================");
        }
        report.setListScorePhonemes(temp);
        report.setDateStart(util.convertDate(list.get(0)));
        report.setDateEnd(util.convertDate(list.get(list.size() - 1)));
        return gson.toJson(report);
    }


}
