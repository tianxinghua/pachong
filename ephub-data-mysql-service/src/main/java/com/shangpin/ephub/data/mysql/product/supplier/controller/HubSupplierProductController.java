package com.shangpin.ephub.data.mysql.product.supplier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.product.supplier.bean.HubSupplierProductRequestWithPage;
import com.shangpin.ephub.data.mysql.product.supplier.po.HubSupplierProduct;
import com.shangpin.ephub.data.mysql.product.supplier.service.HubSupplierProductService;

/**
 * <p>Title:HubSupplierSkuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:15:43
 */
@RestController
@RequestMapping("/hub-supplier-product")
public class HubSupplierProductController {

	@Autowired
	private HubSupplierProductService hubSupplierProductService;
	@RequestMapping(value = "/select-product-page")
    public List<HubSupplierProduct> selectByCriteriaWithRowbounds(@RequestBody HubSupplierProductRequestWithPage request){
    	return hubSupplierProductService.selectHubSupplierProductByPage(request);
    }
	
	@RequestMapping(value = "/count")
    public int count(@RequestBody HubSupplierProductRequestWithPage request){
    	return hubSupplierProductService.count(request);
    }
	
	@RequestMapping(value = "/insert")
    public Long insert(@RequestBody HubSupplierProduct product){
    	 return hubSupplierProductService.insert(product);
    }
}
