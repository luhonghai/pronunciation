package com.cmg.vrc.servlet;

import com.cmg.vrc.data.dao.impl.*;
import com.cmg.vrc.data.jdo.*;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class TestServlet  {
    public static void main(String[] args) {
        LicenseCodeDAO lis=new LicenseCodeDAO();
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        UsageDAO usageDAO=new UsageDAO();
//        try {
//            feedbackDAO.deleteAll();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Feedback feedback = new Feedback();
//        feedback.setAccount("nambui");
//        feedback.setImei("nsm");
//        feedback.setAppVersion("2.5");
//
//        LicenseCode license=new LicenseCode();
//        license.setCode("dsadfd");
//        try {
//            lis.put(license);
//        }catch (Exception e){
//            e.getStackTrace();
//        }
//        Usage usage=new Usage();
//        usage.setAppVersion("323");
//        usage.setLatitude(543.765);
//        usage.setLongitude(423.65);
//
//        usage.setEmei("fdsfg");
//        usage.setTime(new Date(2-10-2014));
//        usage.setUsername("nam bui");
//        try {
//            usageDAO.put(usage);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        UserDeviceDAO userDeviceDAO=new UserDeviceDAO();
//        UserDevice userDevice=new UserDevice();
//        userDevice.setEmei("nam123");
//        userDevice.setDeviceName("cmg");
//        userDevice.setOsApiLevel("12ds");
//        userDevice.setOsVersion("1.53");
//
//
//        try {
//            userDeviceDAO.put(userDevice);
//
//      }catch (Exception e) {
//            e.printStackTrace();
//        }

//        AppDetailDAO appDetailDAO=new AppDetailDAO();
//        AppDetail appDetail=new AppDetail();
//        appDetail.setNoAccessMessage("message");
//        appDetail.setRegistration(true);
//        try {
//            appDetailDAO.put(appDetail);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        UserVoiceModelDAO userVoiceModelDAO=new UserVoiceModelDAO();
//        UserVoiceModel userVoiceModel=new UserVoiceModel();
//        userVoiceModel.setUsername("nam123");
//        userVoiceModel.setCountry("VN");
//        try {
//            userVoiceModelDAO.put(userVoiceModel);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        AdminDAO adminDAO=new AdminDAO();
        Admin admin=new Admin();
        admin.setPassword(StringUtil.md5("1234"));
        admin.setUserName("nam@gmail.com");
        admin.setRole(1);
        admin.setLastName("");
        admin.setFirstName("");
        try {
            adminDAO.put(admin);
        }catch (Exception e){
            e.printStackTrace();;
        }





//        try {
//            Feedback abc=feedbackDAO.getById("b3d4069a-4cb1-4595-be87-fe6020069cbc");
//            System.out.print(abc.getAccount());
//        }catch (Exception e){
//            e.printStackTrace();
//        }


//        try {
//            feedbackDAO.put(feedback);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            List<Feedback> list = feedbackDAO.listAll();
//            if (list != null && list.size() > 0) {
//                for (Feedback f : list) {
//                    System.out.println( "Feedback account: " + f.getAccount());
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
