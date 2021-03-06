package com.shangpin.iog.daniello.stock.schedule;

import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.sop.AbsUpdateProductStock;
@Component
public class Murder extends TimerTask{
	private static ResourceBundle bdl=null;
	private static int time;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("sop");
        time = Integer.valueOf(bdl.getString("time"));
    }
    
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	private AbsUpdateProductStock stockImp;
	public void setStockImp(AbsUpdateProductStock stockImp) {
		this.stockImp = stockImp;
	}
	
	private static Murder murder = new Murder();
	private Murder(){};
	public static Murder getMur(){
		return murder;
	}
	private static ExecutorService executor = new ThreadPoolExecutor(2, 3, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(3),new ThreadPoolExecutor.DiscardPolicy());
//	private static ExecutorService executor =Executors.newCachedThreadPool(); 
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+"执行murder");
		System.out.println(executor.toString());
		Thread t = new Thread(new Worker(stockImp));
		Future<?> future = executor.submit(t);
		try {
			future.get(time, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			future.cancel(true);
			logError.error(Thread.currentThread().getName()+"超时销毁"+e.toString());
			System.out.println(Thread.currentThread().getName()+"超时销毁");
		}
	}
}
