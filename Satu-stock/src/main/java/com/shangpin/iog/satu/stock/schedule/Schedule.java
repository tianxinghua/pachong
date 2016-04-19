package com.shangpin.iog.satu.stock.schedule;

import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.satu.stock.SatuStockImp;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	private static Logger logger = Logger.getLogger("info");

	@Autowired
	SatuStockImp stockImp;
	
	
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
