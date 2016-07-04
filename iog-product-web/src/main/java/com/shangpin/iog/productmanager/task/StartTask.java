package com.shangpin.iog.productmanager.task;

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
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.shangpin.iog.dto.CsvSupplierInfoDTO;
import com.shangpin.iog.productmanager.StartUp;
import com.shangpin.iog.productweb.schedule.service.impl.StartTaskServiceImpl;
import com.shangpin.iog.productweb.tool.Threads;
import com.shangpin.iog.service.CsvSupplierService;
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
	
	@Autowired
	CsvSupplierService csvSupplierService;
	
	public void startTask() {
		
		//TODO 获取总体执行时间
		Task totalTask = new Task();
		totalTask.setCronExpression("0/10 * * * * ?");
		Trigger trigger = totalTask.getTrigger();
		
		ScheduledFuture  future =  scheduler.schedule(new Runnable() {
			public void run() {
				try {
					TaskController taskController = (TaskController)StartUp.getApplicationContext().getBean("taskController");
					//0=stop待关闭 , 1=start待启动 , 2=run正在运行 , 3=stopped已关闭
					// 必须先检查状态为2的进行判断，如果状态为2的并没有运行，则置状态为3
					List<CsvSupplierInfoDTO> suppliers_2 = csvSupplierService.findCsvSuppliersByState("2");
					taskController.checkTask(suppliers_2); 
					// 状态0 执行关闭任务 关闭后修改状态为3 				
					List<CsvSupplierInfoDTO> suppliers_0 = csvSupplierService.findCsvSuppliersByState("0");
					taskController.stopTask(suppliers_0); 
					// 状态1 执行启动 启动后修改数据库状态为2
					List<CsvSupplierInfoDTO> suppliers_1 = csvSupplierService.findCsvSuppliersByState("1");
					taskController.startTask(suppliers_1);
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e); 
				}
			}
		}, trigger);
	}
}
