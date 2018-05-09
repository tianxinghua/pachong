package com.shangpin.ephub.product.business.rest.sku.supplier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.product.business.rest.price.vo.PriceChangeRecordDto;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.StockQuery;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.StockResult;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**

 */

@RestController
@RequestMapping("/supplier-sku")
@Slf4j
public class SupplierSkuController {
	
	@Autowired
	private HubSupplierSkuGateWay supplierSkuGateWay;


	
	@RequestMapping(value="/list-stock",method=RequestMethod.POST)
	public List<StockResult> priceList(@RequestBody StockQuery stockQuery){
        List<StockResult> stockResults = new ArrayList<>();
        if(null==stockQuery) return stockResults;
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.setPageNo(stockQuery.getPageIndex());
		criteria.setPageSize(stockQuery.getPageSize());
		criteria.createCriteria().andSupplierIdEqualTo(stockQuery.getSupplierId()).andSpSkuNoIsNotNull();
		List<HubSupplierSkuDto> skuDtos = supplierSkuGateWay.selectByCriteria(criteria);
		if(null != skuDtos&&skuDtos.size()>0){
			for(HubSupplierSkuDto sku:skuDtos){
				StockResult stockResult = new StockResult();
				stockResult.setSupplierSkuNo(sku.getSupplierSkuNo());
				stockResult.setStock(sku.getStock());
				stockResult.setSize(sku.getSupplierSkuSize());
				stockResults.add(stockResult);
			}

		}
		return stockResults;
	}


}
