package com.shangpin.iog.itemInfo_purchase.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Schedule {

	@Autowired
	com.shangpin.iog.itemInfo_purchase.service.OrderSreviceImpl orderService;
	
	@Scheduled(cron="0 0/15 * * * ?")
	public void start(){
		orderService.start();
	}
}
