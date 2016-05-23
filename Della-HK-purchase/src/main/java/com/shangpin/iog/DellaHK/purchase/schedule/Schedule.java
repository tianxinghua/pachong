package com.shangpin.iog.DellaHK.purchase.schedule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.DellaHK.purchase.Upload;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	@Autowired
	Upload upload;
		
	@Scheduled(cron="${upLoadSchedule}")
	public void saveAndUpLoadOrderFirstTime(){
		try {
			upload.testUpload();
		}catch (Exception  e){
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron="${upLoadScheduleSecond}")
	public void saveAndUpLoadOrderSecondTime(){
		try {
			upload.testUpload();
		}catch (Exception  e){
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron="${upLoadScheduleThird}")
	public void saveAndUpLoadOrderThirdTime(){
		try {
			upload.testUpload();
		}catch (Exception  e){
			e.printStackTrace();
		}
	}
	
}
