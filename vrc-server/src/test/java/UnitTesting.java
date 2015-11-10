
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

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by cmg on 08/07/15.
 */
public class UnitTesting {

    public static void main(String[] args) {

        try {
            //PhonemeScoreService sr = new PhonemeScoreService();
          /*  List<PhonemeScoreDB> list = sr.listByUsernameAndVersion("company@c-mg.com", 0);
            for(PhonemeScoreDB m : list){
                System.out.println(m.getPhonemeWord() + "=" + m.getVersion());

            }
            System.out.println(sr.getMaxVersion("company@c-mg.com"));*/
          /*  DictionaryHelper helper = new DictionaryHelper(DictionaryHelper.Type.BEEP);
            List<String> phonemes = helper.getCorrectPhonemes("bottom");
            if (phonemes != null && phonemes.size() > 0) {
                System.out.println("Found phonemes:");
                for (String s : phonemes) {
                    System.out.println(s);
                }
            } else {
                System.out.println("No phonemes found");
            }*/
        /*    OxfordDictionaryWalker walker = new OxfordDictionaryWalker(new File("D:\\word_pronunciation"));
            walker.generateDictionary();*/

            /*WordMappingPhonemesService service = new WordMappingPhonemesService();
            service.updateDatabase();*/
/*
            WordCollectionDAO dao = new WordCollectionDAO();
            List<WordCollection> list = dao.search("ac","desc",10,10);
            if(list!=null && list.size() > 0){
                for(WordCollection word : list){
                    System.out.println(word.getWord());
                }
            }*/

           /* WordCollectionService sc = new WordCollectionService();
           ListWord list =  sc.searchWord("", "asc", 0, 10, 1);
            List<WordCollection> collection = list.getData();
            for(WordCollection w : collection){
                System.out.println(w.getWord() + " - " + w.getMp3Path());
            }*/
          /*  WordMappingPhonemesDAO dao = new WordMappingPhonemesDAO();
            List<WordMappingPhonemes> list = dao.getByWordID("ab9b9e31-49c5-4cfb-9b1d-cfb334fb7f82");
            if(list !=null && list.size() > 0){
                for(WordMappingPhonemes wmp : list){
                    System.out.println(wmp.getIndex() + " - "  + wmp.getPhoneme());
                }
            }
*/

         /*  WordCollectionDAO dao = new WordCollectionDAO();
            List<String> ids = new ArrayList<String>();
            ids.add("000e7c11-3800-4c81-a8cf-ba1538edda08");
            ids.add("009b77ec-f72c-45d0-b9c0-2a447b2249f2");
            ids.add("009c9892-ac02-456d-b216-4153b1414789");
            List<WordCollection> lis = dao.listIn(ids);
            if(lis!=null && lis.size()>0){
                for(WordCollection word : lis)
                System.out.println(word.getWord());
            }*/
          /* WordMappingPhonemesService service = new WordMappingPhonemesService();
            service.updatePhonemeOfWordToDatabase();*/

          /*  CourseMappingDetailDAO dao = new CourseMappingDetailDAO();

            CourseMappingDetail cmd = new CourseMappingDetail();
            cmd.setIdLevel("idLevel");
            cmd.setIdCourse("idCourse");
            cmd.setIdChild("idchild");
            cmd.setIsTest(false);
            cmd.setVersion(1);
            dao.create(cmd);
            List<CourseMappingDetail> list = dao.getBy("idCourse","idLevel",false);
            if(list.size()>0){
                System.out.println(list.get(0).getIdChild());
            }*/

          //  CourseMappingLevelDAO dao = new CourseMappingLevelDAO();
            //dao.getLatestIndex("1f68610d-cbb1-4952-8dd9-d0fa8ad492d6");
           /* IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
            IpaMapArpabet ap = new IpaMapArpabet();
            ap.setIpa("a");
            ap.setArpabet("AE");
            ap.setColor("#820c28");
            ap.setDescription("As in jam.");
            ap.setTip("To pronounce the phoneme, fully open your mouth, flatten your tongue and push the tip against your bottom teeth. Make a quick release of air from the back of the throat.");
            ap.setType("vowel");
            ap.setIndexingType(2);
            ap.setWords("jam ham fan");
            ap.setMp3Url("http://www.oxforddictionaries.com/media/english/uk_pron/h/hon/honk_/honk__gb_1_8.mp3");
            ap.setDateCreated(new Date(System.currentTimeMillis()));
            //dao.create(ap);
            dao.listAll(0,10,"",0,"asc",new Date(),new Date());*/
           /* LevelService ser = new LevelService();
            List<Level> list = ser.listIn("493acae7-1287-4912-b24a-1c1017f3b7ee");
            if(list!=null && list.size() > 0){
                for(Level lv : list){
                    System.out.println(lv.getName());
                }
            }*/

           /* WordCollectionDAO dao =new WordCollectionDAO();
            System.out.println("start list: " + new Date(System.currentTimeMillis()));
            List<WordCollection> list = dao.search("","asc",10,0);
            System.out.println("end list: " + new Date(System.currentTimeMillis()));
            WordCollectionService service = new WordCollectionService();
            System.out.println("start set arpabet: " + new Date(System.currentTimeMillis()));
            list = service.setPhonemeArpabet(list);
            System.out.println("end set arpabet: " + new Date(System.currentTimeMillis()));*/
          /*  WordMappingPhonemesService service = new WordMappingPhonemesService();
            service.updatePhonemeOfWordToDatabase();*/
//            IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
//            String ipa = dao.getByArpabet("UH");
           // System.out.println(ipa);
//            UserLessonHistory userLessonHistory=new UserLessonHistory();
//            UserLessonHistoryDAO userLessonHistoryDAO=new UserLessonHistoryDAO();
//            userLessonHistory.setCountry("Viet Nam");
//            userLessonHistory.setType("T");
//            userLessonHistory.setUsername("nam.bui@c-mg.com");
//            userLessonHistory.setServerTime(1437473382287l);
//            userLessonHistory.setWord("hello");
//            userLessonHistory.setScore(100);
//            userLessonHistoryDAO.put(userLessonHistory);
//            PhonemeLessonScoreDAO phonemeLessonScoreDAO=new PhonemeLessonScoreDAO();
//            PhonemeLessonScore phonemeLessonScore=new PhonemeLessonScore();
//            phonemeLessonScore.setPhoneme("OW");
//            phonemeLessonScore.setIdUserLessonHistory("733d95a8-cdbc-4eb4-aaac-7901fada2f5f");
//            phonemeLessonScore.setTotalScore(100);
//            phonemeLessonScoreDAO.put(phonemeLessonScore);
            WordCollectionService service = new WordCollectionService();
            service.loadWordToDataBase(new File("D:\\pronunciation"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
