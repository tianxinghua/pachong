package com.shangpin.iog.productweb.schedule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.shangpin.iog.productweb.schedule.StartTaskService;
import com.shangpin.iog.productweb.schedule.Task;
import com.shangpin.iog.productweb.schedule.TaskHanderService;
import com.shangpin.iog.productweb.schedule.TaskState;
import com.shangpin.iog.productweb.tool.Threads;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Service("startTaskServiceImpl")
public class StartTaskServiceImpl implements StartTaskService,ApplicationContextAware {

	private ThreadPoolTaskScheduler scheduler = null;

	private Map<String,List<ScheduledFuture>> futureMap = new HashMap<>();
	
	private static final Log        logger = LogFactory.getLog(StartTaskServiceImpl.class);
	private ApplicationContext        context;
	private int shutdownTimeout = Integer.MAX_VALUE;

	@PostConstruct
	public void start() {
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("SpringCronJob");
		scheduler.setPoolSize(2);
		scheduler.initialize();


	}

	@PreDestroy
	public void stop() {
		ScheduledExecutorService scheduledExecutorService = scheduler.getScheduledExecutor();

		Threads.normalShutdown(scheduledExecutorService, shutdownTimeout, TimeUnit.SECONDS);

	}


	@Override
	public void startTask() {
		//TODO 获取数据库信息

		
         List<Task> tasks = new ArrayList<Task>();
         
         
         Task task1 = new Task();
         //执行
         task1.setHanderExpression("testTask");
         task1.setCronExpression("0/5 * * * * ?");
         
         
         Task task2 = new Task();
         //执行
         task2.setHanderExpression("testTask1");
         task2.setCronExpression("0/10 * * * * ?");
         tasks.add(task1);
         tasks.add(task2);
         //TODO 更改数据状态
		for(final Task task:tasks){
			if(task.getState()== TaskState.INIT){

			}
			try{
				final TaskHanderService hander = task.getHander(context);
				Trigger         trigger = task.getTrigger();
				ScheduledFuture  future =  scheduler.schedule(new Runnable() {
					public void run() {
						try {
							hander.executeTask(task);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				}, trigger);

				if(null!=futureMap.get(task.getId())){
					futureMap.put(task.getId().toString(),new ArrayList<ScheduledFuture>());
				}else{
					futureMap.get(task.getId()).add(future);
				}


				//TODO 更新数据
				logger.debug("load task({}) success!"+task.getId());
			}catch (Exception e) {
				
			}
			
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context = arg0;
		
	}

}
