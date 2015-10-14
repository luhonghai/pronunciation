
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.dao.question.WordOfQuestionDAO;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.services.word.WordCollectionService;

import java.io.File;

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

           /* WordOfQuestionDAO dao = new WordOfQuestionDAO();
            WordOfQuestion q = new WordOfQuestion();
            q.setIdQuestion("b846712f-2308-4f20-9d27-d6ee1916e28f");
            q.setIdWordCollection("1c00cb1c-896f-4e09-82aa-7cfadf69ef03");
            q.setVersion(1);
            q.setIsDeleted(false);
            dao.create(q);*/

            WordCollectionService service = new WordCollectionService();
            service.loadWordToDataBase(new File("D:\\word_pronunciation"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
