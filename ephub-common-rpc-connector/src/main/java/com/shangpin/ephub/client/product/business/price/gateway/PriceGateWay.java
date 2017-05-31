package com.shangpin.ephub.client.product.business.price.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
/**
 * <p>Title: PriceGateWay</p>
 * <p>Description: 价格变化推送公共服务</p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月30日 下午5:57:35
 *
 */
@FeignClient("ephub-product-business-service")
public interface PriceGateWay {

	@RequestMapping(value = "/price/save-and-sendmessage", method = RequestMethod.POST,consumes = "application/json")
	public void savePriceRecordAndSendConsumer(PriceDto priceDto) throws Exception;



}
