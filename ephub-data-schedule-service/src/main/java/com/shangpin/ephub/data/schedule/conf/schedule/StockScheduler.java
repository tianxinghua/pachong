package com.shangpin.ephub.data.schedule.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.data.schedule.service.stock.StockService;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: ProductScheduler</p>
 * <p>Description: 库存的一些定时服务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月21日 下午12:23:23
 *
 */
@Component
@Slf4j
public class StockScheduler {
	
    @Autowired
	private StockService stockService;
    
	@Scheduled(cron = "00 55 20 * * ?")
	public void stockTask() {
		try {
			log.info("======================清除库存定时任务开始======================");
			stockService.updateStockToZero();
			log.info("======================清除库存定时任务结束======================");
		} catch (Exception e) {
			log.error("清除库存定时任务执行失败："+e.getMessage(),e);
		}
	}

}