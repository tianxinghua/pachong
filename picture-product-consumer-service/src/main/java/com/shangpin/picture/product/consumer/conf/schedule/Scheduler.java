package com.shangpin.picture.product.consumer.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.picture.product.consumer.service.SupplierProductPictureService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:Scheduler.java </p>
 * <p>Description: 调度器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月18日 上午10:21:08
 */
@Component
@Slf4j
public class Scheduler {
	
	@Autowired
	private SupplierProductPictureService supplierProductPictureService;

	@Scheduled(cron = "00 03 17 * * ?")
	public void pictureTask() {
		try {
			log.info("================================系统开始扫描拉取失败的图片并发送重新拉取图片事件================================");
			long start = System.currentTimeMillis();
			supplierProductPictureService.scanFailedPictureToRetry();
			log.info("√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√系统扫描拉取失败的图片并发送重新拉取图片事件结束,耗时{}毫秒√√√√√√√√√√√√√√√√√√√√√√√√√√√√√√",System.currentTimeMillis()-start);
		} catch (Throwable e) {
			log.info("××××××××××××××××××××××××××××××系统扫描拉取失败的图片并发送重新拉取图片事件发生异常××××××××××××××××××××××××××××××",e);
			e.printStackTrace();
		}
	}
}