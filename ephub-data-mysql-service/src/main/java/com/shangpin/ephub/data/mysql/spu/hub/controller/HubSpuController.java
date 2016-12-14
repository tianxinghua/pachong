package com.shangpin.ephub.data.mysql.spu.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.spu.hub.service.HubSpuService;
/**
 * <p>Title:HubSpuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:18:04
 */
@RestController
@RequestMapping("/hub-spu")
public class HubSpuController {

	@Autowired
	private HubSpuService hubSpuService;
}
