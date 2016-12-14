package com.shangpin.ephub.data.mysql.rule.pic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.rule.pic.service.HubSupplierPicRuleService;

/**
 * <p>Title:HubSupplierPicRuleController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:58:33
 */
@RestController
@RequestMapping("/hub-supplier-pic-rule")
public class HubSupplierPicRuleController {

	@Autowired
	private HubSupplierPicRuleService hubSupplierPicRuleService;
}
