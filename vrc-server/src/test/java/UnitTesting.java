import com.cmg.vrc.data.dao.impl.PhonemeScoreDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.service.amt.TranscriptionService;

import java.util.List;

/**
 * Created by cmg on 08/07/15.
 */
public class UnitTesting {

    public static void main(String[] args) {

        try {
            PhonemeScoreDB score = new PhonemeScoreDB();
            score.setUsername("compay@c-mg.com");
            score.setVersion(1);
            score.setPhonemeWord("H");
            score.setTotalScore(7);
            PhonemeScoreDAO dao = new PhonemeScoreDAO();
            dao.create(score);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
