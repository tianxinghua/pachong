package com.shangpin.ephub.data.schedule.conf.schedule;

import com.shangpin.ephub.data.schedule.service.PricePushService;
import com.shangpin.ephub.data.schedule.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**

 */
@Component
@Slf4j
public class StockScheduler {
	
    @Autowired
	StockService stockService;

    @Autowired
	PricePushService pricePushService;

	@Scheduled(cron = "00 21 19 * * ?")
	public void stockTask() {
		try {
			log.info("======================清除库存定时任务开始======================");
			stockService.updateStockToZero();
			log.info("======================清除库存定时任务结束======================");
		} catch (Exception e) {
			log.error("清除库存定时任务执行失败："+e.getMessage(),e);
		}
	}


	//@Scheduled(cron = "00 03 11 * * ?")
	public void pricePush() {
		try {
			log.info("===========任务开始============"); 
			pricePushService.handleErrorPush();
		} catch (Exception e) {
			log.error("重推价格服务异常："+e.getMessage(),e);
		}
	}
}