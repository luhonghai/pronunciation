import com.cmg.vrc.data.dao.impl.PhonemeScoreDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.service.PhonemeScoreService;
import com.cmg.vrc.service.UserVoiceModelService;
import com.cmg.vrc.service.amt.TranscriptionService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmg on 08/07/15.
 */
public class UnitTesting {

    public static void main(String[] args) {

        try {
            PhonemeScoreService sr = new PhonemeScoreService();
            List<PhonemeScoreDB> list = sr.listByUsernameAndVersion("company@c-mg.com", 0);
            for(PhonemeScoreDB m : list){
                System.out.println(m.getPhonemeWord() + "=" + m.getVersion());

            }
            System.out.println(sr.getMaxVersion("company@c-mg.com"));
            /*PhonemeScoreService sr = new PhonemeScoreService();
            PhonemeScoreDB s1 = new PhonemeScoreDB();
            s1.setTotalScore(8);
            s1.setPhonemeWord("H");
            s1.setUsername("company@c-mg.com");
            s1.setVersion(1);
            PhonemeScoreDB s2 = new PhonemeScoreDB();
            s2.setTotalScore(8);
            s2.setPhonemeWord("H");
            s2.setUsername("company@c-mg.com");
            s2.setVersion(1);
            List<PhonemeScoreDB> list = new ArrayList<PhonemeScoreDB>();
            list.add(s1);
            list.add(s2);
            PhonemeScoreDAO dao = new PhonemeScoreDAO();
            dao.create(list);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
