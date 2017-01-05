package com.shangpin.ephub.product.business.ui.hub.waitselected.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
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
 * Title:SupplierOrderService.java Company: www.shangpin.com
 * 
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

		if(list==null||list.size()==0)
			return ;
		Map<Long, Map<Long, Map<Long, String>>> spuIdMap = new HashMap<Long, Map<Long, Map<Long, String>>>();
		for (HubWaitSelectStateDto dto : list) {

			HubSkuSupplierMappingDto HubSkuSupplierMappingDto = new HubSkuSupplierMappingDto();
			HubSkuSupplierMappingDto.setSupplierSelectState((byte)SupplierSelectState.SELECTING.getIndex());
			HubSkuSupplierMappingDto.setSkuSupplierMappingId(dto.getSkuSupplierMappingId());
			hubSkuSupplierMappingGateWay.updateByPrimaryKeySelective(HubSkuSupplierMappingDto);

			Long spuId = dto.getSpuId();
			Long mappId = dto.getSkuSupplierMappingId();
			Long skuId = dto.getSkuId();

			if (spuIdMap.containsKey(spuId)) {
				Map<Long, Map<Long, String>> skuMap = spuIdMap.get(spuId);
				if (skuMap.containsKey(skuId)) {
					Map<Long, String> mappMap = skuMap.get(skuId);
					mappMap.put(mappId, null);
					skuMap.put(skuId, mappMap);
					spuIdMap.put(spuId, skuMap);
				} else {
					Map<Long, String> mappMap = new HashMap<Long, String>();
					mappMap.put(mappId, null);
					skuMap.put(skuId, mappMap);
					spuIdMap.put(spuId, skuMap);
				}

			} else {
				Map<Long, String> mappMap = new HashMap<Long, String>();
				mappMap.put(mappId, null);
				Map<Long, Map<Long, String>> skuMap = new HashMap<Long, Map<Long, String>>();
				skuMap.put(skuId, mappMap);
				spuIdMap.put(spuId, skuMap);
			}

		}
		for (Map.Entry<Long, Map<Long, Map<Long, String>>> spuEntry : spuIdMap.entrySet()) {
			HubProductIdDto spu = new HubProductIdDto();
			Long spuId = spuEntry.getKey();
			spu.setId(spuId);

			List<HubProductIdDto> skulist = new ArrayList<HubProductIdDto>();
			Map<Long, Map<Long, String>> skuMap = spuEntry.getValue();
			for (Map.Entry<Long, Map<Long, String>> skuEntry : skuMap.entrySet()) {
				HubProductIdDto sku = new HubProductIdDto();
				Long skuId = skuEntry.getKey();
				sku.setId(skuId);
				Map<Long, String> mappMap = skuEntry.getValue();
				List<HubProductIdDto> mapplist = new ArrayList<HubProductIdDto>();
				for (Map.Entry<Long, String> mappEntry : mappMap.entrySet()) {
					HubProductIdDto mapp = new HubProductIdDto();
					Long mappId = mappEntry.getKey();
					mapp.setId(mappId);
					mapplist.add(mapp);
				}
				sku.setSubProduct(mapplist);
				skulist.add(sku);
			}
			spu.setSubProduct(skulist);
			try {
				log.info("推送scm参数{}", spu);
				hubCommonProductServiceImpl.sendHubProuctToScm(spu);
			} catch (Exception e) {
				log.error("推送scm出错{}", e);
				e.printStackTrace();
			}

		}
	}

	public void updateProductDetail(List<HubWaitSelectStateDto> dto) {
		
	}

}
