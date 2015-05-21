import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.util.StringUtil;

/**
 * Created by luhonghai on 5/8/15.
 */
public class TestDAO {
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("luhonghai@gmail.com");
        user.setPassword(StringUtil.md5("hurricane"));
        user.setActivated(false);
        user.setActivationCode("ABC123");
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.put(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println(userDAO.getUserByEmail("luhonghai@gmail.com").getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println(userDAO.getUserByEmailPassword("luhonghai@gmail.com", StringUtil.md5("hurricane")).getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println(userDAO.getUserByValidationCode("ABC123").getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Count: " + userDAO.getCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
