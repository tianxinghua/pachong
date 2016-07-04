
package com.shangpin.iog.productmanager.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.shangpin.iog.dto.CsvSupplierInfoDTO;
import com.shangpin.iog.productmanager.AbsSaveProductImpl;
import com.shangpin.iog.productmanager.StartUp;
import com.shangpin.iog.service.CsvSupplierService;


@Component("taskController")
public class TaskController {
	
	
//	private static TaskController taskController  = new TaskController();
//	private TaskController(){};
//	public static TaskController getTaskController(){
//		return taskController;
//	}
	
	private  Map<String,ScheduledFuture> recordMap = new HashMap<String, ScheduledFuture>();
	@Autowired
	CsvSupplierService csvSupplierService;
	
	
	
	public void resetTask(Map<String, CsvSupplierInfoDTO> changedMap){
		System.out.println(recordMap.toString());
		System.out.println("================================");
		for (Entry<String, CsvSupplierInfoDTO> entry : changedMap.entrySet()) {
			// 判断状态,如果停止 关闭相应线程
			if (recordMap.containsKey(entry.getKey())) {
				if (entry.getValue().getState().equals(TaskState.STOP)) {
					System.out.println("停止++++++++++++++++");
					recordMap.get(entry.getKey()).cancel(true);
					recordMap.remove(entry.getKey());
				}
			}else{
				if (entry.getValue().getState().equals(TaskState.START)) {
					System.out.println("开始++++++++++++++++");
					excuteTask(entry.getValue());
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
	
	private void excuteTask(final CsvSupplierInfoDTO csvSupplier){
		
		final Task task = new Task();
		task.setCronExpression(csvSupplier.getCrontime());
		Trigger trigger = task.getTrigger();
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(2);
		scheduler.initialize();
		System.out.println(csvSupplier.getSupplierId() +"开始运行"+new Date().toLocaleString());
		ScheduledFuture<?> future = scheduler.schedule(new Runnable() {
			public void run() {
				try {
					System.out.println(csvSupplier.getSupplierId() +"运行中"+new Date().toLocaleString());
					String[] needColsNo = new String[]{"","0","2","14","22","3","","9,10,11,12,8","","16","4","","23","","","15","23","19","20","1","5"};
					//策略组
					String[] strategys = new String[]{"","","","","","","","more% %0%;","","","","","sin% %0%\"\"","","","","sin% %0%\"\"","","","",""};
					
					AbsSaveProductImpl abs = (AbsSaveProductImpl)StartUp.getApplicationContext().getBean("abssaveproduct");
					abs.handleData("sku", csvSupplier.getSupplierId(), 90, "", "", csvSupplier.getFetchUrl(), "F://products.txt", "\t", needColsNo, strategys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, trigger);
		recordMap.put(csvSupplier.getSupplierId(), future);
	}
}

