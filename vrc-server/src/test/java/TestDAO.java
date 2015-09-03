import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.service.RecorderSentenceService;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

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
        RecorderDAO recorderDAO=new RecorderDAO();
        ClientCodeDAO clientCodeDAO=new ClientCodeDAO();
        LicenseCodeCompanyDAO licenseCodeCompanyDAO=new LicenseCodeCompanyDAO();

//        ClientCode clientCode=new ClientCode();
//        clientCode.setCompanyName("CMG");
//        clientCode.setContactName("nambui");
//        clientCode.setEmail("nam.bui@c-mg.com");



        try {

            //clientCodeDAO.put(clientCode);

//            Object[] a = { "aa", "bb", "aa", "bb", "dd", "cc", "dd", "dd", "cc", "cc", "dd"};
//
//            for (int i = 0; i < a.length; i++) {
//                for (int j = i + 1; j < a.length; j++) {
//                    if (a[i].equals(a[j])) {
//                        a=ArrayUtils.remove(a, j);
//                        j=j-1;
//                    }
//                }
//            }
//            for(int i=0;i<a.length;i++){
//                System.out.println(a[i]);
//            }



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
            //clientCodeDAO.listAll(0,10,"dsaa",0,"fsd","fds","fdgd","fds");
           // clientCodeDAO.getCountSearch("afdsf","gfdgd","fdsfs","sada");
            //transcriptionDAO.getLatestVersion();
//            RecorderSentenceService rcs = new RecorderSentenceService();
//           List<RecordedSentence> list = rcs.getListByVersionAndUsername(6, "company@c-mg.com");
//            System.out.println(list.size());
            //recorderDAO.get();
           // transcriptionDAO.getListByVersion(0);
            //recorderDAO.getCountSearch("fdsd", "fdsfs", new Date(10/12/2014),new Date(9/8/2015),3);
           // recorderDAO.listAll();
            lice.listAllByCompany(1,2,"","","",null,null,null,null,"CMG2");
           //lice.getCountSearchByCompany("dsada","dsad","dsad","fdsfs",new Date(10/12/2014),new Date(9/8/2015),"dsada");
            //licenseCodeCompanyDAO.listAll();
           // clientCodeDAO.getCount();
            //lice.listAll(0,1,"com",1,"asb","nam","as",new Date(10/12/2014),new Date(9/8/2015),new Date(10/12/2014),new Date(9/8/2015));
            //lice.getCountSearch("ads","fdf","dsa",new Date(10/12/2014),new Date(9/8/2015),new Date(10/12/2014),new Date(9/8/2015));
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Done");

    }
}
