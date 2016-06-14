package com.shangpin.iog.productweb.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskObserver implements Observer {

	private TaskController taskController;

	public TaskObserver(Observable monitor, TaskController taskController) {
		this.taskController = taskController;
		monitor.addObserver(this);
	}

	// TODO 根据改变的supplier状态,修改执行
	@Override
	public void update(Observable monitor, Object changedMap) {

		HashMap<String, String> changeMap = (HashMap<String, String>) changedMap;
		
		taskController.resetTast(changeMap);
		
	}
}
