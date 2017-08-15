package com.shangpin.ephub.client.data.airshop.supplier.product.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.airshop.supplier.product.dto.HubSupplierProductRequestWithPage;
import com.shangpin.ephub.client.data.airshop.supplier.product.result.HubSupplierProductDto;

/**
 * <p>Title:HubSkuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:08:37
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierProductGateWay {

	@RequestMapping(value = "/hub-supplier-product/select-product-page", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierProductDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierProductRequestWithPage criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-product/count", method = RequestMethod.POST,consumes = "application/json")
    public int count(@RequestBody HubSupplierProductRequestWithPage criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-product/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSupplierProductDto product);
	
}
