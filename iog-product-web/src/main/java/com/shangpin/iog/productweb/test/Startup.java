package com.shangpin.iog.productweb.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;




/**
 * Created by Administrator on 2015/9/28.
 */
public class Startup {
	
       private static ApplicationContext factory;
       private static void loadSpringContext()
       {
           factory = new AnnotationConfigApplicationContext(AppContext.class);
       }

       
    public static void main(String[] args) throws  Exception{
    	//加载spring
    	System.out.println("start");
    	loadSpringContext();
    	
    	
//    	Monitor monitor = Monitor.getMonitor();
//		TaskObserver taskObserver = new TaskObserver(monitor, null);
//		
//		MonitorMessage newMonitorMessage = new MonitorMessage();
//		Map<String,String> supplierIdAndStatus = new HashMap<String, String>();
//		newMonitorMessage.setSupplierIdAndStatus(supplierIdAndStatus);
//		newMonitorMessage.getSupplierIdAndStatus().put("1", "1");
//		
//		monitor.checkChange(newMonitorMessage );
//		
//		for (int i = 1; i < 10; i++) {
//			
//			MonitorMessage newMonitorMessage1 = new MonitorMessage();
//			Map<String,String> supplierIdAndStatus1 = new HashMap<String, String>();
//			newMonitorMessage1.setSupplierIdAndStatus(supplierIdAndStatus1);
//			for (int j = 0; j < i; j++) {
//				newMonitorMessage1.getSupplierIdAndStatus().put(String.valueOf(j), String.valueOf(j));
//			}
//			
//			
//			monitor.checkChange(newMonitorMessage1 );
//		}
//		
		
		
    }
}
