package com.shangpin.iog.productweb.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskController {
	
	
	private static TaskController taskController  = new TaskController();
	private TaskController(){};
	public static TaskController getTaskController(){
		return taskController;
	}
	
	private  Map<String,Thread> recordMap = new HashMap<String, Thread>();
	private  ExecutorService threadPool = Executors.newCachedThreadPool();
	public void resetTask(Map<String, String> changedMap){
		for (Entry<String, String> entry : changedMap.entrySet()) {
			//TODO 判断状态,如果停止 关闭相应线程
			if (recordMap.containsKey(entry.getKey())) {
				if (entry.getValue().equals("停止状态")) {
					recordMap.get(entry.getKey()).stop();
					recordMap.remove(entry.getKey());
				}					
			}else{
				Worker worker = new Worker();
				
				recordMap.put(entry.getKey(), worker);
				threadPool.execute(worker);
			}
		}
	}
}
