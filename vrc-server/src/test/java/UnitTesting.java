import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.service.amt.TranscriptionService;

/**
 * Created by cmg on 08/07/15.
 */
public class UnitTesting {

    public static void main(String[] args) {
        UserVoiceModelDAO dao = new UserVoiceModelDAO();
        try {
            int i = dao.getMaxVersion("company@c-mg.com");
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
