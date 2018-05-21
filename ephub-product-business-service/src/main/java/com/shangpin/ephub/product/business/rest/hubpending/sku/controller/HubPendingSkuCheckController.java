package com.shangpin.ephub.product.business.rest.hubpending.sku.controller;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.service.pending.SkuPendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubSkuCheckDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.product.business.rest.hubpending.sku.service.HubPendingSkuCheckService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HubPendingSku入库前 审核前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/pending-sku")
public class HubPendingSkuCheckController {
	
	@Autowired
	private HubPendingSkuCheckService hubCheckRuleService;
	@Autowired
	private IPendingProductService pendingProductService;

	@Autowired
	private SkuPendingService skuPendingService;
	
	@RequestMapping(value = "/check-sku")
	public HubPendingSkuCheckResult checkSku(@RequestBody HubSkuCheckDto dto){
		HubPendingSkuCheckResult result = hubCheckRuleService.checkHubPendingSku(dto);
		return result;
	}
	@RequestMapping(value = "/export")
	public PendingProducts exportPengdingSku(@RequestBody PendingQuryDto pendingQuryDto){
		PendingProducts products = null;
		if(StringUtils.isEmpty(pendingQuryDto.getSpuState())){//待处理页面
			products = pendingProductService.findPendingProducts(pendingQuryDto,false);
		}else{//其他页面
			products = pendingProductService.findPendingProducts(pendingQuryDto,true);
		}
		products.setCreateUser(pendingQuryDto.getCreateUser());
		return products;
	}

	@RequestMapping(value = "/before-audit-check-sku")
	public HubSpuPendingDto   checkSkuBeforeAudit(@RequestBody HubSpuPendingDto dto){

		 skuPendingService.judgeSizeBeforeAudit(dto);
		 return dto;

	}
}
