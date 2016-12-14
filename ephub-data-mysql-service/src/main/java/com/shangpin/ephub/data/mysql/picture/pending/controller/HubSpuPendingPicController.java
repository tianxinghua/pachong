package com.shangpin.ephub.data.mysql.picture.pending.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.picture.pending.service.HubSpuPendingPicService;
/**
 * <p>Title:HubSpuPendingPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:53:21
 */
@RestController
@RequestMapping("/hub-spu-pending-pic")
public class HubSpuPendingPicController {

	@Autowired
	private HubSpuPendingPicService hubSpuPendingPicService;
}
