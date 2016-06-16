package com.shangpin.iog.productweb.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.SupplierService;
@Component("taskschedule")
public class TaskSchedule {
	@Autowired
	SupplierService supplierService;
	
   	@Scheduled(cron="0/5 * * * * ?")
   	public void start(){
   		System.out.println("start====================");
   		try {
			List<SupplierDTO> supplierList = supplierService.findAllWithAvailable();
			System.out.println(supplierList.size()+"++++++++++++++++++++++++++++++");
			
			MonitorMessage newMonitorMessage = new MonitorMessage();
			Map<String,String> supplierIdAndStatus = new HashMap<String, String>();
			for (SupplierDTO supplierDTO : supplierList) {
				supplierIdAndStatus.put(supplierDTO.getSupplierId(), supplierDTO.getSupplierName());
			}
			newMonitorMessage.setSupplierIdAndStatus(supplierIdAndStatus);
			new TaskObserver(Monitor.getMonitor(), TaskController.getTaskController());
			Monitor.getMonitor().checkChange(newMonitorMessage);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
   	}
       
}
