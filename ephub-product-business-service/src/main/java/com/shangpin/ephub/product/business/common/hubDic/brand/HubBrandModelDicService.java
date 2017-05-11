package com.shangpin.ephub.product.business.common.hubDic.brand;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.rule.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubBrandModelDicService {

	@Autowired
	private HubBrandDicGateway brandDicGateway;
	@Autowired
	HubSupplierBrandDicGateWay supplierBrandDicGateWay;
	@Autowired
	HubBrandModelRuleGateWay hubBrandModelRuleGateWay;

	public HubSupplierBrandDicDto getHubSupplierBrand(String supplierId, String supplierBrandName) {
		HubSupplierBrandDicCriteriaDto criteria = new HubSupplierBrandDicCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierBrandEqualTo(supplierBrandName);
		List<HubSupplierBrandDicDto> hubSupplierBrandDicDtos = supplierBrandDicGateWay.selectByCriteria(criteria);
		if (null != hubSupplierBrandDicDtos && hubSupplierBrandDicDtos.size() > 0) {
			return hubSupplierBrandDicDtos.get(0);
		} else {
			return null;
		}
	}

	public void saveBrand(String supplierId, String supplierBrandName) throws Exception {

		if (null != getHubSupplierBrand(supplierId, supplierBrandName)) {// 重复不做处理
			return;
		}
		HubSupplierBrandDicDto supplierBrandDicDto = new HubSupplierBrandDicDto();
		supplierBrandDicDto.setSupplierId(supplierId);
		supplierBrandDicDto.setSupplierBrand(supplierBrandName);
		supplierBrandDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		supplierBrandDicDto.setDataState(DataState.NOT_DELETED.getIndex());
		try {
			supplierBrandDicGateWay.insert(supplierBrandDicDto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public List<HubBrandDicDto> getBrand() throws Exception {
		HubBrandDicCriteriaDto criteria = new HubBrandDicCriteriaDto();
		criteria.setPageNo(1);
		criteria.setPageSize(ConstantProperty.MAX_BRANDK_MAPPING_QUERY_NUM);
		return brandDicGateway.selectByCriteria(criteria);
	}

	public int countBrandModelByHubCategoryAndHubBrandNo(String hubBrandNo, String hubCategoryNo) {
		HubBrandModelRuleCriteriaDto hubBrandModelRuleCriteriaDto = new HubBrandModelRuleCriteriaDto();
		if(StringUtils.isNotBlank(hubBrandNo)){
			hubBrandModelRuleCriteriaDto.createCriteria().andHubBrandNoEqualTo(hubBrandNo);	
		}
		if(StringUtils.isNotBlank(hubCategoryNo)){
			if("specialCategory".equals(hubCategoryNo)){
				hubBrandModelRuleCriteriaDto.createCriteria().andHubCategoryNoIsNotNull();	
			}else if("allCategory".equals(hubCategoryNo)){
				hubBrandModelRuleCriteriaDto.createCriteria().andHubCategoryNoIsNull();
			}
		}
		return hubBrandModelRuleGateWay.countByCriteria(hubBrandModelRuleCriteriaDto);
	}

	public List<HubBrandModelRuleDto> getBrandModelListByHubCategoryAndHubBrandNo(String hubBrandNo,
			String hubCategoryNo, int pageNo, int pageSize) {
		HubBrandModelRuleCriteriaDto hubBrandModelRuleCriteriaDto = new HubBrandModelRuleCriteriaDto();
		if(StringUtils.isNotBlank(hubBrandNo)){
			hubBrandModelRuleCriteriaDto.createCriteria().andHubBrandNoEqualTo(hubBrandNo);	
		}
		if(StringUtils.isNotBlank(hubCategoryNo)){
			if("specialCategory".equals(hubCategoryNo)){
				hubBrandModelRuleCriteriaDto.createCriteria().andHubCategoryNoIsNotNull();	
			}else if("allCategory".equals(hubCategoryNo)){
				hubBrandModelRuleCriteriaDto.createCriteria().andHubCategoryNoIsNull();
			}
		}
		hubBrandModelRuleCriteriaDto.setPageNo(pageNo);
		hubBrandModelRuleCriteriaDto.setPageSize(pageSize);
		hubBrandModelRuleCriteriaDto.setOrderByClause("update_time desc");
		return hubBrandModelRuleGateWay.selectByCriteria(hubBrandModelRuleCriteriaDto);
	}

	public HubBrandModelRuleDto getHubBrandModelRuleById(Long id) {
		return hubBrandModelRuleGateWay.selectByPrimaryKey(id);
	}

	public void deleteHubBrandModelRuleById(Long id) {
		hubBrandModelRuleGateWay.deleteByPrimaryKey(id);
	}

	public void insertHubBrandModelRule(HubBrandModelRuleDto dicDto) {
		hubBrandModelRuleGateWay.insertSelective(dicDto);		
	}

	public void updateHubBrandModelRuleById(HubBrandModelRuleDto dicDto) {
		hubBrandModelRuleGateWay.updateByPrimaryKeySelective(dicDto);
	}

}
