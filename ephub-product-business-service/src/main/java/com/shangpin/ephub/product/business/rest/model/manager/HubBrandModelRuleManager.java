package com.shangpin.ephub.product.business.rest.model.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.rule.gateway.HubBrandModelRuleGateWay;

/**
 * <p>Title:HubBrandModelRuleManager.java </p>
 * <p>Description: 品牌型号规则资源管理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午4:17:15
 */
@Component
public class HubBrandModelRuleManager {

	@Autowired
	private HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
	
}
