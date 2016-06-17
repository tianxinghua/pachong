package com.shangpin.iog.productmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import org.springframework.stereotype.Component;

import com.shangpin.iog.dto.CsvSupplierInfoDTO;
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
	private Map<String,CsvSupplierInfoDTO> oldMonitorMessage;
	
	public void checkChange(Map<String,CsvSupplierInfoDTO> newMonitorMessage){
		
		Map<String, CsvSupplierInfoDTO> changedMap = isChanged(newMonitorMessage);
		this.oldMonitorMessage = newMonitorMessage;

		if (changedMap.size()>0) {//发生变化,通知观察者
			System.out.println("发生变化");
			this.setChanged();
			this.notifyObservers(changedMap);
		}
		
	}
	
	
	
	// 判断新旧信息是否一致
	private Map<String, CsvSupplierInfoDTO> isChanged(Map<String,CsvSupplierInfoDTO> newMonitorMessage){
		
		if (null==oldMonitorMessage) {
			oldMonitorMessage = new HashMap<String, CsvSupplierInfoDTO>();
		}
		Map<String, CsvSupplierInfoDTO> changedMap = new HashMap<String, CsvSupplierInfoDTO>();
		
		for (Entry<String, CsvSupplierInfoDTO> entry : newMonitorMessage.entrySet()) {
			if (oldMonitorMessage.containsKey(entry.getKey())) {
				if (!oldMonitorMessage.get(entry.getKey()).getState().equals(entry.getValue().getState())) {
					changedMap.put(entry.getKey(), entry.getValue());
				}
			}else{
				changedMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		return changedMap;
	}
	
	
	
}