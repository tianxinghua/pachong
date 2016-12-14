package com.shangpin.ephub.data.mysql.spu.supplier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.spu.supplier.servcie.HubSupplierSpuService;
/**
 * <p>Title:HubSupplierSpuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:21:45
 */
@RestController
@RequestMapping("/hub-supplier-spu")
public class HubSupplierSpuController {

	@Autowired
	private HubSupplierSpuService hubSupplierSpuService;
}
