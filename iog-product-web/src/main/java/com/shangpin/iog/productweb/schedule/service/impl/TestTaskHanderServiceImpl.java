package com.shangpin.iog.productweb.schedule.service.impl;

import com.shangpin.iog.productweb.schedule.Task;
import com.shangpin.iog.productweb.schedule.TaskHanderService;
import org.springframework.stereotype.Service;




@Service("testTask")
public class TestTaskHanderServiceImpl implements TaskHanderService {

	@Override
	public void executeTask(Task task) {
		System.out.println("--------------------testTask--------------"+task.getCronExpression());

	}

}
