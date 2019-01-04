package com.shangpin.iog.stockframe.schedule;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
@PropertySource("classpath:conf.properties")
public class Schedule {
	protected  static Map<String,String>  threadMap= new HashMap();

//	ThreadLocal<Map<String,String>> threadMap=new ThreadLocal<Map<String,String>>();

	@Scheduled(cron="${checkoutOrderSchedule}")
	public void start() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



}
