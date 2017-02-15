package com.shangpin.ephub.product.business.rest.hubpending.spu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HubPendingSpu入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/pending-spu")
@Slf4j
public class HubPendingSpuCheckController {
	
	@Autowired
	private HubPendingSpuCheckService hubCheckRuleService;
	@Autowired
	private IPendingProductService pendingProductService;
	
	@RequestMapping(value = "/check-spu")
	public HubPendingSpuCheckResult checkSpu(@RequestBody HubSpuPendingDto dto){
		log.info("pendingSpu校验接受到数据：{}",dto);
		HubPendingSpuCheckResult returnStr = hubCheckRuleService.checkHubPendingSpu(dto);
		log.info("pendingSpu校验结果：{}",returnStr);
		return returnStr;
	}
	@RequestMapping(value = "/export")
	public PendingProducts exportPengdingSpu(@RequestBody PendingQuryDto pendingQuryDto){
		PendingProducts products = new PendingProducts();
		products.setCreateUser(pendingQuryDto.getCreateUser());
    	List<PendingProductDto> productList = pendingProductService.findPengdingSpu(pendingQuryDto);
    	products.setProduts(productList); 
    	return products;
	}

	
}