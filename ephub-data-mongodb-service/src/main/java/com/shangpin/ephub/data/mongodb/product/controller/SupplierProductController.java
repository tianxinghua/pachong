package com.shangpin.ephub.data.mongodb.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.data.mongodb.common.response.APIRsponse;

/**
 * <p>Title:SupplierProductController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月15日 下午6:35:12
 */
@RestController
@RequestMapping("/supplier-product")
public class SupplierProductController {

	@RequestMapping("/save")
	public APIRsponse save(SupplierProduct supplierProduct){
		return null;
	}
}
