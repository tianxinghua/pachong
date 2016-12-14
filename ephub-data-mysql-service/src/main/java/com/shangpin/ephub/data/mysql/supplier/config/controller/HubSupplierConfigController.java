package com.shangpin.ephub.data.mysql.supplier.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.supplier.config.servcie.HubSupplierConfigService;

/**
 * <p>Title:HubSupplierConfigController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:23:34
 */
@RestController
@RequestMapping("/hub-supplier-config")
public class HubSupplierConfigController {

	@Autowired
	private HubSupplierConfigService hubSupplierConfigService;
}
