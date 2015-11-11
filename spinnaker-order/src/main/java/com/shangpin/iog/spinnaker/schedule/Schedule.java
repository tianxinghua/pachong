package com.shangpin.iog.spinnaker.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.spinnaker.service.OrderService;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {
	
//	@Autowired
//	OrderImpl orderService;
	
	@Autowired
	OrderService orderService;
	
	@Scheduled(cron = "0 0/2 * * * ? ")
	public void start() {
		try {
			orderService.startWMS();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Scheduled(cron = "0 0/2 * * * ? ")
	public void confirmOrder() {
		try {
			orderService.confirmOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//	@Scheduled(cron = "0 0/10 * * * ? ")
//	public void updateEvent() {
//		orderService.updateEvent();
//	}
	//confirmOrder

}
