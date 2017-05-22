package com.shangpin.ephub.product.business.common.hubDic.origin.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubOriginDicService {

	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingByType(Integer type) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.createCriteria().andHubValTypeEqualTo(type.byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public List<HubSupplierValueMappingDto> getSupplierCommonSizeValueMapping() {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setOrderByClause("sort_val");
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.createCriteria().andSupplierIdEqualTo("").andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SIZE.getIndex().byteValue());
		criteria.or(criteria.createCriteria().andSupplierIdIsNull().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SIZE.getIndex().byteValue()));
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingBySupplierIdAndType(String supplierId,
			Integer type) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.setPageNo(1);
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andHubValTypeEqualTo(type.byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public int countHubSupplierValueMapping(String hubVal, String supplierVal) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		if(StringUtils.isNotBlank(hubVal)){
			criteria.createCriteria().andHubValEqualTo(hubVal);	
		}
		if(StringUtils.isNotBlank(supplierVal)){
			criteria.createCriteria().andSupplierValEqualTo(supplierVal);
		}
		criteria.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_ORIGIN.getIndex().byteValue());
		return hubSupplierValueMappingGateWay.countByCriteria(criteria);
	}

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingBySupplierIdAndType(String hubVal,
			String supplierVal, int pageNo, int pageSize) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageNo(pageNo);
		criteria.setPageSize(pageSize);
		criteria.setOrderByClause("update_time desc");
		criteria.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_ORIGIN.getIndex().byteValue());
		if(StringUtils.isNotBlank(hubVal)){
			criteria.createCriteria().andHubValEqualTo(hubVal);	
		}
		if(StringUtils.isNotBlank(supplierVal)){
			criteria.createCriteria().andSupplierValEqualTo(supplierVal);
		}
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public HubSupplierValueMappingDto getHubSupplierValueMappingById(Long id) {
		return hubSupplierValueMappingGateWay.selectByPrimaryKey(id);
	}

	public void updateHubSupplierValueMappingByPrimaryKey(HubSupplierValueMappingDto hubSupplierValueMappingDto) {
		hubSupplierValueMappingGateWay.updateByPrimaryKeySelective(hubSupplierValueMappingDto);
	}

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingBySupplierVal(String supplierVal) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.createCriteria().andSupplierValEqualTo(supplierVal).andHubValTypeEqualTo(SupplierValueMappingType.TYPE_ORIGIN.getIndex().byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public void insertHubSupplierValueMapping(HubSupplierValueMappingDto hubSupplierValueMappingDto) {
		hubSupplierValueMappingGateWay.insertSelective(hubSupplierValueMappingDto);
	}

	public void deleteHubSupplierValueMapping(Long id) {
		hubSupplierValueMappingGateWay.deleteByPrimaryKey(id);
	}
}
