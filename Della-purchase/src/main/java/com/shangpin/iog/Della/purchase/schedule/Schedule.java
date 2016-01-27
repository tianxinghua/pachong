package com.shangpin.iog.Della.purchase.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.Della.purchase.service.OrderService;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	OrderService orderService;
	
	@Scheduled(cron="${jobsSchedule}")
	public void startSOP() {
		try {
			orderService.startSOP();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Scheduled(cron="${jobsSchedule}")
	public void confirmOrder() {
		try {
			orderService.confirmOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Scheduled(cron="${upLoadSchedule}")
	public void saveAndUpLoadOrder(){
		try {
			orderService.saveAndUpLoadOrder();
		}catch (Exception  e){
			e.printStackTrace();
		}
	}
	
}
