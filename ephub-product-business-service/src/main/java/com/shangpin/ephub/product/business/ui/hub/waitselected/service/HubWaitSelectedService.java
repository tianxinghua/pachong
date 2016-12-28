package com.shangpin.ephub.product.business.ui.hub.waitselected.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.ui.hub.waitselected.dao.HubWaitSelectedRequestDto;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponseDto;

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
	@Autowired
	HubProductService hubSpuService;
	public List<HubWaitSelectedResponseDto> findHubWaitSelectedList(HubWaitSelectedRequestDto dto) {
		
		List<HubWaitSelectedResponseDto> returnList = new ArrayList<HubWaitSelectedResponseDto>();
		HubWaitSelectedResponseDto hubWaitSelectedResponseDto = new HubWaitSelectedResponseDto();
		hubWaitSelectedResponseDto.setBrandName("s");
		returnList.add(hubWaitSelectedResponseDto);
		String supplierNo = dto.getSupplierNo();
		List<HubSkuSupplierMappingDto> hubSkuSuppMapplist = null;
		//如果供应商不为空
		if(StringUtils.isNotBlank(supplierNo)){
			HubSkuSupplierMappingCriteriaDto HubSkuSupplierMappingDto = new HubSkuSupplierMappingCriteriaDto();
			HubSkuSupplierMappingDto.createCriteria().andSupplierIdEqualTo(supplierNo);
			HubSkuSupplierMappingDto.setPageNo(dto.getPageNo());
			HubSkuSupplierMappingDto.setPageSize(dto.getPageSize());
			HubSkuSupplierMappingDto.setFields("sku_no");
			HubSkuSupplierMappingDto.setFields("supplier_id");
			hubSkuSuppMapplist = hubSkuSupplierMappingGateWay.selectByCriteria(HubSkuSupplierMappingDto);
			
			if(hubSkuSuppMapplist!=null&&hubSkuSuppMapplist.size()>0){
				
				for(HubSkuSupplierMappingDto hubSkuSupplierMappingDto:hubSkuSuppMapplist){
					String hubSkuNo = hubSkuSupplierMappingDto.getSkuNo();
					HubSkuCriteriaDto criteria = new HubSkuCriteriaDto();
					criteria.createCriteria().andSkuNoEqualTo(hubSkuNo);
					List<HubSkuDto> list = hubSkuGateWay.selectByCriteria(criteria);
					if(list!=null&&list.size()>0){
						for(HubSkuDto sku:list){
							String spuNo = sku.getSpuNo();
							HubSpuCriteriaDto hubSpuCriteriaDto = new HubSpuCriteriaDto();
							hubSpuCriteriaDto.createCriteria().andSpuNoEqualTo(spuNo);
							List<HubSpuDto> listSpu = hubSpuGateway.selectByCriteria(hubSpuCriteriaDto);
							if(listSpu!=null&&listSpu.size()>0){
								for(HubSpuDto spu:listSpu){
									
								}
							}
						}
					}
				}
			}
		
		}
		List<HubSpuDto> hubSpuList = hubSpuService.findHubSpuList(dto);
//		hubSpuService.find
		return returnList;
	}

}
