package com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubSupplierValueMappingService {

	@Autowired
	public HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingByType(Integer type) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.createCriteria().andHubValTypeEqualTo(type.byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public HubSupplierValueMappingDto getHubSupplierValueMappingById(Long id){
		return hubSupplierValueMappingGateWay.selectByPrimaryKey(id);
	}
	
	public void updateHubSupplierValueMappingByPrimaryKey(HubSupplierValueMappingDto hubSupplierValueMappingDto){
		hubSupplierValueMappingGateWay.updateByPrimaryKeySelective(hubSupplierValueMappingDto);
	}
	
}
