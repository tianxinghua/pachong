package com.shangpin.supplier.product.consumer.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.supplier.product.consumer.refreshDic.service.SupplierProductRetryService;

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

	//@Scheduled(cron = "0/15 * * * * ?")
	public void refreshColorTask() {
		try {
			supplierProductPictureService.processProduct(InfoState.RefreshColor.getIndex(),true);
		} catch (Throwable e) {
			log.info("×××××系统扫描需要重新推送的数据事件发生异常××××××××××",e);
			e.printStackTrace();
		}
	}
	
	//@Scheduled(cron = "0/30 * * * * ?")
	public void refreshSpuMergeTask() {
		try {
			supplierProductPictureService.processProduct(InfoState.Union.getIndex(),true);
		} catch (Throwable e) {
			log.info("=======系统扫描同款需要重新推送的数据事件发生异常======",e);
			e.printStackTrace();
		}
	}
}