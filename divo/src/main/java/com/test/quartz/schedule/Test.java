package com.test.quartz.schedule;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.test.quartz.appc.AppContext;
import com.test.quartz.bean.DataWorkContext;
import com.test.quartz.bean.ScheduleJob;
import com.test.quartz.factory.QuartzJobFactory;
@Component
public class Test {
	private static ApplicationContext factory;
	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args) {

		loadSpringContext();
		SchedulerFactoryBean schedulerFactoryBean = (SchedulerFactoryBean)factory.getBean("schedulerFactoryBean");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
	    //这里获取任务信息数据
	    List<ScheduleJob> jobList = DataWorkContext.getAllJob();
	    try {
			for (ScheduleJob job : jobList) {
				TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
				//获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
				CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
				//不存在，创建一个
				if (null == trigger) {
					JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
						.withIdentity(job.getJobName(), job.getJobGroup()).build();
					jobDetail.getJobDataMap().put("scheduleJob", job);
					//表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
						.getCronExpression());
					//按新的cronExpression表达式构建一个新的trigger
					trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
					scheduler.scheduleJob(jobDetail, trigger);
				} else {
					// Trigger已存在，那么更新相应的定时设置
					//表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
						.getCronExpression());
					//按新的cronExpression表达式重新构建trigger
					trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
						.withSchedule(scheduleBuilder).build();
					//按新的trigger重新设置job执行
					scheduler.rescheduleJob(triggerKey, trigger);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}


	}
}
