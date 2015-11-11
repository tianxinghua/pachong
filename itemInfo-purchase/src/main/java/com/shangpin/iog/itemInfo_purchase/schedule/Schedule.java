package com.shangpin.iog.itemInfo_purchase.schedule;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	com.shangpin.iog.itemInfo_purchase.service.OrderSreviceImpl orderService;
	
	
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		orderService.start();
	}
	
}
