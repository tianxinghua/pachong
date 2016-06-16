
package com.shangpin.iog.productmanager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.shangpin.iog.service.SupplierService;


@Component
public class TaskController {
	
	
	private static TaskController taskController  = new TaskController();
	private TaskController(){};
	public static TaskController getTaskController(){
		return taskController;
	}
	
	private  Map<String,ScheduledFuture> recordMap = new HashMap<String, ScheduledFuture>();
	@Autowired
	SupplierService supplierService;
	
	
	
	public void resetTask(Map<String, String> changedMap){
		System.out.println(recordMap.toString());
		System.out.println("================================");
		for (Entry<String, String> entry : changedMap.entrySet()) {
			// 判断状态,如果停止 关闭相应线程
			if (recordMap.containsKey(entry.getKey())) {
				
				if (entry.getValue().contains("stop")) {
					System.out.println("停止++++++++++++++++");
					
					recordMap.get(entry.getKey()).cancel(true);
					
					recordMap.remove(entry.getKey());
					
				}else if(entry.getValue().contains("restart")){
					recordMap.get(entry.getKey()).cancel(true);
//					Worker worker = getWorker(entry.getKey());
//					threadPool.execute(worker);
					//TODO  查询任务
					Task task = new Task();
					task.setSupplierId(entry.getKey());
					excuteTask(task);
				}
				
			}else{
				if (entry.getValue().contains("start")) {
//					Worker worker = getWorker(entry.getKey());
//					threadPool.execute(worker);
					
					//TODO  查询任务
					Task task = new Task();
					task.setSupplierId(entry.getKey());
					task.setCronExpression(entry.getValue().split(",")[1]);
					excuteTask(task);
				}
				
			}
		}
	}
	//TODO 
//	private Worker getWorker(String supplierId){
//		Worker worker = new Worker();
//		worker.setSupplierId(supplierId);
//		worker.setRecordMap(recordMap);
//		return worker;
//	}
	
	private void excuteTask(final Task task){
		task.setHanderExpression("testTask");
		task.setCronExpression(task.getCronExpression());
//		task.setCronExpression(cronExpression);
		Trigger trigger = task.getTrigger();
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("productCron");
		scheduler.setPoolSize(2);
		scheduler.initialize();
		ScheduledFuture<?> future = scheduler.schedule(new Runnable() {
			public void run() {
				try {
					System.out.println(task.getSupplierId() +"运行中"+new Date().toLocaleString());
//					new AbsSaveProductImpl().handleData(flag, supplierId, day, picpath, condition, url, filePath, sep, needColsNo, sepStrategys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, trigger);
		recordMap.put(task.getSupplierId(), future);
	}
}

