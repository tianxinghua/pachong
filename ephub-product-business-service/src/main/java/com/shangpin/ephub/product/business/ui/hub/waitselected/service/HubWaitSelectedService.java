package com.shangpin.ephub.product.business.ui.hub.waitselected.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.product.business.ui.hub.waitselected.dao.HubWaitSelectedRequestDto;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponseDto;

/**
 * <p>
 * Title:SupplierOrderService.java
 * Company: www.shangpin.com
 * @author zhaogenchun
 * @date 2016年12月21日 下午4:06:52
 */
@SuppressWarnings("rawtypes")
@Service
public class HubWaitSelectedService {
	
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	HubSkuSupplierMappingGateWay hubSkuSupplierMappingGateWay;
	@Autowired 
	HubSpuGateWay spuImportGateway;
	@Autowired
	HubProductService hubSpuService;
	public List<HubWaitSelectedResponseDto> findHubWaitSelectedList(HubWaitSelectedRequestDto dto) {
		
		String supplierId = dto.getSupplierNo();
		List<HubSkuSupplierMappingDto> hubSkuSuppMapplist = null;
		//如果供应商不为空
		if(StringUtils.isNotBlank(supplierId)){
			HubSkuSupplierMappingCriteriaDto HubSkuSupplierMappingDto = new HubSkuSupplierMappingCriteriaDto();
			HubSkuSupplierMappingDto.createCriteria().andSupplierIdEqualTo(supplierId);
			HubSkuSupplierMappingDto.setFields("sku_no");
			HubSkuSupplierMappingDto.setFields("supplier_id");
			hubSkuSuppMapplist = hubSkuSupplierMappingGateWay.selectByCriteria(HubSkuSupplierMappingDto);
			
			for(HubSkuSupplierMappingDto hubSkuSupplierMappingDto:hubSkuSuppMapplist){
				long hubSkuNo = hubSkuSupplierMappingDto.getSkuNo();
			}
		}
		List<HubSpuDto> hubSpuList = hubSpuService.findHubSpuList(dto);
//		hubSpuService.find
		return null;
	}

}
