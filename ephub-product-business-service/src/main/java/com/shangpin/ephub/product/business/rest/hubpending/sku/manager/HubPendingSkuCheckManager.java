package com.shangpin.ephub.product.business.rest.hubpending.sku.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicGateWay;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;

/**
 * <p>Title:HubBrandModelRuleManager.java </p>
 * <p>Description: hub商品校验资源管理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午4:17:15
 */
@Component
public class HubPendingSkuCheckManager {

	@Autowired
	HubBrandDicGateway hubBrandDicGateway;
	
	@Autowired
	HubColorDicGateWay hubColorDicGateway;
	@Autowired
	HubSeasonDicGateWay hubSeasonDicGateWay;
	@Autowired
	HubGenderDicGateWay hubGenderDicGateWay;
	/**
	 * 根据查询条件标准查询记录
	 * @param criteria 查询条件标准
	 * @return 查询到的记录列表
	 */
	public List<HubBrandDicDto> findBrandByCriteria(HubBrandDicCriteriaDto criteria){
		return hubBrandDicGateway.selectByCriteria(criteria);
	}
	public List<HubColorDicDto> findColorByCriteria(HubColorDicCriteriaDto hubColorDicCriteriaDto) {
		return hubColorDicGateway.selectByCriteria(hubColorDicCriteriaDto);
	}
	public List<HubSeasonDicDto> findSeasonByCriteria(HubSeasonDicCriteriaDto hubSeasonDicCriteriaDto) {
		// TODO Auto-generated method stub
		return hubSeasonDicGateWay.selectByCriteria(hubSeasonDicCriteriaDto);
	}
	public List<HubGenderDicDto> findGenderByCriteria(HubGenderDicCriteriaDto hubGenderDicCriteriaDto) {
		return hubGenderDicGateWay.selectByCriteria(hubGenderDicCriteriaDto);
	}
}
