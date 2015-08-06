import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.AppDetail;
import com.cmg.vrc.data.jdo.Usage;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;

import java.util.Date;
import org.apache.commons.lang.ArrayUtils;
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
        UserDAO userDAO=new UserDAO();
        UsageDAO usageDAO=new UsageDAO();
        UserDeviceDAO userDeviceDAO=new UserDeviceDAO();
        UserDevice userDevice=new UserDevice();
        AppDetailDAO appDetailDAO=new AppDetailDAO();
        AppDetail appDetail=new AppDetail();
        UserVoiceModelDAO userVoiceModelDAO=new UserVoiceModelDAO();
        AdminDAO adminDAO=new AdminDAO();
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();


        try {
            Object[] a = { "aa", "bb", "aa", "bb", "dd", "cc", "dd", "cc", "cc", "dd"};

            for (int i = 0; i < a.length; i++) {
                for (int j = i + 1; j < a.length; j++) {
                    if (a[i].equals(a[j])) {
                        a=ArrayUtils.remove(a, j);
                    }
                }
            }
            for(int i=0;i<a.length;i++){
                System.out.println(a[i]);
            }



          //lice.listAll(0,10,"ac",0,"acs","","","",new Date(10/12/2014),new Date(9/8/2015));
            //fe.listAll(0,10,"df",0,"asc","ds","fdsf","ds","fdsf",new Date(10/12/2014),new Date(9/8/2015));
           // userDAO.listAll(0,10,"sa",0,"asc","na","fdfs","nam","vietnam","dsa",new Date(10/12/2014),new Date(9/8/2015));
            //usageDAO.listAll(0,10,"na",0,"asc","nam");
           // userDeviceDAO.listAll(0,10,"fds",0,"fdsf","dgfd","fds","gfd","fds","fdsds","fdsg",new Date(10/12/2014),new Date(9/8/2015));
           // appDetailDAO.listAll();
            //userVoiceModelDAO.listAll(0,10,"ds",0,"asc","nam","hello","sd");
           // userDAO.getCountSearch("dsad","dsa","fgd","fdsf","tre","sdf",new Date(10/12/2014),new Date(9/8/2015));
         // fe.getCountSearch("dfss","fdsf","fsd","fds","fds",new Date(10/12/2014),new Date(9/8/2015));
      // userVoiceModelDAO.getCountSearch("fdsf","fdsf","fdsf","sada");
           // lice.getCountSearch("dsad","sdsad","fdsfs","fds",new Date(10/12/2014),new Date(9/8/2015));
           // usageDAO.getCountSearch("dsd","dsad");
          //  userVoiceModelDAO.getListScore("fdsf","fdsf","fdsf","sada");
           // adminDAO.getCountSearch("dfs","fds","fdsf");
           // userVoiceModelDAO.getCountSearch("dfs","fsdgfd","dfs","fsg");
//            long startTime = System.currentTimeMillis();
//
//            //userVoiceModelDAO.getCountSearch("dfs","fsdgfd","dfs","fsg");
//            userVoiceModelDAO.getCount();
//            long endTime   = System.currentTimeMillis();
//            System.out.print("#1: " + ( endTime-startTime));
//            userVoiceModelDAO.getCount();
//            System.out.print("#2: " + ( System.currentTimeMillis() - endTime));
            //userVoiceModelDAO.listAllScore();
//            System.out.println(usageDAO.getCount());
//           // System.out.println(usageDAO.listAll().size());
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            System.out.println(gson.toJson(usageDAO.listAll()));
           // usageDAO.getCountSearch("","anh.nguyen@c-mg.com");
            //lice.listAll(0,10,"ac",0,"acs","","","",new Date(10/12/2014),new Date(9/8/2015));
           // transcriptionDAO.listAll(0,10,"dfs",0,"dsfs","gfdgd",new Date(10/12/2014),new Date(9/8/2015),new Date(10/12/2014),new Date(9/8/2015));
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Done");

    }
}
