package com.shangpin.iog.rossana.stock.schedule;

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

import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.rossana.stock.service.FetchProduct;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	private static Logger logger = Logger.getLogger("info");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	@Value("${supplierId}")
    private String supplierId ;
	@Value("${time}")
    private String time;

	@Autowired	
	FetchProduct stockImp;
	
	
	@SuppressWarnings("deprecation")
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		System.out.println(new Date().toLocaleString()+"开始更新");
    	Murder mur = Murder.getMur();
    	mur.setStockImp(supplierId,time,stockImp); 
    	Thread t = new Thread(mur);
    	t.start();
	}
	
	
	static class Murder<T> extends TimerTask{
		private String supplierId;
		private String time;
		private T stockImp;
		public void setStockImp(String supplierId,String time,T stockImp) {
			this.supplierId = supplierId;
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
			System.out.println("supplierId==="+supplierId);
			System.out.println("time=="+time); 			
			System.out.println(Thread.currentThread().getName()+"执行murder");
			Thread t = new Thread(new Worker(supplierId,stockImp));
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
		private String supplierId;
		public Worker(){};
		public Worker(String supplierId,T stockImp) {
			this.supplierId = supplierId;
			this.stockImp = (AbsUpdateProductStock) stockImp;
		}
		
		@Override
		public void run() {
			try {
				System.out.println("开始");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				logger.info("更新数据库开始");
				try {
					
					stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
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
