package com.shangpin.ephub.client.data.mongodb.product.supplier.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mongodb.product.supplier.gateway.dto.SupplierProductDto;

/**
 * <p>Title:SupplierProductController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月22日 上午11:10:44
 */
@FeignClient("ephub-data-mongodb-service")
public interface SupplierProductGateway {

	@RequestMapping(value = "/supplier-product/save", method = RequestMethod.POST,consumes = "application/json")
	public boolean save(@RequestBody SupplierProductDto supplierProduct);
}
