package com.shangpin.ephub.data.mysql.dictionary.categroy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.categroy.service.HubSupplierCategroyDicService;

/**
 * <p>Title:HubSupplierCategroyDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:19:55
 */
@RestController
@RequestMapping("/hub-supplier-categroy-dic")
public class HubSupplierCategroyDicController {

	@Autowired
	private HubSupplierCategroyDicService hubSupplierCategroyDicService;
}
