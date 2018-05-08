package com.shangpin.ephub.data.schedule.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.modifyDataStatus.HubModifyDataStatusGateWay;
import com.shangpin.ephub.data.schedule.conf.schedule.properties.ScheduleConfig;
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
	StockService stockService;
    @Autowired
    private PricePushService pricePushService;
    @Autowired
    private ProductPullDataService productPullDataService;
    @Autowired
    private HubModifyDataStatusGateWay hubModifyDataStatusGateWay;
    @Autowired
    private ScheduleConfig scheduleConfig;
    
	@Scheduled(cron = "00 25 20 * * ?")
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
		if(!scheduleConfig.isStartPro()){
			return;
		}
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
	
	/**
	 * 修改数据和图片状态
	 */
	@Scheduled(cron = "00 55 08 * * ?")
	public void modifyUpdateStatus(){
		try {
			log.info("======================更新库存定时任务开始======================");
			stockService.updateSupplierStockToPendindStock();
			
			log.info("===========更新数据和图片状态============"); 
			hubModifyDataStatusGateWay.updateStatus();
			hubModifyDataStatusGateWay.updatePicStatus();
			hubModifyDataStatusGateWay.updateNewPicStatus();
			log.info("===========更新数据和图片状态============"); 
		} catch (Exception e) {
			log.error("修改数据和图片状态任务异常："+e.getMessage(),e);
		}
	}
	
	/**
	 * 
	 */
	@Scheduled(cron = "00 50 08 * * ?")
	public void sendSeason(){
		try {
			log.info("===========检测季节变化推送任务开始============"); 
			pricePushService.checkSeason();
			log.info("===========检测季节变化推送任务结束============"); 
		} catch (Exception e) {
			log.error("检测季节任务异常："+e.getMessage(),e);
		}
	}
	
	/**
	 * 漏掉的价格重新推送
	 */
	@Scheduled(cron = "00 30 01 * * ?")
	public void savePriceRecordAndSendConsumer(){
		if(!scheduleConfig.isStartPro()){
			return;
		}
		try {
			log.info("===========漏掉的价格重新推送============"); 
			pricePushService.savePriceRecordAndSendConsumer(1);
			log.info("===========漏掉的价格重新推送============"); 
		} catch (Exception e) {
			log.error("检测季节任务异常："+e.getMessage(),e);
		}
	}
	
	
	
}