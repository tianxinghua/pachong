package com.shangpin.iog.productweb.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskController {
	
	
	private static Map<String,Future> recordMap = new HashMap<String, Future>();
	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	private static TaskController taskController  = new TaskController();
	private TaskController(){};
	public static TaskController getTaskController(){
		return taskController;
	}
	
	public void resetTast(Map<String, String> changedMap){
		for (Entry<String, String> entry : changedMap.entrySet()) {
			threadPool.execute(new Worker(recordMap, threadPool, entry.getKey(), entry.getValue()));
		}
	}
}
