package com.shangpin.iog.theclutcher.schedule;

import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.theclutcher.stock.service.StockImp;

@Component
public class Murder extends TimerTask
{
  private static ResourceBundle bdl = null;
  private static int time;
  private static LoggerUtil logError;
  private StockImp stockImp;
  private static Murder murder;
  private static ExecutorService executor;

  public void setStockImp(StockImp stockImp)
  {
    this.stockImp = stockImp;
  }

  public static Murder getMur()
  {
    return murder;
  }

  public void run()
  {
    System.out.println(Thread.currentThread().getName() + "执行murder");
    Thread t = new Thread(new Worker(this.stockImp));
    Future future = executor.submit(t);
    try {
      future.get(time, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      future.cancel(true);
      logError.error(Thread.currentThread().getName() + "超时销毁");
      System.out.println(Thread.currentThread().getName() + "超时销毁");
    }
  }

  static
  {
    if (null == bdl)
      bdl = ResourceBundle.getBundle("conf");
    time = Integer.valueOf(bdl.getString("time")).intValue();

    logError = LoggerUtil.getLogger("error");

    murder = new Murder();

    executor = new ThreadPoolExecutor(2, 5, 300L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(3), new ThreadPoolExecutor.DiscardPolicy());
  }
}