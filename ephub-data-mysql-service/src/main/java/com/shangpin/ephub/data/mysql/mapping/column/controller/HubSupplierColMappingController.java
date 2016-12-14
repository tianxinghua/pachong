package com.shangpin.ephub.data.mysql.mapping.column.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.mapping.column.service.HubSupplierColMappingService;
/**
 * <p>Title:HubSupplierColMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:39:37
 */
@RestController
@RequestMapping("/hub-supplier-col-mapping")
public class HubSupplierColMappingController {

	@Autowired
	private HubSupplierColMappingService hubSupplierColMappingService;
}
