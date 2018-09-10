package com.shangpin.spider.quartz;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

/**
 * @author njt
 * @date 创建时间：2017年11月22日 上午11:14:00
 * @version 1.0
 * @parameter
 */
@Component
public class QuartzManager {
	@Autowired @Qualifier("Scheduler")
	private Scheduler scheduler;
	public void addJob(String jobName, String jobGroupName, String triggerName,
			String triggerGroupName, Class<? extends Job> jobClass,
			Map<String, Object> data, String cronExpression) {
		try {
			// 任务名，任务组，任务执行类
			JobDetail jobDetail = JobBuilder.newJob().ofType(jobClass)
					.usingJobData(new JobDataMap(data))
					.withIdentity(jobName, jobGroupName).build();
			// 触发器名,触发器组
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.forJob(jobName, jobGroupName)
					.withIdentity(triggerName, triggerGroupName)
					.withSchedule(
							CronScheduleBuilder.cronSchedule(cronExpression))
//							SimpleScheduleBuilder.repeatHourlyForever(hours))
					.build();
			// 启动
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public Set<JobKey> listAll(String jobGroup) {
        try {
            return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Sets.newConcurrentHashSet();
    }
	
	public Triple<JobDetail, String, Trigger> findInfo(JobKey jobKey) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
            TriggerState state = scheduler.getTriggerState(trigger.getKey());
            return Triple.of(jobDetail, state.toString(), trigger);
        } catch (Exception e) {
            return null;
        }
    }
	
	public void delete(JobKey jobkey) {
		try {
            TriggerKey triggerKey = scheduler.getTriggersOfJob(jobkey).get(0).getKey();
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(jobkey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	public void pause(JobKey jobKey) {
		try {
            // 暂停任务
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	public void resume(JobKey jobKey) {
		try {
            // 继续任务
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		
	}
}
