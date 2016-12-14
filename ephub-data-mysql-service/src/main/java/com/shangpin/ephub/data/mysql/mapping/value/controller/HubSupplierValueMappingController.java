package com.shangpin.ephub.data.mysql.mapping.value.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.mapping.value.service.HubSupplierValueMappingService;
/**
 * <p>Title:HubSupplierValueMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:47:53
 */
@RestController
@RequestMapping("/hub-supplier-value-mapping")
public class HubSupplierValueMappingController {

	@Autowired
	private HubSupplierValueMappingService hubSupplierValueMappingService;
}
