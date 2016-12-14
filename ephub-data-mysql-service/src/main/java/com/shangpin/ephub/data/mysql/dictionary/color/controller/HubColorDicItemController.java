package com.shangpin.ephub.data.mysql.dictionary.color.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.color.service.HubColorDicItemService;
/**
 * <p>Title:HubColorDicItemController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:28:19
 */
@RestController
@RequestMapping("/hub-color-dic-item")
public class HubColorDicItemController {

	@Autowired
	private HubColorDicItemService hubColorDicItemService;
}
