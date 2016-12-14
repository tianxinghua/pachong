package com.shangpin.ephub.data.mysql.sku.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.sku.hub.service.HubSkuService;

/**
 * <p>Title:HubSkuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:08:37
 */
@RestController
@RequestMapping("/hub-sku")
public class HubSkuController {

	@Autowired
	private HubSkuService hubSkuService;
}
