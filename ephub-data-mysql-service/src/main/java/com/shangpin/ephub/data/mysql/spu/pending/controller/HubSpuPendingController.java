package com.shangpin.ephub.data.mysql.spu.pending.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.spu.pending.service.HubSpuPendingService;
/**
 * <p>Title:HubSpuPendingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:19:51
 */
@RestController
@RequestMapping("/hub-spu-pending")
public class HubSpuPendingController {

	@Autowired
	private HubSpuPendingService hubSpuPendingService;
}
