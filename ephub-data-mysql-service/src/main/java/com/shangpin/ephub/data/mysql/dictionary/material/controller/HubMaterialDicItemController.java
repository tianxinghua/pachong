package com.shangpin.ephub.data.mysql.dictionary.material.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.material.service.HubMaterialDicItemService;

/**
 * 
 * <p>Title:HubMaterialDicItemController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:33:00
 */
@RestController
@RequestMapping("/hub-material-dic-item")
public class HubMaterialDicItemController {

	@Autowired
	private HubMaterialDicItemService hubMaterialDicItemService;
}
