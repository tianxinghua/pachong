package com.shangpin.ephub.product.business.ui.pending.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title:PendingExportController </p>
 * <p>Description: 待处理页的两个导出</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月24日 下午12:16:41
 *
 */
@RestController
@RequestMapping("/pending-export")
public class PendingExportController {
	
	@Autowired
	private IPendingProductService pendingProductService;

	@RequestMapping(value="/spu",method=RequestMethod.POST)
	public HubResponse<?> exportSpu(@RequestBody PendingQuryDto pendingQuryDto){
		return pendingProductService.exportSpu(pendingQuryDto);
	}
	@RequestMapping(value="/sku",method=RequestMethod.POST)
	public HubResponse<?> exportSku(@RequestBody PendingQuryDto pendingQuryDto){
		return pendingProductService.exportSku(pendingQuryDto);
	}
}
