package com.shangpin.iog.linde.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.manzoni.service.FetchProduct;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
@PropertySource("classpath:conf.properties")
public class Schedule {
	
	@Autowired
	FetchProduct orderService;
	//监控库存，30分钟执行一次，如果库存超过4小时未更新则发邮件通知
	@Scheduled(cron = "0/30  * * * ? ")
	public void start() {
		orderService.fetchProductAndSave();
	}

	//监控订单
	@Scheduled(cron="${orderSchedule}")
	public void sendEmailToSupplier1() {
		orderService.sendEmailToSupplier1();
	}
	@Scheduled(cron = "0 0 20 ? * *")
	public void sendEmailToSupplier2() {
		orderService.sendEmailToSupplier2();
	}
}
