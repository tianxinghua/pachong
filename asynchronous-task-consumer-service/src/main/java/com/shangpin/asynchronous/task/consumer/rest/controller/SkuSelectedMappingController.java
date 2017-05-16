package com.shangpin.asynchronous.task.consumer.rest.controller;


import com.shangpin.asynchronous.task.consumer.rest.service.SkuSelectedMappingService;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**

 * <p>Description: 选品服务</p>
 *
 */
@RestController
@RequestMapping(value = "/hub-sku-supplier-mapping")
public class SkuSelectedMappingController {
	
	@Autowired
	private SkuSelectedMappingService skuSelectedMappingService;
	/**
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/select", method = RequestMethod.POST)
	public void transPrice(@RequestBody ProductMessageDto productMessageDto) {
		skuSelectedMappingService.sendMessageToMQ(productMessageDto);
    }
}
