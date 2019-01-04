package com.shangpin.iog.Della.purchase.schedule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.Della.purchase.service.OrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	OrderService orderService;
	
	@Scheduled(cron="${jobsSchedule}")
	public void startSOP() {
		try {
			orderService.startSOP();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Scheduled(cron="${jobsSchedule}")
	public void confirmOrder() {
		try {
			orderService.confirmOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Scheduled(cron="${upLoadSchedule}")
	public void saveAndUpLoadOrderFirstTime(){
		try {
			Date startTime = new Date();
	        Date endTime = new Date();
	        startTime =DateTimeUtil.convertFormat(
	        		DateTimeUtil.shortFmt(startTime)+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
	        endTime =DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(endTime)+" 14:00:00", "yyyy-MM-dd HH:mm:ss");
			orderService.saveAndUpLoadOrder(startTime,endTime);
		}catch (Exception  e){
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron="${upLoadScheduleSecond}")
	public void saveAndUpLoadOrderSecondTime(){
		try {
			Date startTime = new Date();
	        Date endTime = new Date();
	        startTime =DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(endTime)+" 14:00:00", "yyyy-MM-dd HH:mm:ss");
	        endTime =DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(endTime)+" 19:00:00", "yyyy-MM-dd HH:mm:ss");
			orderService.saveAndUpLoadOrder(startTime,endTime);
		}catch (Exception  e){
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron="${upLoadScheduleThird}")
	public void saveAndUpLoadOrderThirdTime(){
		try {
			Date startTime = new Date();
	        Date endTime = new Date();
	        startTime =DateTimeUtil.convertFormat(
	        		DateTimeUtil.shortFmt(DateTimeUtil.getAppointDayFromSpecifiedDay(startTime, -1, "D"))+" 19:00:00", "yyyy-MM-dd HH:mm:ss");
	        endTime =DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(endTime)+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			orderService.saveAndUpLoadOrder(startTime,endTime);
		}catch (Exception  e){
			e.printStackTrace();
		}
	}
	
}
