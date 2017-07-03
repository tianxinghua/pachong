package com.shangpin.ephub.data.schedule.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.product.business.studio.gateway.StudioGateWay;

import lombok.extern.slf4j.Slf4j;

/**

 */
@Component
@Slf4j
public class StudioScheduler {
	
    @Autowired
    StudioGateWay studioGateWay;
//	@Scheduled(cron = "00 30 00 * * ?")
	@Scheduled(cron = "0 0 */1 * * ?")
	public void createStudioSlot() {
		try {
			log.info("======================createStudioSlot定时任务开始======================");
			studioGateWay.createStudioSlot();
			log.info("======================createStudioSlot定时任务结束======================");
		} catch (Exception e) {
			log.error("createStudioSlot 任务执行失败："+e.getMessage(),e);
		}
	}


	@Scheduled(cron = "0 0 */1 * * ?")
	public void checkStudioSlot() {
		try {
			log.info("===========任务开始============"); 
			studioGateWay.checkStudioSlot();
		} catch (Exception e) {
			log.error("checkStudioSlot 服务异常："+e.getMessage(),e);
		}
	}
	
	@Scheduled(cron = "00 30 00 * * ?")
	public void downLoadImageByFtp() {
		try {
			log.info("===========任务开始============"); 
			studioGateWay.downLoadImageByFtp();
		} catch (Exception e) {
			log.error("downLoadImageByFtp 服务异常："+e.getMessage(),e);
		}
	}
}