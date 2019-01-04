package com.shangpin.iog.logs.schedule;

import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
@PropertySource("classpath:conf.properties")
public class Schedule {
	
	@Scheduled(cron="${checkoutOrderSchedule}")
	public void start() {
		
	}
	
	@Scheduled(cron = "${confirmOrderSchedule}")
	public void confirmOrder() {
		
	}

}
