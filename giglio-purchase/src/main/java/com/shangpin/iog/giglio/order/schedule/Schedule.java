package com.shangpin.iog.giglio.order.schedule;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.giglio.order.service.OrderImpl;




@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	OrderImpl orderService;
	
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		orderService.loopExecute();
	}
	
	@Scheduled(cron = "0 0/2  * * * ? ")
	public void confirmOrder() {
		orderService.confirmOrder();
	}
	
//	@Scheduled(cron = "0 0/1  * * * ? ")
//	public void test(){
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setDetail("22218-TU:1,");
//		orderDTO.setSpPurchaseNo("CGD201607120001");
//		orderService.handleConfirmOrder(orderDTO );
//	}
//	
//	@Scheduled(cron = "0 0/1  * * * ? ")
//	public void testCancell(){
//		ReturnOrderDTO deleteOrder =  new ReturnOrderDTO();
//		deleteOrder.setDetail("22218-TU:1,");
//		deleteOrder.setSpPurchaseNo("CGD201607120001");
//		orderService.handleRefundlOrder(deleteOrder); 
//		
//	}
	
}
