
package com.shangpin.iog.tiziana.stock.schedule;

import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.logger.LoggerUtil;

@Component
public class Murder extends TimerTask{
	private static ResourceBundle bdl=null;
	private static int time;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
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
//	private static ExecutorService executor = new ThreadPoolExecutor(2, 5, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(3),new ThreadPoolExecutor.CallerRunsPolicy());
	private static ExecutorService executor =Executors.newCachedThreadPool(); 
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
			logError.error(Thread.currentThread().getName()+"超时销毁");
			System.out.println(Thread.currentThread().getName()+"超时销毁");
		}
	}
}

