package com.shangpin.iog.productmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import org.springframework.stereotype.Component;
/**
 * 监控数据库变化,通知观察者
 * @author Administrator
 *
 */
@Component
public class Monitor extends Observable{
	
	private Monitor(){};
	private static Monitor monitor;
	public static Monitor getMonitor(){
		if (null==monitor) {
			monitor = new Monitor();
		}
		return monitor;
	}
//	private MonitorMessage oldMonitorMessage = new MonitorMessage();
//	public MonitorMessage getMonitorMessage(){
//		return oldMonitorMessage;
//	}
	private Map<String,String> oldMonitorMessage;
	
//	public void checkChange(MonitorMessage newMonitorMessage){
	public void checkChange(Map<String,String> newMonitorMessage){
		
		Map<String, String> changedMap = isChanged(newMonitorMessage);
		this.oldMonitorMessage = newMonitorMessage;

		if (changedMap.size()>0) {//发生变化,通知观察者
			System.out.println("发生变化");
			this.setChanged();
			this.notifyObservers(changedMap);
		}
		
	}
	
	
	
	// 判断新旧信息是否一致
	private Map<String, String> isChanged(Map<String,String> newMonitorMessage){
		
		if (null==oldMonitorMessage) {
			oldMonitorMessage = new HashMap<String, String>();
		}
		Map<String, String> changedMap = new HashMap<String, String>();
		
		for (Entry<String, String> entry : newMonitorMessage.entrySet()) {
			if (oldMonitorMessage.containsKey(entry.getKey())) {
				if (!oldMonitorMessage.get(entry.getKey()).equals(entry.getValue())) {
					changedMap.put(entry.getKey(), entry.getValue());
				}
			}else{
				changedMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		return changedMap;
	}
	
	
	
}