
import com.cmg.lesson.dao.course.CourseMappingDetailDAO;
import com.cmg.lesson.dao.course.CourseMappingLevelDAO;
import com.cmg.lesson.dao.history.PhonemeLessonScoreDAO;
import com.cmg.lesson.dao.history.UserLessonHistoryDAO;
import com.cmg.lesson.dao.ipa.IpaMapArpabetDAO;
import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.dao.question.WordOfQuestionDAO;
import com.cmg.lesson.dao.word.WordCollectionDAO;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.history.PhonemeLessonScore;
import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.services.lessons.LessonMappingQuestionService;
import com.cmg.lesson.services.level.LevelService;
import com.cmg.lesson.services.word.WordCollectionService;
import com.cmg.lesson.services.word.WordMappingPhonemesService;
import com.cmg.vrc.data.dao.impl.AdminDAO;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import edu.cmu.sphinx.linguist.dictionary.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
            File fWords = new File("D:\\", "brit-a-z.txt");
            if (!fWords.exists()) {
                AWSHelper awsHelper = new AWSHelper();
                awsHelper.download("sphinx-data/dict/brit-a-z.txt", fWords);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
