package com.cmg.vrc.job;

import com.cmg.vrc.data.dao.impl.NumberDateDAO;
import com.cmg.vrc.data.dao.impl.UserDAO;
import com.cmg.vrc.data.jdo.NumberDate;
import com.cmg.vrc.data.jdo.User;
import com.cmg.vrc.service.MailService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.util.ArrayList;
import java.util.List;
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
            UserDAO userDAO=new UserDAO();
            NumberDateDAO numberDateDAO=new NumberDateDAO();
            MailService mailService=new MailService();
            List<String> recipients = new ArrayList<String>();
            String subject=null;
            String message = null;
            try {
                List<User> users=userDAO.users();
                DateTime date=new DateTime(System.currentTimeMillis());
                int number=numberDateDAO.numberDate().getNumberDate();
                if(users!=null){
                    for(Object user:users){
                        Object[] array = (Object[]) user;
                        int x=Days.daysBetween(date, (DateTime)array[2]).getDays();
                        if(x>number){
                            recipients.add(array[1].toString());
                        }
                    }
                }
                String[] recipients1 = recipients.toArray(new String[recipients.size()]);
                mailService.sendEmail(recipients1,subject,message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("Complete StopEnvironmentJob job");
        }
    }

}
