package com.shangpin.iog.MengottiOnline.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.MengottiOnline.stock.StockImp;


@Component
public class Worker implements Runnable{
	private StockImp stockImp;
	public Worker(){};
	public Worker(StockImp stockImp) {
		this.stockImp = stockImp;
	}
//	@Autowired
//	StockClientImp stockImp;
	private static Logger logger = Logger.getLogger("info");
	@Override
	public void run() {
		try {
			System.out.println("开始");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			logger.info("更新数据库开始");
			try {
				stockImp.updateProductStock("2015100701573", "2015-01-01 00:00", format.format(new Date()));
			} catch (Exception e) {
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
