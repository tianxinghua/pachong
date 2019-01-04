package com.shangpin.iog.monnierfreres.schedule;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.monnierfreres.service.OrderService;
import com.shangpin.iog.monnierfreres.service.UploadService;



@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	OrderService orderService;
	@Autowired
	UploadService uploadService;
	
	
	//@Scheduled(cron="${jobsSchedule}")
	public void start(){
		orderService.loopExecute();
	}
	
	//@Scheduled(cron = "0 0/2  * * * ? ")
	public void confirmOrder() {
		orderService.confirmOrder();
	}
	
	/**
	 * 上传订单
	 */
	@Scheduled(cron="${uploadConfirmSchedule}")
	public void uploadConfirmOrder(){
		uploadService.uploadConfirmOrder();
	}
	
	/**
	 * 上传取消订单
	 */
//	@Scheduled(cron="${uploadCandellSchedule}")
	public void uploadCancelledOrder(){
		uploadService.uploadCancelledOrder();
	}
	
}
