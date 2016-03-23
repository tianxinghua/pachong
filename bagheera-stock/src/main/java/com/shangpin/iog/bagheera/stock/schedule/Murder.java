package com.shangpin.iog.bagheera.stock.schedule;

import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import com.shangpin.iog.bagheera.stock.StockClientImp;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
@Component
public class Murder extends TimerTask{
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	private StockClientImp stockImp;
	public void setStockImp(StockClientImp stockImp) {
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
		Thread t = new Thread(new Worker(stockImp));
//		Thread t = new Thread(new Worker());
		Future<?> future = executor.submit(t);
		try {
			future.get(1000*60*90, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			future.cancel(true);
			logError.error(Thread.currentThread().getName()+"超时销毁");
			System.out.println(Thread.currentThread().getName()+"超时销毁");
		}
	}
}
