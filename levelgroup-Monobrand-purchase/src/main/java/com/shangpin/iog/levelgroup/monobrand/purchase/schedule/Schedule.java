package com.shangpin.iog.levelgroup.monobrand.purchase.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.levelgroup.monobrand.purchase.service.OrderService;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");

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
			logger.info("订单确认开始");
			orderService.confirmOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
