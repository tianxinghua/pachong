package com.shangpin.iog.refreshtoken.schedule;

import com.google.gson.Gson;
import com.shangpin.iog.refreshtoken.service.ProductFetchUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	private static Logger logger = Logger.getLogger("info");

	@Autowired
	ProductFetchUtil productFetchUtil;

	@Scheduled(cron="${getGebTokenJobSchedule}")
	public void getGebTokenMethod(){
		System.out.println("获取EGB的token");
		logger.info("------获取EGB的token定时器开启---------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("------获取EGB的token定时器开启---------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		productFetchUtil.queryGebToken();
		System.out.println("------获取EGB的token定时器结束---------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logger.info("------获取EGB的token定时器结束---------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	@Scheduled(cron = "${getVipTokenJobSchedule}")
	public void getVipTokenMethod(){
		System.out.println("获取Vip的token");
		logger.info("------获取Vip的token定时器开启---------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("------获取Vip的token定时器开启---------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		productFetchUtil.queryVipToken();
		System.out.println("------获取Vip的token定时器结束---------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		logger.info("------获取Vip的token定时器结束---------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	
}
