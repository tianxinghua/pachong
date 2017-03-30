package com.shangpin.ephub.product.business.rest.price.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;

@RestController
@RequestMapping("price")

public class PriceController {

	@RequestMapping(value = "/save")
	public void savePriceRecordAndSendConsumer(HubSupplierSkuDto hubSupplierSkuDto) throws Exception{
		
	}
}
