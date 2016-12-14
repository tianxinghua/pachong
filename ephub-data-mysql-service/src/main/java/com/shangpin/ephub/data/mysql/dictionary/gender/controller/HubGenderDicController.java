package com.shangpin.ephub.data.mysql.dictionary.gender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.gender.service.HubGenderDicService;
/**
 * <p>Title:HubGenderDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:30:46
 */
@RestController
@RequestMapping("/hub-gender-dic")
public class HubGenderDicController {

	@Autowired
	private HubGenderDicService hubGenderDicService;
}
