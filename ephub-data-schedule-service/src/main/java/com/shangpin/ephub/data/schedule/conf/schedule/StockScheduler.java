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

	//@Scheduled(cron = "00 21 12 * * ?")
	public void stockTask() {
		try {
			stockService.updateStockToZero();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Scheduled(cron = "00 52 10 * * ?")
	public void pricePush() {
		try {
			log.info("===========任务开始============"); 
			pricePushService.handleErrorPush();
		} catch (Exception e) {
			log.error("重推价格服务异常："+e.getMessage(),e);
		}
	}
}