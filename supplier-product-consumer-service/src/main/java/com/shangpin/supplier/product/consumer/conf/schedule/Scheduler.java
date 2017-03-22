package com.shangpin.supplier.product.consumer.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.supplier.product.consumer.service.SupplierProductRetryService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:Scheduler.java </p>
 * <p>Description: 调度器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2017年1月18日 上午10:21:08
 */
@Component
@Slf4j
public class Scheduler {
	
	@Autowired
	private SupplierProductRetryService supplierProductPictureService;

//	@Scheduled(cron = "0 0/30 * * * ?")
	public void pictureTask() {
		try {
			long start = System.currentTimeMillis();
			log.info("×××××××系统扫描到infoState："+4+"需要重新推送的数据×××××××××××××");
			supplierProductPictureService.processProduct((byte)4);
			log.info("×××××系统扫描到需要重新推送的数据结束,耗时{}毫秒×××××××××××××",System.currentTimeMillis()-start);
		} catch (Throwable e) {
			log.info("×××××系统扫描需要重新推送的数据事件发生异常××××××××××",e);
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron = "0 0/30 * * * ?")
	public void modelTask() {
		try {
			long start = System.currentTimeMillis();
			log.info("========系统扫描到infoState："+5+"需要重新推送的数据===");
			supplierProductPictureService.processProduct((byte)5);
			log.info("=====系统扫描到需要重新推送的数据结束,耗时{}毫秒======",System.currentTimeMillis()-start);
		} catch (Throwable e) {
			log.info("=======系统扫描同款需要重新推送的数据事件发生异常======",e);
			e.printStackTrace();
		}
	}
}