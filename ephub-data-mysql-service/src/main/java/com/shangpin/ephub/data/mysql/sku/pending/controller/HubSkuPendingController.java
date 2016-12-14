package com.shangpin.ephub.data.mysql.sku.pending.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.sku.pending.service.HubSkuPendingService;

/**
 * <p>Title:HubSkuPendingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:12:44
 */
@RestController
@RequestMapping("/hub-sku-pending")
public class HubSkuPendingController {

	@Autowired
	private HubSkuPendingService hubSkuPendingService;
}
