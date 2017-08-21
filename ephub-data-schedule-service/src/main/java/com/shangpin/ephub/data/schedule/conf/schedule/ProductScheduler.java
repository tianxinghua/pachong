package com.shangpin.ephub.data.schedule.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.data.schedule.service.price.PricePushService;
import com.shangpin.ephub.data.schedule.service.product.ProductPullDataService;
import com.shangpin.ephub.data.schedule.service.stock.StockService;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: ProductScheduler</p>
 * <p>Description: 产品的一些定时服务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月21日 下午12:23:23
 *
 */
@Component
@Slf4j
public class ProductScheduler {
	
    @Autowired
	private StockService stockService;
    @Autowired
    private PricePushService pricePushService;
    @Autowired
    private ProductPullDataService productPullDataService;
    

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


	@Scheduled(cron = "00 00 07 * * ?")
	public void pricePush() {
		try {
			log.info("===========pricePush任务开始============"); 
			pricePushService.handleErrorPush();
		} catch (Exception e) {
			log.error("重推价格服务异常："+e.getMessage(),e);
		}
	}
	
	/**
	 * 检测产品拉去
	 */
	@Scheduled(cron = "00 00 09 * * ?")
	public void productCheck(){
		try {
			log.info("===========检测产品拉去任务开始============"); 
			productPullDataService.productCheck();
			log.info("===========检测产品拉去任务结束============"); 
		} catch (Exception e) {
			log.error("检测产品拉去任务异常："+e.getMessage(),e);
		}
	}
	/**
	 * 检测价格推送
	 */
	@Scheduled(cron = "00 00 09 * * ?")
	public void priceCheck(){
		try {
			log.info("===========检测价格推送任务开始============"); 
			pricePushService.priceCheck();
			log.info("===========检测价格推送任务结束============"); 
		} catch (Exception e) {
			log.error("检测价格推送任务异常："+e.getMessage(),e);
		}
	}
}