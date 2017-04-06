package com.shangpin.ephub.product.business.common.hubDic.size;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubSizeDicService extends HubSupplierValueMappingService{

	public List<HubSupplierValueMappingDto> getSupplierCommonSizeValueMapping() {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setOrderByClause("sort_val");
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.createCriteria().andSupplierIdEqualTo("").andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SIZE.getIndex().byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public int countHubSupplierValueMapping(String supplierId, Integer index) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		if(supplierId!=null){
			criteria.createCriteria().andSupplierIdEqualTo(supplierId).andHubValTypeEqualTo(index.byteValue());
		}else{
			criteria.createCriteria().andHubValTypeEqualTo(index.byteValue());
		}
		
		return hubSupplierValueMappingGateWay.countByCriteria(criteria);
	}
}
