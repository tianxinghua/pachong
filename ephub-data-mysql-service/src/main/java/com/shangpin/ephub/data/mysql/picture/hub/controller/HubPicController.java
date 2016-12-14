package com.shangpin.ephub.data.mysql.picture.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.picture.hub.service.HubPicService;

/**
 * <p>Title:HubPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:50:53
 */
@RestController
@RequestMapping("/hub-pic")
public class HubPicController {

	@Autowired
	private HubPicService hubPicService;
}
