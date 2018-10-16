package com.shangpin.iog.gucci.schedule;

import com.shangpin.iog.gucci.service.FetchStockImpl;
import com.shangpin.iog.gucci.service.UpdateStockImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@PropertySource("classpath:conf.properties")
public class FetchStockSchedule {

	private static Logger logger = Logger.getLogger("info");

	@Autowired
	@Qualifier("fetchStockImpl")
    FetchStockImpl stockImp;

	@Autowired
	@Qualifier("updateStockImpl")
	UpdateStockImpl updateImp;

	/**
	 * 拉取商品库存 任务
	 */
	@SuppressWarnings("deprecation")
	@Scheduled(cron="${fetchJobsSchedule}")
	public void startFetch(){
		logger.info(new Date().toLocaleString()+"开始拉取库存信息");
		System.out.println(new Date().toLocaleString()+"开始拉取库存信息");
    	Murder mur = Murder.getMur();
    	mur.setStockImp(stockImp);
    	Thread t = new Thread(mur);
    	t.start();
	}


	/**
	 * 推送商品库存信息 任务
	 */
	@SuppressWarnings("deprecation")
	@Scheduled(cron="${updateJobsSchedule}")
	public void startUpdate(){
		logger.info("=================="+new Date().toLocaleString()+"开始更新库存信息");
		System.out.println("=================="+new Date().toLocaleString()+"开始更新库存信息");
		Murder mur = Murder.getMur();
		mur.setStockImp(updateImp);
		Thread t = new Thread(mur);
		t.start();
	}

	
}
