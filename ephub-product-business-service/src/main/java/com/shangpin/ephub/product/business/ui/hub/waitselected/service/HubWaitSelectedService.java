package com.shangpin.ephub.product.business.ui.hub.waitselected.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.rest.hubpending.spu.dto.HubPendingSpuDto;
import com.shangpin.ephub.product.business.ui.hub.waitselected.dto.HubWaitSelectStateDto;

/**
 * <p>
 * Title:SupplierOrderService.java
 * Company: www.shangpin.com
 * @author zhaogenchun
 * @date 2016年12月21日 下午4:06:52
 */
@Service
public class HubWaitSelectedService {
	
	
	@Autowired
	HubSkuSupplierMappingGateWay hubSkuSupplierMappingGateWay;
	@Autowired 
	HubSpuGateWay hubSpuGateway;
	@Autowired
	HubSkuGateWay hubSkuGateWay;
	public void updateSelectState(List<HubWaitSelectStateDto> list) {
		
		for(HubWaitSelectStateDto dto:list){
			HubSkuSupplierMappingDto HubSkuSupplierMappingDto = new HubSkuSupplierMappingDto();
			HubSkuSupplierMappingDto.setSupplierSelectState((byte)1);
			HubSkuSupplierMappingDto.setSkuSupplierMappingId(dto.getSkuSupplierMappingId());
			hubSkuSupplierMappingGateWay.updateByPrimaryKeySelective(HubSkuSupplierMappingDto);
			
			HubSpuWithCriteriaDto HubSpuWithCriteriaDto = new HubSpuWithCriteriaDto();
			HubSpuCriteriaDto HubSpuCriteriaDto = new HubSpuCriteriaDto();
			HubSpuCriteriaDto.createCriteria().andSpuNoEqualTo(dto.getSpuNo());
			HubSpuDto hubSpu = new HubSpuDto();
			hubSpu.setSpuSelectState((byte)1);
			hubSpu.setUpdateTime(new Date());
			HubSpuWithCriteriaDto.setHubSpu(hubSpu);
			HubSpuWithCriteriaDto.setCriteria(HubSpuCriteriaDto);
			hubSpuGateway.updateByCriteriaSelective(HubSpuWithCriteriaDto);
		}
	}

}
