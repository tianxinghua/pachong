package com.shangpin.iog.product.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.product.service.HandleProductService;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	HandleProductService handleProductService;
	
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		handleProductService.handleProduct();
	}
	
}
