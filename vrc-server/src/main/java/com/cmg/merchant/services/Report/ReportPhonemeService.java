package com.cmg.merchant.services.Report;

import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.merchant.dao.report.ReportLessonDAO;
import com.cmg.merchant.dao.report.ReportPhoneDao;
import com.cmg.merchant.util.DateUtil;
import com.cmg.vrc.data.jdo.Reports;
import com.cmg.vrc.data.jdo.StudentMappingClass;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-04-14.
 */
public class ReportPhonemeService {

    class ListStudent{
        private String message;
        private ArrayList<String> listStudent;

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
    }

    /**
     *
     * @param teacherName
     * @return
     */
    public String generateListStudent(String teacherName){
        ListStudent list = new ListStudent();
        Gson gson = new Gson();
        ReportPhoneDao dao = new ReportPhoneDao();
        try {
            ArrayList<String> tmp =dao.getListStudentByTeacher(teacherName);
            list.setListStudent(tmp);
            list.setMessage("success");
        }catch (Exception e){
            list.setMessage("error");
        }
        return gson.toJson(list);
    }

    /**
     *
     * @return
     */
    public String generatePhonemeList(){
        Gson gson = new Gson();
        Reports report = new Reports();
        ReportPhoneDao dao = new ReportPhoneDao();
        try {
            ArrayList<IpaMapArpabet> tmp = dao.listPhonemes();
            report.setListPhonemes(tmp);
        }catch (Exception e){
            report.setPhonemes(null);
        }
        return gson.toJson(report);
    }

    /**
     *
     * @param student
     * @param ipa
     * @param startDate
     * @param endDate
     * @return
     */
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
    }


}
