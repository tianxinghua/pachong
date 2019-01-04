package com.shangpin.logtemplate.schedule;


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
	LoggerServiceImpl loggerService;
	
	@Scheduled(cron="0/1 * * * * ?")
	public void start() {
		try {
			loggerService.printLog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	


}
