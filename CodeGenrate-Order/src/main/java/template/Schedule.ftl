package com.shangpin.iog.${supplierName}.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.${supplierName}.service.OrderService;



@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	OrderService orderService;
	
	
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		orderService.loopExecute();
	}
	
	@Scheduled(cron = "${jobsScheduleConfirm}")
	public void confirmOrder() {
		orderService.confirmOrder();
	}
	
}
