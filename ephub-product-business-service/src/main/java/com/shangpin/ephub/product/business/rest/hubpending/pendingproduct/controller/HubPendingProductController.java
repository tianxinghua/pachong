package com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.controller;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.dto.SpSkuNoDto;
import com.shangpin.ephub.product.business.rest.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.product.business.rest.hubpending.sku.service.HubPendingSkuCheckService;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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




		HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
		hubSkuPending.setSpSkuNo(dto.getSkuNo());
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo())
				.andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());

		HubSkuPendingWithCriteriaDto skuCritria = new HubSkuPendingWithCriteriaDto(hubSkuPending,criteria);
		skuPendingGateWay.updateByCriteriaSelective(skuCritria);


//		HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
//
//		HubSpuPendingCriteriaDto criteriaSpu = new HubSpuPendingCriteriaDto();
//
//		HubSpuPendingWithCriteriaDto spuCritria = new HubSpuPendingWithCriteriaDto(hubSpuPending,criteriaSpu);
//		spuPendingGateWay.updateByCriteriaSelective(spuCritria);


	}

	
}
