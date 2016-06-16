
package com.shangpin.iog.productweb.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.shangpin.iog.productweb.schedule.Task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//TODO
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Worker extends Thread {
	private String supplierId;
	private String cronExpression;
	private String url;
	private String filePath;
	private String[] needColsNo;
	private String[] sepStrategys;
	private String sep;
	private String condition;
	private String flag;
	private int day;
	private String picpath;
	private String status;
	private  Map<String,Thread> recordMap;
	
	
	@Override
	public void run() {
//		recordMap.put(supplierId, Thread.currentThread());
		final Task task = new Task();
		// 执行
		task.setHanderExpression("testTask");
		task.setCronExpression("0/10 * * * * ?");
//		task.setCronExpression(cronExpression);
		Trigger trigger = task.getTrigger();
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("productCron");
		scheduler.setPoolSize(2);
		scheduler.initialize();
		scheduler.schedule(new Runnable() {
			public void run() {
				try {
					recordMap.put(supplierId, Thread.currentThread());
					System.out.println(supplierId +"运行中");
//					new AbsSaveProductImpl().handleData(flag, supplierId, day, picpath, condition, url, filePath, sep, needColsNo, sepStrategys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, trigger);
	}
	
	public static void main(String[] args) {
	}
}

