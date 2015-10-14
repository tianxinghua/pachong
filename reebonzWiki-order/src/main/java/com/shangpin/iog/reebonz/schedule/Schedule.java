package com.shangpin.iog.reebonz.schedule;

import com.shangpin.iog.reebonz.service.OrderImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {
	
	com.shangpin.iog.reebonz.service.OrderImpl orderService = new OrderImpl();
	
	@Scheduled(cron = "0 0/1 * * * ? ")
	public void start() {
		orderService.loopExecute();
	}

}
