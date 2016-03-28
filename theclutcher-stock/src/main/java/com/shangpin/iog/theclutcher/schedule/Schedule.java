package com.shangpin.iog.theclutcher.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.theclutcher.stock.service.GrabStockImp;
import com.shangpin.sop.AbsUpdateProductStock;

@Component
@PropertySource("classpath:sop.properties")
public class Schedule {

	private static Logger logger = Logger.getLogger("info");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	@Value("${HOST}")
	private String host;
	@Value("${APP_KEY}")
	private String app_key;
	@Value("${APP_SECRET}")
	private String app_secret;
	@Value("${time}")
    private String time;

	@Autowired	
	GrabStockImp stockImp;
	
	
	@SuppressWarnings("deprecation")
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		System.out.println(new Date().toLocaleString()+"开始更新");
    	Murder mur = Murder.getMur();
    	mur.setStockImp(host,app_key,app_secret,time,stockImp); 
    	Thread t = new Thread(mur);
    	t.start();
	}
	
	
	static class Murder<T> extends TimerTask{
		private String host;
		private String app_key;
		private String app_secret;
		private String time;
		private T stockImp;
		public void setStockImp(String host,String app_key,String app_secret,String time,T stockImp) {
			this.host = host;
			this.app_key = app_key;
			this.app_secret = app_secret;
			this.time = time;
			this.stockImp = stockImp;
		}		
		private static Murder murder = new Murder();
		private Murder(){};
		public static Murder getMur(){
			return murder;
		}
		
		private static ExecutorService executor = new ThreadPoolExecutor(2, 5, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(3),new ThreadPoolExecutor.CallerRunsPolicy());
		@Override
		public void run() {				
			System.out.println(Thread.currentThread().getName()+"执行murder");
			Thread t = new Thread(new Worker( host, app_key, app_secret,stockImp));
			Future<?> future = executor.submit(t);
			try {
				future.get(Integer.parseInt(time), TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				future.cancel(true);
				e.printStackTrace();
				logError.error(Thread.currentThread().getName()+"超时销毁");
				System.out.println(Thread.currentThread().getName()+"超时销毁");
			}
		}
	}
	
	static class Worker<T> implements Runnable{
		private AbsUpdateProductStock stockImp;
		private String host;
		private String app_key;
		private String app_secret;
		public Worker(){};
		public Worker(String host,String app_key,String app_secret,T stockImp) {
			this.host = host;
			this.app_key = app_key;
			this.app_secret = app_secret;
			this.stockImp = (AbsUpdateProductStock) stockImp;
		}
		
		@Override
		public void run() {
			try {
				System.out.println("开始");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				logger.info("更新数据库开始");
				try {
					
					stockImp.updateProductStock(host, app_key, app_secret, "2015-01-01 00:00", format.format(new Date()));
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("更新库存数据库出错"+e.toString());
				}
				logger.info("更新数据库结束");
				System.out.println("结束");
			} catch (Exception e) {
				logger.info("aaaaaaaaaaaaaaa被取消了");
				System.out.println("aaaaaaaaaaaaaaa被取消了");
			}
		}
		
	}
	
}
