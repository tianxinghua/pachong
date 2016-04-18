package com.shangpin.iog.magway.schedule;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.magway.service.OrderService;



@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	OrderService orderService;
	
	
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		orderService.loopExecute();
	}
	
	@Scheduled(cron = "0 0/2  * * * ? ")
	public void confirmOrder() {
		orderService.confirmOrder();
	}
	
}
