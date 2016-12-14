package com.shangpin.ephub.data.mysql.rule.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.rule.model.mapper.HubBrandModelRuleMapper;

/**
 * <p>Title:HubBrandModelRuleService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:57:14
 */
@Service
public class HubBrandModelRuleService {

	@Autowired
	private HubBrandModelRuleMapper hubBrandModelRuleMapper;
}
