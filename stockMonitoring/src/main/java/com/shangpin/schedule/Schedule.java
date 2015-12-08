package com.shangpin.schedule;

import com.shangpin.service.StockMoniTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {
	
	@Autowired
	StockMoniTask stockMoniTask;
	
	@Scheduled(cron = "0 0/60 * * *  ? ")
	public void start() {
		stockMoniTask.sendMail();
	}

}
