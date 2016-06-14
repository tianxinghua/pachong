package com.shangpin.iog.productweb.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
/**
 * 监控数据库变化,通知观察者
 * @author Administrator
 *
 */
public class Monitor extends Observable{
	
	private Monitor(){};
	private static Monitor monitor;
	public static Monitor getMonitor(){
		if (null==monitor) {
			monitor = new Monitor();
		}
		return monitor;
	}
	private MonitorMessage oldMonitorMessage;
	public MonitorMessage getMonitorMessage(){
		return oldMonitorMessage;
	}
	
	
	public void checkChange(MonitorMessage newMonitorMessage){
		//赋值第一次记录
		if (this.oldMonitorMessage==null) {
			this.oldMonitorMessage = newMonitorMessage;
		}
		
		Map<String, String> changedMap = isChanged(newMonitorMessage);
		this.oldMonitorMessage = newMonitorMessage;

		if (changedMap.size()>0) {//发生变化,通知观察者
			this.setChanged();
			this.notifyObservers(changedMap);
		}
		
	}
	
	
	
	// 判断新旧信息是否一致
	private Map<String, String> isChanged(MonitorMessage newMonitorMessage){
		Map<String, String> changedMap = new HashMap<String, String>();
		
		for (Entry<String, String> entry : newMonitorMessage.getSupplierIdAndStatus().entrySet()) {
			if (oldMonitorMessage.getSupplierIdAndStatus().containsKey(entry.getKey())) {
				if (!oldMonitorMessage.getSupplierIdAndStatus().get(entry.getKey()).equals(entry.getValue())) {
					changedMap.put(entry.getKey(), entry.getValue());
				}
			}else{
				changedMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		return changedMap;
	}
	
}
