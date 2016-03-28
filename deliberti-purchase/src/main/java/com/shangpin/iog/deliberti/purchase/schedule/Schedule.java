package com.shangpin.iog.deliberti.purchase.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.deliberti.purchase.service.OrderService;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	OrderService order;
	
	@Scheduled(cron="${jobsSchedule}")
	public void startSOP() {
		try {
			order.startWMS();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Scheduled(cron="${jobsSchedule}")
	public void confirmOrder() {
		try {
			order.confirmOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
