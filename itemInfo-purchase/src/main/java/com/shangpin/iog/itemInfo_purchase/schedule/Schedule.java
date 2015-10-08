package com.shangpin.iog.itemInfo_purchase.schedule;

import org.springframework.scheduling.annotation.Scheduled;

public class Schedule {

	com.shangpin.iog.itemInfo_purchase.service.OrderSreviceImpl orderService;
	
	@Scheduled(cron="0 0/15 * * * ?")
	public void start(){
		orderService.start();
	}
}
