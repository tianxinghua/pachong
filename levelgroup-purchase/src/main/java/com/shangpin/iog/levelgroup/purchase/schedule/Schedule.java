package com.shangpin.iog.levelgroup.purchase.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	com.shangpin.iog.levelgroup.purchase.service.OrderSreviceImpl orderService;
	
	
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		orderService.start();
	}
	
}
