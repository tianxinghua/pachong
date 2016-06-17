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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.productweb.schedule.TaskHanderService;
import com.shangpin.iog.productweb.schedule.service.impl.StartTaskServiceImpl;
import com.shangpin.iog.productweb.tool.Threads;
import com.shangpin.iog.service.SupplierService;

@Component("startTask")
public class StartTask {
	private ThreadPoolTaskScheduler scheduler = null;

	private Map<String, List<ScheduledFuture>> futureMap = new HashMap<>();

	private static final Log logger = LogFactory.getLog(StartTaskServiceImpl.class);
//	private ApplicationContext context;
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
	@Autowired
	SupplierService supplierService;
	public void startTask() {
		//TODO 获取信息
		List<Task> tasks = new ArrayList<Task>();
		
		
		//TODO 获取总体执行时间
		Task totalTask = new Task();
		totalTask.setCronExpression("0/10 * * * * ?");
		
//		excuteTask(totalTask);
//		final TaskHanderService hander = totalTask.getHander(context);
		Trigger         trigger = totalTask.getTrigger();
		ScheduledFuture  future =  scheduler.schedule(new Runnable() {
			public void run() {
				try {
					Monitor monitor = Monitor.getMonitor();
					TaskObserver taskObserver = TaskObserver.getTaskObserver();
					taskObserver.setMonitor(monitor);
					taskObserver.setTaskController(TaskController.getTaskController());
					monitor.addObserver(taskObserver);
					//TODO 获取信息
					Map<String, String> newMonitorMessage = new HashMap<String, String>();
					List<SupplierDTO> list = supplierService.findAllWithAvailable();
					System.out.println("获取到"+list.size());
					for (SupplierDTO supplierDTO : list) {
						newMonitorMessage.put(supplierDTO.getSupplierId(), supplierDTO.getSupplierName());
					}
					
					
					monitor.checkChange(newMonitorMessage );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, trigger);
		System.out.println("asdasd");
	}

	
	private void excuteTask(Task task){
		try{
//			final TaskHanderService hander = task.getHander(context);
			Trigger         trigger = task.getTrigger();
			ScheduledFuture  future =  scheduler.schedule(new Runnable() {
				public void run() {
					try {
						Monitor monitor = Monitor.getMonitor();
//						TaskObserver taskObserver = new TaskObserver(monitor, TaskController.getTaskController());
						//TODO 获取信息
						Map<String, String> newMonitorMessage = new HashMap<String, String>();
						
						List<SupplierDTO> list = supplierService.findAllWithAvailable();
						for (SupplierDTO supplierDTO : list) {
							newMonitorMessage.put(supplierDTO.getSupplierId(), supplierDTO.getSupplierName());
						}
						
						
						monitor.checkChange(newMonitorMessage );
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, trigger);
			//TODO 更新数据
		}catch (Exception e) {
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
