package com.shangpin.ephub.data.mysql.sku.supplier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.sku.supplier.service.HubSupplierSkuService;

/**
 * <p>Title:HubSupplierSkuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:15:43
 */
@RestController
@RequestMapping("/hub-supplier-sku")
public class HubSupplierSkuController {

	@Autowired
	private HubSupplierSkuService hubSupplierSkuService;
}
