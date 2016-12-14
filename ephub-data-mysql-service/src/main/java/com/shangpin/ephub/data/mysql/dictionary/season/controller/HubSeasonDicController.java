package com.shangpin.ephub.data.mysql.dictionary.season.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.season.service.HubSeasonDicService;
/**
 * <p>Title:HubSeasonDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:37:10
 */
@RestController
@RequestMapping("/hub-season-dic")
public class HubSeasonDicController {

	@Autowired
	private HubSeasonDicService hubSeasonDicService;
}
