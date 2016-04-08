
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
import com.cmg.merchant.dao.level.LVMODAO;
import com.cmg.merchant.dao.report.ReportLessonDAO;
import com.cmg.merchant.data.dto.CourseDTO;
import com.cmg.merchant.services.CMTSERVICES;
import com.cmg.merchant.services.QuestionServices;
import com.cmg.vrc.data.dao.impl.AdminDAO;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.Reports;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import edu.cmu.sphinx.linguist.dictionary.Word;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
            ReportLessonDAO dao = new ReportLessonDAO();
            int i = dao.getAvgScoreWordInLessonOfUser("nambui","1148fd96-f817-4f83-b860-6cd43e8e6a75","image");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
