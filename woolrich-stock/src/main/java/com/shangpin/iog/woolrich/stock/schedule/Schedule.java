package com.shangpin.iog.woolrich.stock.schedule;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.woolrich.stock.WoolrichStockImp;

@Component
@PropertySource("classpath:sop.properties")
public class Schedule {

	private static Logger logger = Logger.getLogger("info");

	@Autowired
	WoolrichStockImp stockImp;
	
	
	@SuppressWarnings("deprecation")
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		logger.info(new Date().toLocaleString()+"开始更新");
		System.out.println(new Date().toLocaleString()+"开始更新");
    	Murder mur = Murder.getMur();
    	mur.setStockImp(stockImp);
    	Thread t = new Thread(mur);
    	t.start();
	}
	
	
}
