import com.cmg.vrc.data.dao.impl.FeedbackDAO;
import com.cmg.vrc.data.dao.impl.LicenseCodeDAO;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.util.StringUtil;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by luhonghai on 5/8/15.
 */
public class TestDAO {
    public static void main(String[] args) {
//        User user = new User();
//        user.setUsername("luhonghai@gmail.com");
//        user.setPassword(StringUtil.md5("hurricane"));
//        user.setActivated(false);
//        user.setActivationCode("ABC123");
//        UserDAO userDAO = new UserDAO();
//        try {
//            userDAO.put(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(userDAO.getUserByEmail("luhonghai@gmail.com").getUsername());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(userDAO.getUserByEmailPassword("luhonghai@gmail.com", StringUtil.md5("hurricane")).getUsername());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(userDAO.getUserByValidationCode("ABC123").getUsername());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        LicenseCodeDAO lice=new LicenseCodeDAO();
        FeedbackDAO fe=new FeedbackDAO();
        try {
          //lice.listAll(0,10,"ac",0,"acs","","","",new Date(10/12/2014),new Date(9/8/2015));
            fe.listAll(0,10,"df",0,"asc","ds","fdsf","ds","fdsf",new Date(10/12/2014),new Date(9/8/2015));

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
}
