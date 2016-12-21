package com.shangpin.ephub.product.business.rest.model.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
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
	/**
	 * 根据查询条件标准查询记录
	 * @param criteria 查询条件标准
	 * @return 查询到的记录列表
	 */
	public List<HubBrandModelRuleDto> findByCriteria(HubBrandModelRuleCriteriaDto criteria){
		return hubBrandModelRuleGateWay.selectByCriteria(criteria);
	}
}
