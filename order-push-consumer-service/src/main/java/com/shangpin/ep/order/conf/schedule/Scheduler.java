package com.shangpin.ep.order.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.module.orderapiservice.impl.DellaMartiraServiceImpl;
import com.shangpin.ep.order.module.orderapiservice.impl.MonnierfreresServiceImpl;
/**
 * <p>Title: Scheduler</p>
 * <p>Description: 订单的定时任务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年4月21日 上午10:37:15
 *
 */
@Component
public class Scheduler {
	
	@Autowired
	private DellaMartiraServiceImpl dellaMartiraServiceImpl;
	@Autowired
	private MonnierfreresServiceImpl monnierfreresServiceImpl;

	/**
	 * 供应商dellaMartira上传订单到ftp定时任务
	 */
	@Scheduled(cron="00 00 00,14,19 * * ?")
	public void dellaMartiraUploadOrdersToFTP(){
		dellaMartiraServiceImpl.uploadFtp();
	}
	/**
	 * 供应商monnierfreres上传订单到ftp定时任务
	 */
	@Scheduled(cron="00 00 04 * * ?")
	public void monnierfreresUploadOrdersToFTP(){
		monnierfreresServiceImpl.uploadFtp();
	}
}
