package com.shangpin.ephub.client.consumer.price.gateway;

import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>Title:PriceMqGateWay.java </p>
 * 价格推送服务


 */
@FeignClient("price-push-consumer-service")
public interface PriceMqGateWay {
	

	@RequestMapping(value = "/hub-supplier-sku-price/transprice", method = RequestMethod.POST, consumes = "application/json")
	public void transPrice(@RequestBody ProductPriceDTO productPriceDTO);
}