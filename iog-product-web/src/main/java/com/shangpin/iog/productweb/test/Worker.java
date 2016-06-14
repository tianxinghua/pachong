package com.shangpin.iog.productweb.test;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.shangpin.iog.productweb.schedule.Task;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Worker implements Runnable {
	private Map<String, Future> recordMap;
	private ExecutorService threadPool;
	private String supplierId;
	private String status;

	@Override
	public void run() {

		final Task task1 = new Task();
		// 执行
		task1.setHanderExpression("testTask");
		task1.setCronExpression("0/5 * * * * ?");
		Trigger trigger = task1.getTrigger();
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("SpringCronJob");
		scheduler.setPoolSize(2);
		scheduler.initialize();
		scheduler.schedule(new Runnable() {
			public void run() {
				try {
					System.out.println("tttttttttttttttttttt1");
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}, trigger);
	}
	
	public static void main(String[] args) {
		new Worker().run();
	}
	
}
