package com.shangpin.ephub.data.mysql.dictionary.color.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.color.service.HubColorDicService;

/**
 * <p>Title:HubColorDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:24:58
 */
@RestController
@RequestMapping("/hub-color-dic")
public class HubColorDicController {

	@Autowired
	private HubColorDicService hubColorDicService;
}
