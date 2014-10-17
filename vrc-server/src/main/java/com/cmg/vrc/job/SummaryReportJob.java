package com.cmg.vrc.job;

import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.SummaryReport;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/17/14.
 */
public class SummaryReportJob implements Job {
    private static final Logger logger = Logger.getLogger(SummaryReportJob.class.getName());

    public static void startJob() throws SchedulerException {
        String sysenv = Configuration.getValue(Configuration.SYSTEM_ENVIRONMENT);
        if (sysenv != null && sysenv.equalsIgnoreCase("aws")) {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();

            //Get schedular
            Scheduler scheduler = schedulerFactory.getScheduler();

            //Create JobDetail object specifying which Job you want to execute
            JobDetail jobDetail = JobBuilder
                    .newJob(SummaryReportJob.class)
                    .withIdentity("summaryReport", "SummaryGroup")
                    .build();

            //Associate Trigger to the Job
            CronTrigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("summaryReportTrigger", "SummaryGroup")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))
                    .build();

            //Pass JobDetail and trigger dependencies to schedular
            scheduler.scheduleJob(jobDetail, trigger);

            //Start schedular
            scheduler.start();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Start summary report job");
        SummaryReport.analyze();
        logger.info("Complete summary report job");
    }
}
