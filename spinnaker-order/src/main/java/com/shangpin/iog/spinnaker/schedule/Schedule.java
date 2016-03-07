package com.shangpin.iog.spinnaker.schedule;

import com.shangpin.iog.spinnaker.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.spinnaker.service.OrderService;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
@PropertySource("classpath:conf.properties")
public class Schedule {
	
//	@Autowired
//	OrderImpl orderService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	LogisticsService logisticsService;
	
	@Scheduled(cron="${checkoutOrderSchedule}")
	public void start() {
		try {
			orderService.startWMS();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Scheduled(cron = "${confirmOrderSchedule}")
	public void confirmOrder() {
		try {
			orderService.confirmOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Scheduled(cron = "${shipOrderSchedule}")
	public void handleShippedOrder(){
		try {
			logisticsService.handleShippedOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Scheduled(cron = "${invoiceSchedule}")
	public void handleInvoice(){
		try {
			logisticsService.handleInvoice();
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
