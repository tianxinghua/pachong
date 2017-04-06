package com.shangpin.ephub.client.data.mysql.price.unionselect.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.price.unionselect.dto.PriceQueryDto;
import com.shangpin.ephub.client.data.mysql.price.unionselect.result.HubSupplierPrice;

/**
 * <p>Title: HubSupplierPriceGateWay</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年4月6日 上午11:02:36
 *
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierPriceGateWay {

	@RequestMapping(value = "/hub-supplier-price/select-by-price-query", method = RequestMethod.POST,consumes = "application/json")
	public List<HubSupplierPrice> selectByQuery(@RequestBody PriceQueryDto priceQueryDto);
	
	@RequestMapping(value = "/hub-supplier-price/count-by-price-query", method = RequestMethod.POST,consumes = "application/json")
	public int countByQuery(@RequestBody PriceQueryDto priceQueryDto);
}
