package com.cmg.merchant.dao.report;

import com.cmg.lesson.data.dto.objectives.IndexLesson;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.common.SQL;
import com.cmg.merchant.common.SqlReport;
import com.cmg.merchant.data.dto.TempReport;
import com.cmg.merchant.util.DateUtil;
import com.cmg.vrc.data.jdo.ClassJDO;
import com.cmg.vrc.data.jdo.Reports;
import com.cmg.vrc.data.jdo.StudentMappingClass;
import com.cmg.vrc.data.jdo.StudentMappingTeacher;
import com.cmg.vrc.util.PersistenceManagerHelper;
import com.cmg.vrc.util.StringUtil;
import org.joda.time.DateTime;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.Convert;
import javax.jdo.metadata.TypeMetadata;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 3/28/2016.
 */
public class ReportLessonDAO {
    SimpleDateFormat format=new SimpleDateFormat("dd.MM.yyyy");

    DateFormat dateFormat= DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM, DateFormat.SHORT);
    /**
     *
     * @param tName
     * @return
     */
    public List<ClassJDO> getClassByTeacher(String tName){

        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sqlUtil = new SqlReport();
        String sql = sqlUtil.getSqlListClassByTeacher(tName);
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
        List<ClassJDO> list = new ArrayList<>();
        try {
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                ClassJDO c = new ClassJDO();
                if (data[0] != null) {
                    c.setId(data[0].toString());
                }
                if (data[1] != null) {
                    c.setClassName(data[1].toString());
                }
                list.add(c);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return list;
    }


    /**
     *
     * @param teacherName
     * @return
     */
    public List<StudentMappingClass> getListStudentForClass(String classId,String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport util = new SqlReport();
        String sql = util.getStudentInClass(classId, teacherName);
        System.out.println("get student in class : " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
        try {
            List<StudentMappingClass> list = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                StudentMappingClass smc = new StudentMappingClass();
                if (data[1] != null) {
                    smc.setStudentName(data[1].toString());
                }
                list.add(smc);
            }
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }



    /**
     *
     *
     * @param idClass
     * @return
     */
    public List<Course> getListCourse(String idClass){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql=new SqlReport();
        String query=sql.getSqlListCourseInClass(idClass);
        System.out.println("query get course in class : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        List<Course> list = new ArrayList<>();
        try {

            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Course course = new Course();
                if (data[0] != null) {
                    course.setId(data[0].toString());
                }else {
                    course.setId(null);
                }
                if (data[1] != null) {
                    course.setName(data[1].toString());
                }else{
                    course.setName(null);
                }
                list.add(course);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return list;
    }

    public List<Level> getListLevel(String idCourse){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        String query=sql.getSqlLevelFromCourse(idCourse);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Level> levels = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Level level = new Level();
                if (data[0] != null) {
                    level.setId(data[0].toString());
                }else {
                    level.setId(null);
                }
                if (data[1] != null) {
                    level.setName(data[1].toString());
                }else{
                    level.setName(null);
                }
                levels.add(level);
            }
            return levels;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public List<Objective> getListOBJ(String idLevel){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        String query=sql.getSqlOBJFromLevel(idLevel);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Objective> objectives = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Objective objective = new Objective();
                if (data[0] != null) {
                    objective.setId(data[0].toString());
                }else {
                    objective.setId(null);
                }
                if (data[1] != null) {
                    objective.setName(data[1].toString());
                }else{
                    objective.setName(null);
                }
                objectives.add(objective);
            }
            return objectives;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<LessonCollection> getListLesson(String idObj) throws ParseException {
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        String query=sql.getSqlLessonFromObj(idObj);
        System.out.println("query get all lesson in objective : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<LessonCollection> lessonCollections = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                LessonCollection lessonCollection = new LessonCollection();
                if (data[0] != null) {
                    lessonCollection.setId(data[0].toString());
                }else {
                    lessonCollection.setId(null);
                }
                if (data[1] != null) {
                    lessonCollection.setName(data[1].toString());
                }else{
                    lessonCollection.setName(null);
                }
                if (data[2] != null) {
                    lessonCollection.setDateCreated((Date) data[2]);
                    String s = format.format(lessonCollection.getDateCreated());
                    Date t  =   format.parse(s);
                }else{
                    lessonCollection.setDateCreated(null);
                }
                if (data[3] != null) {
                    lessonCollection.setTitle(data[3].toString());
                }else{
                    lessonCollection.setTitle(null);
                }
                lessonCollections.add(lessonCollection);
            }
            return lessonCollections;
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }


    /**
     *
     * @param idLesson
     * @param student
     * @return
     * @throws ParseException
     */
    public int getStudentScoreLesson(String idLesson,String student) throws Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql = new SQL();
        int scoreStudent = 0;
        String query=sql.getSqlScoreStudent(idLesson, student);
        System.out.println("sql calculate score student in lesson : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                Object obj = tmp.get(0);
                if(obj!=null){
                    Object[] array = (Object[]) obj;
                    if(array[0]!=null){
                        scoreStudent = Integer.parseInt(array[0].toString());
                    }
                }
            }
            return scoreStudent;
        } catch (Exception e) {
           e.printStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }

    }
    public int getClassAvgScoreLesson(String idLesson,String student) throws ParseException {
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        int scoreStudent=0;
        String query=sql.getSqlScoreStudent(idLesson, student);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            scoreStudent = (int) q.execute();
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return scoreStudent;
    }
    public List<String> getListWordLesson(String idLesson,String student) throws ParseException {
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        List<String> word=null;
        String query=sql.getSqlScoreStudent(idLesson, student);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            word = (List<String>) q.execute();
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return word;
    }
    public List<String> getListPhonemeLesson(String idLesson,String student) throws ParseException {
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        List<String> phoneme=null;
        String query=sql.getSqlScoreStudent(idLesson, student);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            phoneme = (List<String>) q.execute();
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return phoneme;
    }
    public List<Float> getWordStudentScore(String idLesson,String student) throws ParseException {
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        List<Float> wordStudentScore=null;
        String query=sql.getSqlScoreStudent(idLesson, student);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            wordStudentScore = (List<Float>) q.execute();
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return wordStudentScore;
    }

    public List<Float> getWordClassScore(String idLesson,String student) throws ParseException {
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        List<Float> wordStudentScore=null;
        String query=sql.getSqlScoreStudent(idLesson, student);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            wordStudentScore = (List<Float>) q.execute();
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return wordStudentScore;
    }
    public List<Float> getPhonemesClassScore(String idLesson,String student) throws ParseException {
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        List<Float> wordStudentScore=null;
        String query=sql.getSqlScoreStudent(idLesson, student);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            wordStudentScore = (List<Float>) q.execute();
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return wordStudentScore;
    }

    public List<Float> getPhonemesStudentScore(String idLesson,String student) throws ParseException {
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sql=new SQL();
        List<Float> wordStudentScore=null;
        String query=sql.getSqlScoreStudent(idLesson, student);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            wordStudentScore = (List<Float>) q.execute();
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return wordStudentScore;
    }






    //function to get data in the report of teacher console module

    /**
     *
     * @param student
     * @param idLesson
     * @return true if student completed the lesson
     */
    public boolean checkUserCompletedLesson(String student, String idLesson, String idSession){
        boolean completed = true;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCheckUserCompletedLesson(student, idLesson,idSession);
        System.out.println("sql check user completed lesson : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for(Object obj : tmp){
                    String s = (String) StringUtil.isNull(obj,"null");
                    System.out.println(s);
                    if(s!="null"){
                        completed = false;
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
        return completed;
    }

    /**
     *
     * @param student
     * @param idLesson
     * @return return avg score of student in lesson and the date time
     */
    public Reports getStudentAvgScoreLesson(String student,String idLesson, String sessionId){
        Reports report = new Reports();;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateUserScoreLesson(student, idLesson, sessionId);
        System.out.println("sql check get student avg score in lesson " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        ArrayList<TempReport> list = new ArrayList<>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[0]!=null && data[1]!=null && data[2]!=null){
                        boolean checkExisted = false;
                        TempReport temp = new TempReport();
                        temp.setIdQuestion(data[0].toString());
                        Double dbScore = (Double)data[1];
                        float score = (float) Math.round(dbScore);
                        temp.setScore(score);
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        temp.setCompletedDate(format.format(new Date((long) data[2])));
                        //additional logic for calculate score base on mobile.
                        float finalScore = 0;
                        int index = 0;
                        int repeat = 1;
                        for(int i = 0; i < list.size();i++){
                            if(list.get(i).getIdQuestion().equals(temp.getIdQuestion())){
                                checkExisted = true;
                                index = i;
                                repeat = repeat + 1;
                                finalScore = list.get(i).getScore() + temp.getScore();
                            }
                        }
                        if(!checkExisted){
                            System.out.println("add : " + temp.getIdQuestion() +":" +temp.getScore());
                            list.add(temp);
                        }else{
                            finalScore = (float) Math.round(finalScore/repeat);
                            list.get(index).setScore(finalScore);
                            System.out.println("refine : " + list.get(index).getIdQuestion() + ":" + list.get(index).getScore());
                        }
                    }
                    /*if(data[0]!=null){
                        System.out.println(data[0].getClass());
                        Double score = (Double)data[0];
                        report.setStudentScoreLesson(score.intValue());
                    }
                    if(data[1]!=null){
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        report.setDateCreated(format.format(new Date((long) data[1])));
                    }
                    if(data[2]!=null){
                        report.setSessionId(data[2].toString());
                    }*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        if(list.size()>0){
            float finalScoreStudent = 0;
            System.out.println("================");
            for(int i = 0 ; i < list.size();i++){
                System.out.println(list.get(i).getIdQuestion() +":" +list.get(i).getScore());
                finalScoreStudent = finalScoreStudent + list.get(i).getScore();
            }
            finalScoreStudent = (int) Math.round(finalScoreStudent/list.size());
            System.out.println("final score student : " + finalScoreStudent);
            int studentScore = (int) Math.round(finalScoreStudent);
            System.out.println("score student : " + finalScoreStudent);
            report.setStudentScoreLesson(studentScore);
            report.setDateCreated(list.get(0).getCompletedDate());
            return report;
        }else{
            return null;
        }

    }

    /**
     * @param idLesson
     * @return
     */
    public ArrayList<String> getListWordInLesson(String idLesson){
        ArrayList<String> listWord = new ArrayList<>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlListWordInLesson(idLesson);
        System.out.println("sql generate list word : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for(Object obj : tmp){
                    String s = (String) StringUtil.isNull(obj,"null");
                    if(s!="null"){
                        listWord.add(s);
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
        return listWord;
    }

    /**
     *
     * @param student
     * @param idLesson
     * @param word
     * @return
     */
    public int getAvgScoreWordInLessonOfUser(String student, String idLesson, String word, String latestSession){
        int score = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateScoreWord(student, idLesson, word, latestSession);
        System.out.println("sql generate score word of user " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        Double value = (Double)data[1];
                        score = (int) Math.round(value);
                    }else{
                        score = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            q.closeAll();
            pm.close();
        }
        return score;
    }

 /*   *//**
     *
     * @param classId
     * @param idLesson
     * @param word
     * @return
     *//*
    public int getAvgScoreWordInLessonOfClass(String classId, String idLesson, String word){
        int score = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateScoreWordOfClass(classId, idLesson, word);
        System.out.println("sql generate score word of class : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        Double value = (Double)data[1];
                        score = (int) Math.round(value);
                    }else{
                        score = 0;
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
        return score;
    }*/



    /*
     * @return
     */
    public ArrayList<String> getListPhonemes(){
        ArrayList<String> listPhonemes = new ArrayList<>();
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
                    listPhonemes.add(map.getIpa());
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

    /**
     *
     * @param student
     * @param idLesson
     * @return
     */
    public int getAvgScorePhonemesInLessonOfUser(String student, String idLesson, String ipa,String latestSession){
        int score = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateScorePhoneme(student, idLesson, ipa, latestSession);
        System.out.println("sql generate score phoneme of user " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        Double value = (Double)data[1];
                        score = (int) Math.round(value);
                    }else{
                        score = -1;
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
        return score;
    }

    /**
     *
     * @param classId
     * @param idLesson
     * @return
     */
    public int getAvgScorePhonemesInLessonOfClass(String classId, String idLesson, String ipa){
        int score = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateScorePhonemeOfClass(classId, idLesson, ipa);
        System.out.println("sql generate score phoneme of class : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        Double value = (Double)data[1];
                        score = (int) Math.round(value);
                    }else{
                        score = 0;
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
        return score;
    }


    /**
     *
     * @param student
     * @param lessonId
     * @return
     */
    public String getLatestSessionIdIn3Months(String student, String lessonId){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.sqlGetSessionId3Months(student, lessonId);
        System.out.println("sql get latest session in 3 months : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        String sessionId = (String) StringUtil.isNull(data[1], "null");
                        if(sessionId!=null){
                            return sessionId;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            q.closeAll();
            pm.close();
        }
        return null;
    }

    /**
     *
     * @param student
     * @param lessonId
     * @return
     */
    public ArrayList<String> getWordsInSession(String student, String lessonId, String idSession){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getWordsInSession(student, lessonId, idSession);
        System.out.println("sql get all words in session : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        ArrayList<String> words = new ArrayList<>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[0]!=null){
                        String word = (String) StringUtil.isNull(data[0], "null");
                        if(word!="null"){
                            System.out.println("word practice : " + word);
                            words.add(word);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            q.closeAll();
            pm.close();
        }
        return words;
    }

    /**
     *
     * @param student
     * @param lessonId
     * @return
     */
    public ArrayList<String> getPhonemesInSession(String student, String lessonId, String idSession){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getPhonemesInSession(student, lessonId, idSession);
        System.out.println("sql get all phonemes in session : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        ArrayList<String> phonemes = new ArrayList<>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        String ipa = (String) StringUtil.isNull(data[1], "null");
                        if(ipa!="null"){
                            phonemes.add(ipa);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            q.closeAll();
            pm.close();
        }
        return phonemes;
    }

}
