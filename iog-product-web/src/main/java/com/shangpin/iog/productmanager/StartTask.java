package com.shangpin.iog.productmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.shangpin.iog.productweb.schedule.service.impl.StartTaskServiceImpl;
import com.shangpin.iog.productweb.tool.Threads;

@Component("startTask")
public class StartTask {
	private ThreadPoolTaskScheduler scheduler = null;

	private Map<String, List<ScheduledFuture>> futureMap = new HashMap<>();

	private static final Log logger = LogFactory.getLog(StartTaskServiceImpl.class);
	private ApplicationContext context;
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

		Threads.normalShutdown(scheduledExecutorService, shutdownTimeout,TimeUnit.SECONDS);
	}

	public void startTask() {
		//TODO 获取信息
		List<Task> tasks = new ArrayList<Task>();
		
		
		//TODO 获取总体执行时间
		Task totalTask = new Task();
		totalTask.setHanderExpression("testTask");
		totalTask.setCronExpression("0/5 * * * * ?");
		
		
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
