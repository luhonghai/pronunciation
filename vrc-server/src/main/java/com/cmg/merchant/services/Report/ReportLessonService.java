package com.cmg.merchant.services.Report;

import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.dao.level.LevelDAO;
import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.dao.report.ReportLessonDAO;
import com.cmg.vrc.data.dao.impl.StudentMappingTeacherDAO;
import com.cmg.vrc.data.jdo.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMGT400 on 3/28/2016.
 */
public class ReportLessonService {
    ReportLessonDAO reportLessonDAO=new ReportLessonDAO();
    CourseDAO courseDAO=new CourseDAO();
    LevelDAO levelDAO=new LevelDAO();
    ObjectiveDAO objectiveDAO=new ObjectiveDAO();
    Gson gson=new Gson();
    class ListClass{
        private String message;
        private List<ClassJDO> list;
    }
    class ListStudent{
        private String message;
        private List<StudentMappingClass> listSMC;
        private ArrayList<String> listStudent;
    }
    class ListCourse{
        private String message;
        private List<Course> listCourse;
        private List<StudentMappingTeacher> listStudent;
    }
    class ListLevel{
        private String message;
        private List<Level> listLevel;
    }
    class ListObjective{
        private String message;
        private List<Objective> listObj;
    }
    class ListLesson{
        private String message;
        private List<LessonCollection> listLesson;
    }
    class Information{
        private String message;
        private Reports reports;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Reports getReports() {
            return reports;
        }

        public void setReports(Reports reports) {
            this.reports = reports;
        }

        public Information(){

        }
    }
    /**
     *
     * @param teacherName
     * @return
     */
    public String listClass(String teacherName){
        ListClass obj = new ListClass();
        try {
            List<ClassJDO> temp = reportLessonDAO.getClassByTeacher(teacherName);
            obj.list = temp;
            obj.message = "success";
        }catch (Exception e){
            obj.message = "error";
            obj.list = null;
        }
        return gson.toJson(obj);
    }

    /**
     *
     * @param idClass
     * @param teacherName
     * @return
     */
    public String listStudent(String idClass,String teacherName){
        ListStudent listStudent=new ListStudent();
        String list=null;
        try {
            listStudent.message="success";
            listStudent.listSMC=reportLessonDAO.getListStudentForClass(idClass, teacherName);
            list = gson.toJson(listStudent);
        } catch (Exception e) {
            listStudent.message="error";
            listStudent.listSMC=new ArrayList<>();
            list = gson.toJson(listStudent);
        }
        return list;
    }


    /**
     *
     *
     * @param idClass
     * @return
     */
    public String listCourse(String idClass){
        ListCourse listCourse=new ListCourse();
        String list=null;
        try {
            listCourse.message="success";
            listCourse.listCourse = reportLessonDAO.getListCourse(idClass);
            list = gson.toJson(listCourse);
        } catch (Exception e) {
            listCourse.message="error";
            listCourse.listCourse=new ArrayList<>();
            list = gson.toJson(listCourse);
        }
        return list;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public String listLevel(String idCourse){
        ListLevel listLevel=new ListLevel();
        String list=null;
        try {
            listLevel.message="success";
            listLevel.listLevel=reportLessonDAO.getListLevel(idCourse);
            list = gson.toJson(listLevel);
        } catch (Exception e) {
            listLevel.message="error";
            listLevel.listLevel=new ArrayList<>();
            list = gson.toJson(listLevel);
        }
        return list;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public String listOBJ(String idLevel){
        ListObjective listObjective=new ListObjective();
        String list=null;
        try {
            listObjective.message="success";
            listObjective.listObj=reportLessonDAO.getListOBJ(idLevel);
            list = gson.toJson(listObjective);
        } catch (Exception e) {
            listObjective.message="error";
            listObjective.listObj=new ArrayList<>();
            list = gson.toJson(listObjective);
        }
        return list;
    }

    /**
     *
     * @param idObj
     * @return
     */
    public String listLesson(String idObj){
        ListLesson listLesson=new ListLesson();
        String list=null;
        try {
            listLesson.message="success";
            listLesson.listLesson=reportLessonDAO.getListLesson(idObj);
            list = gson.toJson(listLesson);
        } catch (Exception e) {
            listLesson.message="error";
            listLesson.listLesson=new ArrayList<>();
            list = gson.toJson(listLesson);
        }
        return list;
    }

    /**
     *
     * @param idLesson
     * @param student
     * @param idClass
     * @return
     */
    public String drawCircle(String idLesson, String student, String idClass){
        Information information = new Information();
        long startTime = System.currentTimeMillis();
        System.out.println("======draw circle==========");
        try {
            Reports report = new Reports();
            String latestSession = reportLessonDAO.getLatestSessionIdIn3Months(student,idLesson);
            if(latestSession!=null){
                boolean checkUserCompleted = reportLessonDAO.checkUserCompletedLesson(student,idLesson,latestSession);
                if(checkUserCompleted){
                    Reports tmp = reportLessonDAO.getStudentAvgScoreLesson(student,idLesson,latestSession);
                    report.setStudentScoreLesson(tmp.getStudentScoreLesson());
                    report.setDateCreated(tmp.getDateCreated());
                    report.setSessionId(tmp.getSessionId());
                    int classAvgScore = getAvgScoreOfClass(idClass,idLesson);
                    report.setClassAvgScoreLesson(classAvgScore);
                    information.setReports(report);
                    information.setMessage("success");
                }else{
                    information.setMessage("error");
                }
            }else{
                information.setMessage("error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            information.setMessage("error");
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("load data draw circle of lesson : " +idLesson +" from db of student : " + student + " spent :" + totalTime);
        return gson.toJson(information);
    }

    /**
     *
     * @param idClass
     * @param idLesson
     * @return
     */
    public int getAvgScoreOfClass(String idClass,String idLesson){
        List<StudentMappingClass> listStudent = reportLessonDAO.getListStudentForClass(idClass, "");
        int totalScore = 0;
        if(listStudent!=null && listStudent.size() > 0){
            for(StudentMappingClass stc : listStudent){
                String student = stc.getStudentName();
                String latestSession = reportLessonDAO.getLatestSessionIdIn3Months(student, idLesson);
                if(latestSession!=null) {
                    boolean checkUserCompleted = reportLessonDAO.checkUserCompletedLesson(student, idLesson, latestSession);
                    if (checkUserCompleted) {
                        Reports tmp = reportLessonDAO.getStudentAvgScoreLesson(student, idLesson, latestSession);
                        totalScore = totalScore + tmp.getStudentScoreLesson();
                    }
                }
            }
        }
        int avgScore = totalScore/listStudent.size();
        return avgScore;
    }

    public String draw(String idLesson,String student){
        Information information = new Information();
        String list=null;
        try {
            information.message="success";
            information.reports=getReport(idLesson,student);
        } catch (Exception e) {

        }
        return list;
    }
    public Reports getReport(String idLesson,String student){
        Reports reports=new Reports();
        try {
            /*reports.setStudentScoreLesson(reportLessonDAO.getStudentScoreLesson(idLesson,student));
            reports.setClassAvgScoreLesson(reportLessonDAO.getClassAvgScoreLesson(idLesson,student));
            reports.setWord(reportLessonDAO.getListWordLesson(idLesson, student));
            reports.setPhonemes(reportLessonDAO.getListPhonemeLesson(idLesson,student));
            reports.setWordStudentScore(reportLessonDAO.getWordStudentScore(idLesson,student));
            reports.setWordClassScore(reportLessonDAO.getWordClassScore(idLesson,student));
            reports.setPhonemesClassScore(reportLessonDAO.getPhonemesClassScore(idLesson,student));
            reports.setPhonemesStudentScore(reportLessonDAO.getPhonemesStudentScore(idLesson,student));*/
        }catch (Exception e){
            e.getStackTrace();
        }

        return reports;
    }

    /**
     *
     * @param student
     * @param idLesson
     * @param word
     * @param latestSession
     * @return
     */
    public int generateScoreWordByStudent(String student, String idLesson, String word, String latestSession){
        int temp = reportLessonDAO.getAvgScoreWordInLessonOfUser(student, idLesson, word, latestSession);
        return temp;
    }

    /**
     *
     *
     * @param idLesson
     * @param word
     * @return
     */
    public int generateClassScoreWord(List<StudentMappingClass> listStudent,String idLesson,String word){
        int totalScore = 0;
        int deviceNumber = 0;
        if(listStudent.size() > 0){
            for(StudentMappingClass st : listStudent){
                String student = st.getStudentName();
                String latestSessionStudent = reportLessonDAO.getLatestSessionIdIn3Months(student, idLesson);
                if(latestSessionStudent!=null && reportLessonDAO.checkUserCompletedLesson(student, idLesson, latestSessionStudent)){
                    int temp = reportLessonDAO.getAvgScoreWordInLessonOfUser(student, idLesson, word,latestSessionStudent);
                    if(temp  > 0){
                        totalScore = totalScore + temp;
                        deviceNumber ++;
                    }

                }
            }
        }
        if(totalScore == 0 && deviceNumber == 0) return 0;
        return listStudent.size() == 0 ? 0 : Math.round(totalScore/deviceNumber);
    }

    /**
     *
     * @param student
     * @param idLesson
     * @param ipa
     * @param latestSessionStudent
     * @return
     */
    public int generateStudentScorePhoneme(String student, String idLesson, String ipa, String latestSessionStudent){
        int temp = reportLessonDAO.getAvgScorePhonemesInLessonOfUser(student, idLesson, ipa,latestSessionStudent);
        if(temp == -1){
            temp = 0;
        }
        return temp;
    }

    /**
     *
     * @param listStudent
     * @param idLesson
     * @param ipa
     * @return
     */
    public int generateClassScorePhoneme(List<StudentMappingClass> listStudent,String idLesson, String ipa){
        int totalScore = 0;
        int deviceNumber = 0;
        if(listStudent.size() > 0){
            for(StudentMappingClass st : listStudent){
                String student = st.getStudentName();
                String latestSessionStudent = reportLessonDAO.getLatestSessionIdIn3Months(student, idLesson);
                if(latestSessionStudent!=null && reportLessonDAO.checkUserCompletedLesson(student, idLesson, latestSessionStudent)){
                    int temp = reportLessonDAO.getAvgScorePhonemesInLessonOfUser(student, idLesson, ipa,latestSessionStudent);
                    if(temp!=-1){
                        totalScore = totalScore + temp;
                        deviceNumber++;
                    }
                }
            }
        }
        if(totalScore == 0 && deviceNumber == 0) return 0;
        return listStudent.size() == 0 ? 0 : Math.round(totalScore/deviceNumber);
    }
    /**
     *
     * @param classId
     * @param student
     * @param idLesson
     * @return object report contain all the data to draw the report
     */
    public String generateDataForReportLesson(String classId, String student, String idLesson){
        Information container = new Information();
        Reports report = new Reports();
        long startTime = System.currentTimeMillis();
        System.out.println("=====draw report=========");
        try {
            List<Integer> studentScoreList = null;
            List<Integer> classScoreList = null;
            List<String> listWord = reportLessonDAO.getListWordInLesson(idLesson);
            String latestSessionStudent = reportLessonDAO.getLatestSessionIdIn3Months(student, idLesson);
            List<String> listPracticedWord = reportLessonDAO.getWordsInSession(student,idLesson,latestSessionStudent);
            List<StudentMappingClass> listStudent = reportLessonDAO.getListStudentForClass(classId, "");
            if(listWord!=null &&  listWord.size()>0){
                studentScoreList = new ArrayList<>();
                classScoreList = new ArrayList<>();
                for(String word : listWord){
                    if(listPracticedWord.contains(word)){
                        int studentScore = generateScoreWordByStudent(student, idLesson, word, latestSessionStudent);
                        studentScoreList.add(studentScore);
                        if(studentScore > 0){
                            classScoreList.add(generateClassScoreWord(listStudent,idLesson,word));
                        }else{
                            classScoreList.add(0);
                        }
                    }else{
                        studentScoreList.add(0);
                        classScoreList.add(0);
                    }

                }
                report.setWord(listWord);
                report.setWordClassScore(classScoreList);
                report.setWordStudentScore(studentScoreList);
            }

            List<String> listPhonemes = reportLessonDAO.getListPhonemes();
            List<String> listPhonemesPracticed = reportLessonDAO.getPhonemesInSession(student,idLesson,latestSessionStudent);
            if(listPhonemes!=null &&  listPhonemes.size()>0){
                studentScoreList = new ArrayList<>();
                classScoreList = new ArrayList<>();
                for(String ipa : listPhonemesPracticed){
                    if(listPhonemesPracticed.contains(ipa)){
                        int studentScore = generateStudentScorePhoneme(student, idLesson, ipa, latestSessionStudent);
                        studentScoreList.add(studentScore);
                        if(studentScore > 0){
                            classScoreList.add(generateClassScorePhoneme(listStudent, idLesson, ipa));
                        }else{
                            classScoreList.add(0);
                        }
                    }else{
                        studentScoreList.add(0);
                        classScoreList.add(0);
                    }

                }
                report.setPhonemes(listPhonemesPracticed);
                report.setPhonemesClassScore(classScoreList);
                report.setPhonemesStudentScore(studentScoreList);
            }
            container.setMessage("success");
            container.setReports(report);
        }catch (Exception e){
            container.setMessage("error");
            e.printStackTrace();
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("load data draw report of lesson : " +idLesson +" from db of student : " + student + " spent :" + totalTime);
        return gson.toJson(container);
    }
}
