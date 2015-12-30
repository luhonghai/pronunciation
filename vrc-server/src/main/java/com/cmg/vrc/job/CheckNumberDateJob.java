package com.cmg.vrc.job;

import com.cmg.vrc.data.dao.impl.AppDetailDAO;
import com.cmg.vrc.data.dao.impl.NumberDateDAO;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.AppDetail;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.service.MailService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.*;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Created by CMGT400 on 11/13/2015.
 */
public class CheckNumberDateJob {
    private static final Logger logger = Logger.getLogger(CheckNumberDateJob.class.getName());

    public static void startJob() throws SchedulerException {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            //Get schedular
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail jobStart = JobBuilder
                    .newJob(Check.class)
                    .withIdentity("StartEnvironmentJob", "BeanstalkGroup")
                    .build();

            //Associate Trigger to the Job
            CronTrigger triggerStart = TriggerBuilder
                    .newTrigger()
                    .withIdentity("StartEnvironmentJobTrigger", "BeanstalkGroup")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0,1))
                    .build();

            //Pass JobDetail and trigger dependencies to schedular
            scheduler.scheduleJob(jobStart, triggerStart);


            //Start schedular
            scheduler.start();

    }

    public static class Check implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            logger.info("Start Check job");
            AppDetailDAO appDetailDAO=new AppDetailDAO();

            UserDAO userDAO=new UserDAO();
            NumberDateDAO numberDateDAO=new NumberDateDAO();
            List<User> users=null;
            MailService mailService=new MailService();
            List<String> recipients = new ArrayList<String>();
            try {
                List<AppDetail> appDetails=appDetailDAO.listAll();
                users=userDAO.users();
                Date date=new Date(System.currentTimeMillis());
                int number=numberDateDAO.numberDate().getNumberDate();
                java.util.Calendar cal1 = java.util.Calendar.getInstance();
                cal1.setTime(date);
                cal1.set(Calendar.MILLISECOND, 0);
                cal1.set(Calendar.SECOND,0);
                cal1.set(Calendar.MINUTE, 0);
                cal1.set(Calendar.HOUR, 0);
                java.util.Calendar cal2 = java.util.Calendar.getInstance();
                if(users!=null){
                    for(User user:users){
                        cal2.setTime(user.getCreatedDate());
                        cal2.set(Calendar.MILLISECOND, 0);
                        cal2.set(Calendar.SECOND,0);
                        cal2.set(Calendar.MINUTE,0);
                        cal2.set(Calendar.HOUR, 0);
                        cal2.add(java.util.Calendar.DATE, number);
                        if(cal2.equals(cal1)){
                            logger.info("send mail to expired user :"+user.getUsername() + " created date: " + user.getCreatedDate());
                            recipients.add(user.getUsername());
                        }
                    }
                }
                if (recipients.size() > 0) {
                    logger.info("Send expired email notification to " + recipients.size() + " users");
                    String[] recipients1 = recipients.toArray(new String[recipients.size()]);
                    mailService.sendEmail(recipients1, appDetails.get(0).getSubject(), appDetails.get(0).getMessage());
                } else {
                    logger.info("No expired user found. Skip email sender.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("Complete StopEnvironmentJob job");
        }
    }
    public static void main(String[] args){
//        AppDetailDAO appDetailDAO=new AppDetailDAO();
//
//        UserDAO userDAO=new UserDAO();
//        NumberDateDAO numberDateDAO=new NumberDateDAO();
//        List<User> users=null;
//        MailService mailService=new MailService();
//        List<String> recipients = new ArrayList<String>();
//        try {
//            List<AppDetail> appDetails=appDetailDAO.listAll();
//            users=userDAO.users();
//            Date date=new Date(System.currentTimeMillis());
//            int number=numberDateDAO.numberDate().getNumberDate();
//            java.util.Calendar cal1 = java.util.Calendar.getInstance();
//            cal1.setTime(date);
//            cal1.set(Calendar.MILLISECOND, 0);
//            cal1.set(Calendar.SECOND,0);
//            cal1.set(Calendar.MINUTE,0);
//            cal1.set(Calendar.HOUR, 0);
//            java.util.Calendar cal2 = java.util.Calendar.getInstance();
//            if(users!=null){
//                for(User user:users){
//                    cal2.setTime(user.getCreatedDate());
//                    cal2.set(Calendar.MILLISECOND, 0);
//                    cal2.set(Calendar.SECOND,0);
//                    cal2.set(Calendar.MINUTE,0);
//                    cal2.set(Calendar.HOUR, 0);
//                    cal2.add(java.util.Calendar.DATE, number);
//                    if(cal2.equals(cal1)){
//                        logger.info("send mail to expired user :"+user.getUsername() + " created date: " + user.getCreatedDate());
//                        recipients.add(user.getUsername());
//                    }
//                }
//            }
//            if (recipients.size() > 0) {
//                logger.info("Send expired email notification to " + recipients.size() + " users");
//                String[] recipients1 = recipients.toArray(new String[recipients.size()]);
//                mailService.sendEmail(recipients1, appDetails.get(0).getSubject(), appDetails.get(0).getMessage());
//            } else {
//                logger.info("No expired user found. Skip email sender.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        logger.info("Complete StopEnvironmentJob job");

    }

}
