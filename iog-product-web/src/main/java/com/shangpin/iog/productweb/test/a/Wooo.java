package com.shangpin.iog.productweb.test.a;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.productweb.schedule.Task;


public class Wooo extends Thread implements Observer{
	private Test t;
	private ScheduledFuture future;
	public Wooo(Test t) {
		super();
		this.t = t;
		t.addObserver(this);
	}

	@Override
	public void run() {
		
		final Task task = new Task();
		// 执行
		task.setHanderExpression("testTask");
		task.setCronExpression("0/5 * * * * ?");
//		task.setCronExpression(cronExpression);
		Trigger trigger = task.getTrigger();
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("productCron");
		scheduler.setPoolSize(2);
		scheduler.initialize();
		future = scheduler.schedule(new Runnable() {
			public void run() {
				try {
				System.out.println("asdasd");
				String string = HttpUtil45.get("http://shop.areadocks.it/en/api/product?page=2&pagesize=1000", new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10), null);
				System.out.println(string);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, trigger);
		System.out.println("jixuchixin++++++++++++++++++++++++");
//		thread = Thread.currentThread();
//		try {
//			System.out.println("runID"+Thread.currentThread().getId());
//			System.out.println("========================s===========");
//			
//			
//			String string = HttpUtil45.get("http://www.forzieri.com/productfeed/shangpin/data_feed.csv", new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10), null);
//			
//			System.out.println("====================asds===========");
//		} catch (Exception e) {
//			System.out.println("提前终止");
//			e.printStackTrace();
//		}
	}

	@Override
	public void update(Observable o, Object arg) {
		
		future.cancel(true);
		System.out.println(future.isCancelled());
	}

}
