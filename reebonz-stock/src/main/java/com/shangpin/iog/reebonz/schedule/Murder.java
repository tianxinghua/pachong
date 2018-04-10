package com.shangpin.iog.reebonz.schedule;

import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.redi.service.FetchStockImpl;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.concurrent.*;

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
	private FetchStockImpl stockImp;
	public void setStockImp(FetchStockImpl stockImp) {
		this.stockImp = stockImp;
	}
	
	private static Murder murder = new Murder();
	private Murder(){};
	public static Murder getMur(){
		return murder;
	}
	
	private static ExecutorService executor = new ThreadPoolExecutor(1, 2, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(1),new ThreadPoolExecutor.DiscardPolicy());
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+"执行murder");
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
