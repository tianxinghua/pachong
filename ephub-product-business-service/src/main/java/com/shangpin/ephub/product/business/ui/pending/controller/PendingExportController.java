package com.shangpin.ephub.product.business.ui.pending.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
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
		return pendingProductService.exportSpu(pendingQuryDto,TaskType.EXPORT_PENDING_SPU);
	}
	@RequestMapping(value="/sku",method=RequestMethod.POST)
	public HubResponse<?> exportSku(@RequestBody PendingQuryDto pendingQuryDto){
		return pendingProductService.exportSku(pendingQuryDto,TaskType.EXPORT_PENDING_SKU);
	}
	/**
	 * 全部商品页，导出spu
	 * @param pendingQuryDto
	 * @return
	 */
	@RequestMapping(value="/spu-all",method=RequestMethod.POST)
	public HubResponse<?> exportSpuAll(@RequestBody PendingQuryDto pendingQuryDto){
		return pendingProductService.exportSpu(pendingQuryDto,TaskType.EXPORT_SPU_ALL);
	}
	/**
	 * 全部商品页，导出sku
	 * @param pendingQuryDto
	 * @return
	 */
	@RequestMapping(value="/sku-all",method=RequestMethod.POST)
	public HubResponse<?> exportSkuAll(@RequestBody PendingQuryDto pendingQuryDto){
		return pendingProductService.exportSku(pendingQuryDto,TaskType.EXPORT_SKU_ALL);
	}
	/**
	 * 全部商品页,商品导出
	 * @param pendingQuryDto
	 * @return
	 */
	@RequestMapping(value="/product-all",method=RequestMethod.POST)
	public HubResponse<?> exportproductAll(@RequestBody PendingQuryDto pendingQuryDto){
		return pendingProductService.exportSku(pendingQuryDto,TaskType.ALL_PRODUCT);
	}
}
