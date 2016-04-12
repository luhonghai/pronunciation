package com.cmg.merchant.dao.report;

import com.cmg.lesson.data.dto.objectives.IndexLesson;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.common.SQL;
import com.cmg.merchant.common.SqlReport;
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
    public boolean checkUserCompletedLesson(String student, String idLesson){
        boolean completed = true;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCheckUserCompletedLesson(student, idLesson);
        System.out.println("sql check user completed lesson : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for(Object obj : tmp){
                    String s = (String) StringUtil.isNull(obj,"null");
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
    public Reports getStudentAvgScoreLesson(String student,String idLesson){
        Reports report = new Reports();;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateUserScoreLesson(student, idLesson);
        System.out.println("sql check user completed lesson" + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[0]!=null){
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
        return report;
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
    public int getAvgScoreWordInLessonOfUser(String student, String idLesson, String word){
        int score = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateScoreWord(student, idLesson, word);
        System.out.println("sql generate score word of user " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        Double value = (Double)data[1];
                        score = value.intValue();
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
     * @param classId
     * @param idLesson
     * @param word
     * @return
     */
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
                        score = value.intValue();
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
                    String s = (String) StringUtil.isNull(obj,"null");
                    if(s!="null"){
                        listPhonemes.add(s);
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
        return listPhonemes;
    }

    /**
     *
     * @param student
     * @param idLesson
     * @return
     */
    public int getAvgScorePhonemesInLessonOfUser(String student, String idLesson, String ipa){
        int score = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateScorePhoneme(student, idLesson, ipa);
        System.out.println("sql generate score phoneme of user " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        Double value = (Double)data[1];
                        score = value.intValue();
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
     * @param classId
     * @param idLesson
     * @return
     */
    public int getAvgScorePhonemesInLessonOfClass(String classId, String idLesson, String ipa){
        int score = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql = new SqlReport();
        String query=sql.getSqlCalculateScorePhonemeOfClass(classId, idLesson,ipa);
        System.out.println("sql generate score phoneme of class : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size()>0){
                for (Object object : tmp) {
                    Object[] data = (Object[]) object;
                    if(data[1]!=null){
                        Double value = (Double)data[1];
                        score = value.intValue();
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
}
