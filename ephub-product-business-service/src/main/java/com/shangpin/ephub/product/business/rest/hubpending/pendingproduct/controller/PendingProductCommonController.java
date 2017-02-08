package com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.controller;

import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.*;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.dto.SpSkuNoDto;
import com.shangpin.ephub.product.business.service.pending.PendingCommonService;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 外部调用 非页面
 */
@RestController
@RequestMapping(value = "/pending-product")
@Slf4j
public class PendingProductCommonController {
	
    @Autowired
	HubSkuPendingGateWay skuPendingGateWay;

    @Autowired
	HubSpuPendingGateWay spuPendingGateWay;

	@Autowired
	HubSkuSupplierMappingGateWay skuSupplierMappingGateWay;

	@Autowired
	HubSkuGateWay hubSkuGateWay;

	@Autowired
	PendingCommonService pendingCommonService;
	
	@RequestMapping(value = "/handle-pending")
	public HubResponse<?> checkSku(@RequestBody PendingSpu dto){
		log.info("receive PendingSpu :{}",dto);
		try {


		} catch (Exception e) {
			log.error("update  error. reason :"+ e.getMessage(),e);
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

			HubSkuDto hubSku = new HubSkuDto();
			hubSku.setSpSkuNo(dto.getSkuNo());
			HubSkuCriteriaDto skuCriteria = new HubSkuCriteriaDto();
			skuCriteria.createCriteria().andSkuNoEqualTo(searchSkuPending.getHubSkuNo());
			HubSkuWithCriteriaDto criteriaWithSku = new HubSkuWithCriteriaDto(hubSku,skuCriteria);
			hubSkuGateWay.updateByCriteriaSelective(criteriaWithSku);
		}




	}

	
}
