package com.shangpin.ephub.product.business.ui.hub.waitselected.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.service.hub.dto.HubProductIdDto;
import com.shangpin.ephub.product.business.service.hub.impl.HubProductServiceImpl;
import com.shangpin.ephub.product.business.ui.hub.waitselected.dto.HubWaitSelectStateDto;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:SupplierOrderService.java
 * Company: www.shangpin.com
 * @author zhaogenchun
 * @date 2016年12月21日 下午4:06:52
 */
@Service
@Slf4j
public class HubWaitSelectedService {
	
	
	@Autowired
	HubSkuSupplierMappingGateWay hubSkuSupplierMappingGateWay;
	@Autowired 
	HubSpuGateWay hubSpuGateway;
	@Autowired
	HubSkuGateWay hubSkuGateWay;
	@Autowired
	HubProductServiceImpl hubCommonProductServiceImpl;
	public void updateSelectState(List<HubWaitSelectStateDto> list) {
		
		for(HubWaitSelectStateDto dto:list){
			HubSkuSupplierMappingDto HubSkuSupplierMappingDto = new HubSkuSupplierMappingDto();
			HubSkuSupplierMappingDto.setSupplierSelectState((byte)1);
			HubSkuSupplierMappingDto.setSkuSupplierMappingId(dto.getSkuSupplierMappingId());
			hubSkuSupplierMappingGateWay.updateByPrimaryKeySelective(HubSkuSupplierMappingDto);
			
			HubSpuDto hubSpu = new HubSpuDto();
			hubSpu.setSpuSelectState((byte)1);
			hubSpu.setSpuId(dto.getSpuId());
			hubSpu.setUpdateTime(new Date());
			hubSpuGateway.updateByPrimaryKeySelective(hubSpu);
			
			Long spuId = dto.getSpuId();
			Long mappId = dto.getSkuSupplierMappingId();
			Long skuId = dto.getSkuId();
			
			List<HubProductIdDto> skulist = new ArrayList<HubProductIdDto>();
		
			HubProductIdDto skuDto = new HubProductIdDto();
			skuDto.setId(skuId);
			
			List<HubProductIdDto> mapplist = new ArrayList<HubProductIdDto>();
			HubProductIdDto mappDto = new HubProductIdDto();
			mappDto.setId(mappId);
			mapplist.add(mappDto);
			
			skuDto.setSubProduct(mapplist);
			skulist.add(skuDto);
			
			HubProductIdDto spuDto = new HubProductIdDto();
			spuDto.setId(spuId);
			spuDto.setSubProduct(skulist);
			
			try {
				log.info("推送scm参数",spuDto);
				hubCommonProductServiceImpl.sendHubProuctToScm(spuDto);
			} catch (Exception e) {
				log.error("推送scm出错",e);
				e.printStackTrace();
			}
			
		}
	}

}
