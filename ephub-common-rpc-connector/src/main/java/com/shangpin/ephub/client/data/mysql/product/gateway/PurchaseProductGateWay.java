package com.shangpin.ephub.client.data.mysql.product.gateway;

import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.product.dto.PurchaseProductDto;
import com.shangpin.ephub.client.data.mysql.product.dto.PurchaseProductRecordDto;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 *
 * pendingToHub处理
 *
 */
@FeignClient("ephub-data-mysql-service")
public interface PurchaseProductGateWay {

	@RequestMapping(value = "/purchase-product/get-purchase-product", method = RequestMethod.POST,consumes = "application/json")
    public List<PurchaseProductRecordDto> getProductWithPurchase(@RequestBody PurchaseProductDto dto);

}
