package com.shangpin.iog.newmenlook.schedule;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.newmenlook.service.OrderImpl;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {
	
	@Autowired
	OrderImpl orderService;
	
	@Scheduled(cron = "0 0/2 * * * ? ")
	public void start() {
		orderService.loopExecute();
	}
	@Scheduled(cron = "0 0/2  * * * ? ")
	public void confirmOrder() {
		orderService.confirmOrder();
	}
//	@Scheduled(cron = "0 0/10 * * * ? ")
//	public void updateEvent() {
//		orderService.updateEvent();
//	}
	//confirmOrder

}
