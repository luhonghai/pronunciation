package com.cmg.vrc.job;

import com.amazonaws.services.elasticbeanstalk.model.EnvironmentDescription;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.util.AWSHelper;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/17/14.
 */
public class BeanstalkJob {
    private static final String[] ENVIRONMENTS = new String[] {
            "accenteasytomcat-PRD"
    };

    private static final Logger logger = Logger.getLogger(BeanstalkJob.class.getName());

    public static void startJob() throws SchedulerException {
        String sysenv = Configuration.getValue(Configuration.SYSTEM_ENVIRONMENT);
        if (sysenv != null && sysenv.equalsIgnoreCase("aws")) {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            //Get schedular
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail jobStart = JobBuilder
                    .newJob(StartEnvironmentJob.class)
                    .withIdentity("StartEnvironmentJob", "BeanstalkGroup")
                    .build();

            JobDetail jobStop = JobBuilder
                    .newJob(StopEnvironmentJob.class)
                    .withIdentity("StopEnvironmentJob", "BeanstalkGroup")
                    .build();

            //Associate Trigger to the Job
            CronTrigger triggerStart = TriggerBuilder
                    .newTrigger()
                    .withIdentity("StartEnvironmentJobTrigger", "BeanstalkGroup")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0,1))
                    .build();

            CronTrigger triggerStop = TriggerBuilder
                    .newTrigger()
                    .withIdentity("StopEnvironmentJobTrigger", "BeanstalkGroup")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(16,0))
                    .build();

            //Pass JobDetail and trigger dependencies to schedular
            scheduler.scheduleJob(jobStart, triggerStart);

            scheduler.scheduleJob(jobStop, triggerStop);

            //Start schedular
            scheduler.start();
        }
    }

    public static class StopEnvironmentJob implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            logger.info("Start StopEnvironmentJob job");
            AWSHelper awsHelper = new AWSHelper();
            List<EnvironmentDescription> descriptionList = awsHelper.getEnvironments();
            for (String env : ENVIRONMENTS) {
                logger.info("Test environment: " + env);
                boolean exist = false;
                if (descriptionList != null && descriptionList.size() > 0) {
                    for (EnvironmentDescription description : descriptionList) {
                        logger.info("Found online environment: " + description.getEnvironmentName());
                        if (description.getEnvironmentName().equalsIgnoreCase(env)) {
                            logger.info("Matched environment!");
                            exist = true;
                            break;
                        }
                    }
                }
                if (exist) {
                    logger.info( env + " is exist. Try to terminate this!");
                    awsHelper.terminateEnvironment(env);
                } else {
                    logger.info( env + " is not exist. Skip by default");
                }
            }
            logger.info("Complete StopEnvironmentJob job");
        }
    }

    public static class StartEnvironmentJob implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            logger.info("Start StartEnvironmentJob job");
            AWSHelper awsHelper = new AWSHelper();
            List<EnvironmentDescription> descriptionList = awsHelper.getEnvironments();
            for (String env : ENVIRONMENTS) {
                logger.info("Test environment: " + env);
                boolean exist = false;
                if (descriptionList != null && descriptionList.size() > 0) {
                    for (EnvironmentDescription description : descriptionList) {
                        logger.info("Found online environment: " + description.getEnvironmentName());
                        if (description.getEnvironmentName().equalsIgnoreCase(env)) {
                            logger.info("Matched environment!");
                            exist = true;
                            break;
                        }
                    }
                }
                if (!exist) {
                    logger.info(env + " is not exist. Try to create new one");
                    awsHelper.createEnvironment(env);
                } else {
                    logger.info( env + " is exist. Skip by default");
                }
            }
            logger.info("Complete StartEnvironmentJob job");
        }
    }
}
