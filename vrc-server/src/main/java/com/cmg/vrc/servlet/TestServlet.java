package com.cmg.vrc.servlet;

import com.cmg.lesson.dao.word.WordCollectionDAO;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.services.question.QuestionService;

import java.util.List;


public class TestServlet {
    public static void main(String[] args) {
//        LicenseCodeDAO lis=new LicenseCodeDAO();
//        FeedbackDAO feedbackDAO = new FeedbackDAO();
//        UsageDAO usageDAO=new UsageDAO();
//        UserDAO userDAO=new UserDAO();
//        RecorderDAO recorderDAO=new RecorderDAO();
//        ClientCodeDAO clientCodeDAO=new ClientCodeDAO();
//        LicenseCodeCompanyDAO licenseCodeCompanyDAO=new LicenseCodeCompanyDAO();
////        try {
////            feedbackDAO.deleteAll();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//        Feedback feedback = new Feedback();
//        feedback.setAccount("nambui");
//        feedback.setImei("nam123");
//        feedback.setAppVersion("2.5");
//        try {
//            feedbackDAO.put(feedback);
//        }catch (Exception e){
//            e.getStackTrace();
//        }
//
////        LicenseCode license=new LicenseCode();
////        license.setCode("dsadfd");
////        try {
////            lis.put(license);
////        }catch (Exception e){
////            e.getStackTrace();
////        }
////        Usage usage=new Usage();
////        usage.setAppVersion("323");
////        usage.setLatitude(543.765);
////        usage.setLongitude(423.65);
////
////        usage.setImei("nam123");
////        usage.setTime(new Date(2-10-2014));
////        usage.setUsername("nam bui");
////          User user=new User();
////        RecordedSentence recordedSentence=new RecordedSentence();
////        recordedSentence.setVersion(0);
////        recordedSentence.setSentenceId("NAM.BUI123");
////        recordedSentence.setAccount("nam.bui@c-mg.com");
////        recordedSentence.setIsDeleted(1);
//          LicenseCodeCompany licenseCodeCompany=new LicenseCodeCompany();
//          licenseCodeCompany.setCompany("cmg");
//         licenseCodeCompany.setCode("kdjs54");
//        licenseCodeCompany.setIsDeleted(false);
//        ClientCode clientCode=new ClientCode();
//        clientCode.setEmail("nam.bui@c-mg.com");
//        clientCode.setIsDeleted(false);
//        clientCode.setCompanyName("cmg");
//        LicenseCode licenseCode=new LicenseCode();
//        licenseCode.setCode("sdfsd2");
//        licenseCode.setIsDeleted(false);
//        licenseCode.setImei("fdsfsdf");
//
//
//        try {
//           // licenseCodeCompanyDAO.put(licenseCodeCompany);
//          //  lis.put(licenseCode);
//            //clientCodeDAO.put(clientCode);
//
////            recorderDAO.put(recordedSentence);
//////            usageDAO.put(usage);
////            userDAO.put(user);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
////        UserDeviceDAO userDeviceDAO=new UserDeviceDAO();
////        UserDevice userDevice=new UserDevice();
////        userDevice.setImei("nam123");
////        userDevice.setDeviceName("cmg");
////        userDevice.setOsApiLevel("12ds");
////        userDevice.setOsVersion("1.53");
////
////
////        try {
////            userDeviceDAO.put(userDevice);
////
////      }catch (Exception e) {
////            e.printStackTrace();
////        }
//
////        AppDetailDAO appDetailDAO=new AppDetailDAO();
////        AppDetail appDetail=new AppDetail();
////        appDetail.setNoAccessMessage("message");
////        appDetail.setRegistration(true);
////        try {
////            appDetailDAO.put(appDetail);
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////        UserVoiceModelDAO userVoiceModelDAO=new UserVoiceModelDAO();
////        UserVoiceModel userVoiceModel=new UserVoiceModel();
////        userVoiceModel.setUsername("nam123");
////        userVoiceModel.setCountry("VN");
////        try {
////            userVoiceModelDAO.put(userVoiceModel);
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////        AdminDAO adminDAO=new AdminDAO();
////        Admin admin=new Admin();
////        admin.setPassword(StringUtil.md5("1234"));
////        admin.setUserName("nam@gmail.com");
////        admin.setRole(1);
////        admin.setLastName("");
////        admin.setFirstName("");
////        try {
////            adminDAO.put(admin);
////        }catch (Exception e){
////            e.printStackTrace();;
////        }


//        QuestionService questionSER=new QuestionService();
//        try {
//            System.out.println(questionSER.addQuestionToDB("bai 3").getMessage());
//
//        }catch (Exception e){
//            e.printStackTrace();;
//        }
            WordCollection wordCollection=new WordCollection();
            WordCollectionDAO wordCollectionDAO=new WordCollectionDAO();
            try{
                List<WordCollection> wordCollectionList=wordCollectionDAO.listAll();
                for(WordCollection wordCollection1:wordCollectionList){
                    String definition = wordCollection1.getDefinition();
                    if(definition.endsWith(":")) {
                        definition = definition.substring(0, definition.length() - 1) + ".";
                    }
                    if(!definition.endsWith(".")){
                        definition = definition + ".";
                    }
                    System.out.println("New definition: " + definition + ". Old definition: " + wordCollection1.getDefinition());
                    wordCollection1.setDefinition(definition);
                    wordCollectionDAO.put(wordCollection1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

//
//
//
//
//
////        try {
////            Feedback abc=feedbackDAO.getById("b3d4069a-4cb1-4595-be87-fe6020069cbc");
////            System.out.print(abc.getAccount());
////        }catch (Exception e){
////            e.printStackTrace();
////        }
//
//
////        try {
////            feedbackDAO.put(feedback);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        try {
////            List<Feedback> list = feedbackDAO.listAll();
////            if (list != null && list.size() > 0) {
////                for (Feedback f : list) {
////                    System.out.println( "Feedback account: " + f.getAccount());
////                }
////
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }
//        String dateStr="03/09/2015";
//        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
//        Date date = null;
//        try {
//            date = format.parse(dateStr);
//            System.out.println( "Feedback account: " + date.toString());
//        }catch(Exception e){
//
//        }
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//        System.out.println( "Feedback account: " + df.format(date).toString());
    }
}
