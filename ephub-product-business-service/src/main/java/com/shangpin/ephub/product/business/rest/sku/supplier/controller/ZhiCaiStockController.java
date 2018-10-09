package com.shangpin.ephub.product.business.rest.sku.supplier.controller;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.ephub.product.business.service.supplier.SupplierInHubService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.rest.sku.supplier.dto.ZhiCaiSkuStock;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.ZhiCaiStock;
import com.shangpin.ephub.response.HubResponse;
import com.shangpin.iog.ice.service.StockHandleService;

import lombok.extern.slf4j.Slf4j;

/**

 */

@RestController
@RequestMapping("/zhicai")
@Slf4j
public class ZhiCaiStockController {
	@Autowired
	StockHandleService stockHandleService;

	@Autowired
	SupplierInHubService supplierInHubService;

	// zhicai更新库存
	@RequestMapping(value = "/update-stock", method = RequestMethod.POST)
	public HubResponse<?> zhicaiUpdateStock(@RequestBody ZhiCaiStock zhiCaiStock) {
		if (StringUtils.isBlank(zhiCaiStock.getSupplierId()))
			return HubResponse.errorResp("供应商id不能为null!");
		if (zhiCaiStock.getZhiCaiSkuStockList() == null || zhiCaiStock.getZhiCaiSkuStockList().size() == 0)
			return HubResponse.errorResp("供应商sku List不能为null!");

		Map<String, Integer> map = new HashMap<>();
		for (ZhiCaiSkuStock zhiCaiSkuStock : zhiCaiStock.getZhiCaiSkuStockList()) {
			map.put(zhiCaiSkuStock.getSpSkuNo(), zhiCaiSkuStock.getQty());
		}

		Integer updateFailedNum = null;
		try {
			if(supplierInHubService.isDirectHotboom(zhiCaiStock.getSupplierId())){//代购直发供货商
				updateFailedNum = stockHandleService.updateIceDirectStock(zhiCaiStock.getSupplierId(), map);
			}else{
				updateFailedNum = stockHandleService.updateIceStock(zhiCaiStock.getSupplierId(), map);
			}

			log.info(" updateFailedNum:" + updateFailedNum);
		} catch (Exception e) {
			log.error("-----------更新库存 信息失败！！！！ ");
			e.printStackTrace();
			return HubResponse.errorResp("更新失败!");
		}
		return HubResponse.successResp("更新成功!");
	}

}
