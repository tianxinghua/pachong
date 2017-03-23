package com.shangpin.ephub.product.business.rest.hubpending.sku.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.rest.hubpending.sku.service.HubPendingSkuHandleService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HubPendingSpu入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/pending-sku-handle")
@Slf4j
public class HubPendingSkuHandleController {
	
	@Autowired
	private HubPendingSkuHandleService hubPendingSkuHandleService;
	
	@RequestMapping(value = "/pending-sku")
	public void handleHubPendingSku(@RequestBody HubSkuPendingDto dto){
		try {
			hubPendingSkuHandleService.handleHubPendingSku(dto);
		} catch (Exception e) {
			log.error("====skuPending处理时发生异常：{}",e);
			e.printStackTrace();
		}
	}
	
}