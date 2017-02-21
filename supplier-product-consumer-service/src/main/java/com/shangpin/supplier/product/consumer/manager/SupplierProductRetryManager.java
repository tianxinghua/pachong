package com.shangpin.supplier.product.consumer.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;

/**
 * <p>Title:SupplierProductPictureManager.java </p>
 * <p>Description: 负责外部系统调用的管理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2017年1月2日 下午12:44:02
 */
@Component
public class SupplierProductRetryManager {
	
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;
	public int getSupplierProductRetryCount(HubSupplierSpuCriteriaDto criteria) {
		return hubSupplierSpuGateWay.countByCriteria(criteria);
	}
	public List<HubSupplierSpuDto> findSupplierProduct(HubSupplierSpuCriteriaDto criteria) {
		return hubSupplierSpuGateWay.selectByCriteria(criteria);
	}
	public List<HubSupplierValueMappingDto> findHubSupplierValueMapping(HubSupplierValueMappingCriteriaDto criteria){
	
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}
	public void updateSupplierSpu(HubSupplierSpuDto hubSupplierSpu) {
		hubSupplierSpuGateWay.updateByPrimaryKeySelective(hubSupplierSpu);		
	}

	public void insert(HubSupplierValueMappingDto dto){
		hubSupplierValueMappingGateWay.insert(dto);
	}
}
