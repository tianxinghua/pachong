package com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.dto.SpSkuNoDto;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 外部调用 非页面
 */
@RestController
@RequestMapping(value = "/pendingproduct")
@Slf4j
public class HubPendingProductController {
	
    @Autowired
	HubSkuPendingGateWay skuPendingGateWay;

    @Autowired
	HubSpuPendingGateWay spuPendingGateWay;

	@Autowired
	HubSkuSupplierMappingGateWay skuSupplierMappingGateWay;

	@Autowired
	HubSkuGateWay hubSkuGateWay;
	
	@RequestMapping(value = "/setspskuno")
	public HubResponse<?> checkSku(@RequestBody SpSkuNoDto dto){
		log.info("receive parameters :{}",dto);
		try {
			this.updatePendingSku(dto);

		} catch (Exception e) {
			log.error("update spsku error. reason :"+ e.getMessage(),e);
			return HubResponse.errorResp(e.getMessage());
		}
		return HubResponse.successResp(true);
	}

	private void updatePendingSku(SpSkuNoDto dto) throws Exception{
		//写入尚品的SKUno
		HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
		hubSkuPending.setSpSkuNo(dto.getSkuNo());
		hubSkuPending.setMemo(dto.getErrorReason());
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo())
				.andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());



		HubSkuPendingWithCriteriaDto skuCritria = new HubSkuPendingWithCriteriaDto(hubSkuPending,criteria);
		skuPendingGateWay.updateByCriteriaSelective(skuCritria);

		List<HubSkuPendingDto> hubSkuPendingDtos = skuPendingGateWay.selectByCriteria(criteria);
		HubSkuPendingDto  searchSkuPending  = null;
		if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
			searchSkuPending = hubSkuPendingDtos.get(0);
		}

		//更新SKUSUPPLIERMAPPING 的状态

		HubSkuSupplierMappingCriteriaDto criteriaDto = new HubSkuSupplierMappingCriteriaDto();
		criteriaDto.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo()).andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());

		HubSkuSupplierMappingDto hubSkuSupplierMapping = new HubSkuSupplierMappingDto();
		if(dto.getSign()==1){
			hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTED.getIndex()).byteValue());
		}else{
			hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTE_FAIL.getIndex()).byteValue());
			hubSkuSupplierMapping.setMemo(dto.getErrorReason());
		}

		HubSkuSupplierMappingWithCriteriaDto skumappingCritria = new HubSkuSupplierMappingWithCriteriaDto(hubSkuSupplierMapping,criteriaDto);
		skuSupplierMappingGateWay.updateByCriteriaSelective(skumappingCritria);

		//修改hub_sku中的商品sku编号
		if(null!=searchSkuPending){
//			HubSkuCriteriaDto criteriaSku = new HubSkuCriteriaDto();
			HubSkuDto hubSku = new HubSkuDto();
			hubSku.setSpSkuNo(dto.getSkuNo());
			HubSkuCriteriaDto skuCriteria = new HubSkuCriteriaDto();
			skuCriteria.createCriteria().andSkuNoEqualTo(searchSkuPending.getHubSkuNo());
			HubSkuWithCriteriaDto criteriaWithSku = new HubSkuWithCriteriaDto(hubSku,skuCriteria);
			hubSkuGateWay.updateByCriteriaSelective(criteriaWithSku);
		}

//		HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
//
//		HubSpuPendingCriteriaDto criteriaSpu = new HubSpuPendingCriteriaDto();
//
//		HubSpuPendingWithCriteriaDto spuCritria = new HubSpuPendingWithCriteriaDto(hubSpuPending,criteriaSpu);
//		spuPendingGateWay.updateByCriteriaSelective(spuCritria);


	}

	
}
