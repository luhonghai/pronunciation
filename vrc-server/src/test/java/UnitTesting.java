
import com.cmg.lesson.dao.QuestionDAO;
import com.cmg.lesson.dao.WordCollectionDAO;
import com.cmg.lesson.dao.WordMappingPhonemesDAO;
import com.cmg.lesson.dao.WordOfQuestionDAO;
import com.cmg.lesson.data.dto.ListWord;
import com.cmg.lesson.data.jdo.WordCollection;
import com.cmg.lesson.data.jdo.WordMappingPhonemes;
import com.cmg.lesson.data.jdo.WordOfQuestion;
import com.cmg.lesson.services.WordCollectionService;

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

            WordOfQuestionDAO dao = new WordOfQuestionDAO();
            WordOfQuestion q = new WordOfQuestion();
            q.setVersion(1);
            q.setIdQuestion("abc");
            q.setIdWordCollection("def");
            q.setIsDeleted(false);
            dao.create(q);
            //dao.updateDeleted("abc","def");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
