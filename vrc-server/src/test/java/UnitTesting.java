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

            WordMappingPhonemesService service = new WordMappingPhonemesService();
            service.updateDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
