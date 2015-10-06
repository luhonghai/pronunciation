import com.cmg.lesson.dao.WordCollectionDAO;
import com.cmg.lesson.data.jdo.WordCollection;
import com.cmg.lesson.services.WordMappingPhonemesService;
import com.cmg.vrc.data.dao.impl.PhonemeScoreDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.dictionary.OxfordDictionaryWalker;
import com.cmg.vrc.service.PhonemeScoreService;
import com.cmg.vrc.service.UserVoiceModelService;
import com.cmg.vrc.service.amt.TranscriptionService;
import com.cmg.vrc.sphinx.DictionaryHelper;

import java.io.File;
import java.util.ArrayList;
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

            WordCollectionDAO dao = new WordCollectionDAO();
           dao.updateWordInformation("000e7c11-3800-4c81-a8cf-ba1538edda08","The state or experience of being alienated","http://www.oxforddictionaries.com/media/english/uk_pron/a/ali/alien/alienation__gb_1_8.mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
