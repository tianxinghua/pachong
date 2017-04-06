package com.shangpin.ephub.price.consumer.rest.controller;


import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import com.shangpin.ephub.price.consumer.rest.service.PriceService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**

 * <p>Description: 价格服务</p>
 *
 */
@RestController
@RequestMapping(value = "/hub-supplier-sku-price")
@Slf4j
public class PriceController {
	
	@Autowired
	private PriceService priceService;
	/**
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/transprice", method = RequestMethod.POST)
	public void transPrice(@RequestBody ProductPriceDTO productPriceDTO) {
		priceService.sendMessageToMQ(productPriceDTO);
    }
}
