package com.shangpin.ephub.product.business.common.hubDic.size;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicRequestDto;

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

	public int countHubSupplierValueMapping(HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto, Integer index) {
		
		Byte type = hubSupplierSizeDicRequestDto.getType();
		String supplierId = hubSupplierSizeDicRequestDto.getSupplierId();
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		if(type==1){
			//通配映射
			criteria.createCriteria().andSupplierIdIsNull().andHubValTypeEqualTo(index.byteValue());
		}else if(type==2){
			//供应商尺码映射
			criteria.createCriteria().andSupplierIdEqualTo(supplierId).andHubValTypeEqualTo(index.byteValue());
		}else if(type==3){
			//全局尺码映射
			criteria.createCriteria().andSupplierIdIsNotNull().andHubValTypeEqualTo(index.byteValue());
		}
		return hubSupplierValueMappingGateWay.countByCriteria(criteria);
	}
	
	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingBySupplierIdAndType(HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto,
			Integer index) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageSize(hubSupplierSizeDicRequestDto.getPageSize());
		criteria.setPageNo(hubSupplierSizeDicRequestDto.getPageNo());
		
		Byte type = hubSupplierSizeDicRequestDto.getType();
		String supplierId = hubSupplierSizeDicRequestDto.getSupplierId();
		if(type==1){
			//通配映射
			criteria.createCriteria().andSupplierIdIsNull().andHubValTypeEqualTo(index.byteValue());
		}else if(type==2&&supplierId!=null){
			//供应商尺码映射
			criteria.createCriteria().andSupplierIdEqualTo(supplierId).andHubValTypeEqualTo(index.byteValue());
		}else if(type==3){
			//全局尺码映射
			criteria.createCriteria().andSupplierIdIsNotNull().andHubValTypeEqualTo(index.byteValue());
		}
		
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}
}
