package com.shangpin.ephub.data.mysql.mapping.sku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.mapping.sku.service.HubSkuSupplierMappingService;

/**
 * <p>Title:HubSkuSupplierMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:42:50
 */
@RestController
@RequestMapping("/hub-sku-supplier-mapping")
public class HubSkuSupplierMappingController {

	@Autowired
	private HubSkuSupplierMappingService hubSkuSupplierMappingService;
}
