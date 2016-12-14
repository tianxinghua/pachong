package com.shangpin.ephub.data.mysql.picture.spu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.picture.spu.servie.HubSpuPicService;

/**
 * <p>Title:HubSpuPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:55:19
 */
@RestController
@RequestMapping("/hub-spu-pic")
public class HubSpuPicController {

	@Autowired
	private HubSpuPicService hubSpuPicService;
}
