package com.cel.dataxfer.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class TransferCron{

	public static void main(String[] args){
				
		try {
			//create scheduler
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			//create job detail
			JobDetail job = JobBuilder.newJob(TransferJob.class)
					.withIdentity("TransferJob")
					.build();
			
			//create cron job trigger
			CronTrigger trigger = TriggerBuilder.newTrigger()
					.withSchedule(CronScheduleBuilder.cronSchedule("* * 7 * *  ?")) //run at 7:00AM everyday 
					.forJob(job)
					.build();
			
			//add job & trigger to scheduler
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}		
	}
}
