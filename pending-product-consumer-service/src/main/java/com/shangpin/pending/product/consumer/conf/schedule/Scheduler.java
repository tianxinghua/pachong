package com.shangpin.pending.product.consumer.conf.schedule;

import java.util.ArrayList;
import java.util.List;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.pending.product.consumer.service.MaterialProperties;
import com.shangpin.pending.product.consumer.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.pending.product.consumer.service.HubFilterService;
import com.shangpin.pending.product.consumer.supplier.common.PendingHandler;
import com.shangpin.pending.product.consumer.supplier.common.VariableInit;

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
	PendingHandler pendingHandler;

	@Autowired
	MaterialProperties materialProperties;
	
	@Autowired
	HubFilterService hubFilterService;

	@Autowired
	MaterialService materialService;


	@Autowired
	VariableInit variableInit;
	@Scheduled(cron = "0 10 8 * * ?")
	public void modelTask() {
		
		try {
			List<String> categoryList = new ArrayList<String>();
			categoryList.add("A01B01");
			categoryList.add("A01B02");
			categoryList.add("A02B01");
			categoryList.add("A02B02");
			categoryList.add("A03B01");
			categoryList.add("A03B02");
			categoryList.add("A11");
			for(String category:categoryList){
				log.info("待刷新的品类："+category);
				hubFilterService.refreshHubFilter(category);
			}
		} catch (Throwable e) {
			log.info("=======系统扫描同款需要重新推送的数据事件发生异常======",e);
			e.printStackTrace();
		}
		
	}


	@Scheduled(cron = "0 0/1 * * * ?")
	public void refreshMaterial() {
		try {
			if(materialProperties.isRefresh()){
				List<HubSupplierSpuDto> supplierSpuList  = materialService.getSupplierSpuList();
				if(null!=supplierSpuList&&supplierSpuList.size()>0){

					materialService.translateMaterial(supplierSpuList);
				}
			}
			;
		} catch (Throwable e) {
			log.info("=======刷新材质发生异常======",e);
			e.printStackTrace();
		}
	}
	
//	@Scheduled(cron = "0/30 * * * * ?")
//	public void aa() {
//		
//		String supplierId= "2015103001637";
//		String supplierSkuNo = "aw430317-011-00";
//
//		PendingSpu spu = new PendingSpu();
//		List<PendingSku> skuList = new ArrayList<>();
//		PendingSku skus = new PendingSku();
//		skus.setSupplierId(supplierId);
//		skus.setSupplierSkuNo(supplierSkuNo);
//		skuList.add(skus);
//		spu.setSkus(skuList);
//		
//		HubSpuPendingDto spuPendingDto = new HubSpuPendingDto();
//		spuPendingDto.setCatgoryState((byte)1);
//		try {
//			pendingHandler.refreshHubSize(supplierId, spu, spuPendingDto);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
}