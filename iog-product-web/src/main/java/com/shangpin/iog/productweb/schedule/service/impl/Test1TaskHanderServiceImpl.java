package com.shangpin.iog.productweb.schedule.service.impl;

import com.shangpin.iog.productweb.schedule.Task;
import com.shangpin.iog.productweb.schedule.TaskHanderService;
import org.springframework.stereotype.Service;




@Service("testTask1")
public class Test1TaskHanderServiceImpl implements TaskHanderService {


	@Override
	public void executeTask(Task task) {
		System.out.println("--------------------testTask11--------------");
		

	}

}
