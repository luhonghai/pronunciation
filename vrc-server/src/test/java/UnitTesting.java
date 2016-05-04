
import com.cmg.lesson.dao.course.CourseMappingDetailDAO;
import com.cmg.lesson.dao.course.CourseMappingLevelDAO;
import com.cmg.lesson.dao.history.PhonemeLessonScoreDAO;
import com.cmg.lesson.dao.history.SessionScoreDAO;
import com.cmg.lesson.dao.history.UserLessonHistoryDAO;
import com.cmg.lesson.dao.ipa.IpaMapArpabetDAO;
import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.dao.question.WeightForPhonemeDAO;
import com.cmg.lesson.dao.question.WordOfQuestionDAO;
import com.cmg.lesson.dao.word.WordCollectionDAO;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.history.PhonemeLessonScore;
import com.cmg.lesson.data.jdo.history.SessionScore;
import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.services.lessons.LessonMappingQuestionService;
import com.cmg.lesson.services.level.LevelService;
import com.cmg.lesson.services.word.WordCollectionService;
import com.cmg.lesson.services.word.WordMappingPhonemesService;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.lessons.LDAO;
import com.cmg.merchant.dao.level.LVMODAO;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.dao.report.ReportLessonDAO;
import com.cmg.merchant.dao.report.ReportPhoneDao;
import com.cmg.merchant.dao.teacher.TCHDAO;
import com.cmg.merchant.data.dto.CourseDTO;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
import com.cmg.merchant.data.jdo.TeacherCourseHistory;
import com.cmg.merchant.services.CMTSERVICES;
import com.cmg.merchant.services.LessonServices;
import com.cmg.merchant.services.QuestionServices;
import com.cmg.merchant.services.Report.ReportLessonService;
import com.cmg.merchant.services.Sync.CourseSyncService;
import com.cmg.merchant.util.DateUtil;
import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import edu.cmu.sphinx.linguist.dictionary.Word;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cmg on 08/07/15.
 */
public class UnitTesting {
    private static Map<String, Map<String, List<String>>> BEEP_CACHE = new ConcurrentHashMap<String, Map<String, List<String>>>();
    private static String s3Path = "sphinx-data/dict/cmg-beep-1.0";
    public static void main(String[] args) {
        try {
          /* WordCollectionService ser = new WordCollectionService();
            ser.loadWordToDataBase(new File("D:\\check.json"));*/
            /*WordMappingPhonemesService se = new WordMappingPhonemesService();
            se.updatePhonemeOfWordToDatabase();*/
           /* File fWords = new File("D:\\", "brit-a-z.txt");
            if (!fWords.exists()) {
                AWSHelper awsHelper = new AWSHelper();
                awsHelper.download("sphinx-data/dict/brit-a-z.txt", fWords);
            }*/
            /*WeightForPhonemeDAO weightDao = new WeightForPhonemeDAO();
            List<WeightForPhoneme> weight = weightDao.listBy("079eca3b-d4d0-4259-8302-824930c53305","4177e406-ed44-43df-8578-9e0a2585a153");
            for(WeightForPhoneme w : weight){
                if(w.getPhoneme().trim().equalsIgnoreCase("AY"))
                System.out.println(w.getPhoneme());
            }*/
          /* String s =  StringEscapeUtils.escapeJava("country Vietnam - avoid confusing /a/ with /ʌ/ - words with /ʌ/ : Q1");
            System.out.println(s);*/
           /*QuestionDAO dao = new QuestionDAO();
            List<Question> list = dao.searchName(null, "country Vietnam - avoid confusing /a/ with /ʌ/ - words with /ʌ/ : Q1");*/

            /*LVMODAO dao = new LVMODAO();
            System.out.println(dao.getMaxIndex("66b3510d-8964-47a0-8c33-72dc14f8dded"));*/
          /*  CMTSERVICES services = new CMTSERVICES();
            ArrayList<CourseDTO> list = services.getCoursesForMainPage("02354aca-32e3-430f-bf88-b4dd472085be","790b2928-2617-41c3-9a10-f3f56f03c874");
            if(list!=null){
                for(CourseDTO dto  : list){
                    System.out.println(dto.getNameCourse()+"-"+dto.getCompanyName() + "-" + dto.getState());
                }
            }*/
           /* SessionScore ss = new SessionScore();
            ss.setId("21");
            ss.setIdLessonCollection("idlesson");
            ss.setItemID("12");
            ss.setIdLevel("idLevel");
            ss.setIdUserLessonHistory("idUserLessonHistory");
            ss.setIdQuestion("idQuestion");
            SessionScoreDAO dao = new SessionScoreDAO();
            dao.put(ss);*/

        /*    UserLessonHistory uh = new UserLessonHistory();
            uh.setId("idUserLessonHistory");
            uh.setIdLevel("idLevel");
            uh.setIdQuestion("idQuestion");
            uh.setSessionID("sessionid");
            uh.setIdWord("idword");
            uh.setWord("word");
            uh.setIdItem("idItem");
            UserLessonHistoryDAO dao = new UserLessonHistoryDAO();
            dao.put(uh);*/

         /*   PhonemeLessonScore pls = new PhonemeLessonScore();
            pls.setId("id");
            pls.setIdUserLessonHistory("id");
            PhonemeLessonScoreDAO dao = new PhonemeLessonScoreDAO();
            dao.put(pls);*/

          /*  SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            long bi = 1456767694161l;
            Date d = new Date(bi);
            String s = format.format(d);
            System.out.println(s);*/
         /*   ReportPhoneDao dao = new ReportPhoneDao();
            dao.getScorePhonemeByStudent("nambui","AH","2016-02-28","2016-03-01");*/

           /* DateUtil util = new DateUtil();
            String d = util.convertDate("28/02/2016") ;
            System.out.println(d);*/
            //dao.listPhonemes();
          /*  CMTSERVICES s = new CMTSERVICES();
            ArrayList<CourseDTO> list = s.getCoursesForMainPage("","");
            Gson gson  = new Gson();
            String json = gson.toJson(list);
            System.out.println(json);*//*
          ReportLessonDAO dao = new ReportLessonDAO();
            dao.getClassByTeacher("");*/
       /*     StudentMappingTeacherDAO dao = new StudentMappingTeacherDAO();
            StudentMappingTeacher smt = new StudentMappingTeacher();
            smt.setId("temp");
            smt.setFirstTeacherName("temp");
            smt.setIsDeleted(true);
            smt.setIsView(true);
            smt.setLicence(true);
            smt.setMappingBy("temp");
            smt.setStudentName("temp");
            dao.put(smt);
            ClassMappingTeacher cmt = new ClassMappingTeacher();
            cmt.setId("temp");
            ClassMappingTeacherDAO dao1 = new ClassMappingTeacherDAO();
            dao1.put(cmt);*/
           /* CourseMappingClass cmc = new CourseMappingClass();
            cmc.setId("temp");
            CourseMappingClassDAO cmcdao = new CourseMappingClassDAO();
            cmcdao.put(cmc);*/
         /*   TeacherCourseHistory tch = new TeacherCourseHistory();
            tch.setId("abc");
            tch.setIdCourse("idcourse");
            tch.setVersion(1);
            tch.setPathAws("idcourse-v1.zip");
            TCHDAO dao = new TCHDAO();
            dao.put(tch);*/
           /* CourseMappingTeacher cmt = new CourseMappingTeacher();
            cmt.setId("test");
            CMTDAO dao1 = new CMTDAO();
            dao1.put(cmt);*/
         /*   Double value = 19.5;
            int i = (int) Math.round(value);
            System.out.println(i);*/

            /*ClassJDO c = new ClassJDO();
            c.setId("test");
            ClassDAO dao = new ClassDAO();
            dao.put(c);*/

           /* CourseSyncService service = new CourseSyncService();
            ArrayList<TeacherCourseHistory> list = service.listCourseByUser("pablo.dropbox01@gmail.com");
            for(TeacherCourseHistory tch : list){
                System.out.println(tch.getIdCourse() + "-" + tch.getName() + "-" + tch.getUrlDownload());
            }*/
           /* LessonServices services = new LessonServices();
            String c = services.updateLesson("test","6f26a3ed-1d05-46bb-84f1-cf36c3d1c0ce","test"," test description"," test type"," test detail");
            System.out.println(c);*/
          /*  Double value = 19.5;
            int i = (int) Math.round(value);
            System.out.println(i);*/
           // ReportLessonDAO dao = new ReportLessonDAO();
            //dao.getStudentAvgScoreLesson("pablo.dropbox03@gmail.com","5a0ecb3b-3a88-4264-b80e-9cd2f156e612","3EAC1A83-3653-42BF-9CE4-AB42C2DBEDD6");
            //ReportLessonService services = new ReportLessonService();
            //int i = services.getAvgScoreOfClass("004adfdc-64d0-413c-8367-2914828b5280","5a0ecb3b-3a88-4264-b80e-9cd2f156e612");
            // int i = (int) Math.round((0+17)/2);
            //System.out.println(i);
          /*  int i = (int) Math.round(0/0);*/
            /*ReportLessonDAO dao = new ReportLessonDAO();
            String latestSession = dao.getLatestSessionIdIn3Months("pablo.dropbox02@gmail.com","699fabf3-6a6f-4698-9a46-7ff286cd2cb0");
            System.out.println(latestSession);
            boolean test =  dao.checkUserCompletedLesson("pablo.dropbox02@gmail.com","699fabf3-6a6f-4698-9a46-7ff286cd2cb0", latestSession);
            System.out.println(test);*/
           /* ArrayList<String> practice = new ArrayList<>();
            ArrayList<String> total = new ArrayList<>();
            practice.add("e");
            total.add("e");
            for(int i = 0 ; i < total.size();i++){
                String ipa = total.get(i);
                if(practice.contains(ipa)){
                    System.out.println("contain");
                }
            }*/
            TCHDAO dao = new TCHDAO();
            ArrayList<TeacherCourseHistory> listTmp = dao.getListCourseByStudent("xuan.bui@c-mg.com");
            for(TeacherCourseHistory tch : listTmp){
                System.out.println(tch.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
