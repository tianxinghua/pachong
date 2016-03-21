package com.shangpin.iog.inviqa.schedule;

import com.shangpin.iog.inviqa.service.OrderImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
@PropertySource("classpath:conf.properties")
public class Schedule {
	
	@Autowired
	OrderImpl orderService;
	
//	@Scheduled(cron = "0/20 * * * * ? ")
    @Scheduled(cron="${jobsSchedule}")
	public void start() {
		orderService.loopExecute();
	}
	@Scheduled(cron = "0 0/2  * * * ? ")
	public void confirmOrder() {
		orderService.confirmOrder();
	}

}
