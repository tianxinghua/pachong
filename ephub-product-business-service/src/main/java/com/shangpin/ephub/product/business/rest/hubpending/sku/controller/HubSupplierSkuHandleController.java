package com.shangpin.ephub.product.business.rest.hubpending.sku.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.product.business.rest.hubpending.sku.dto.SupplierSkuRequestDto;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HubPendingSpu入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author wangchao
 * @date 2017年10月19 下午3:52:56
 */
@RestController
@RequestMapping(value = "/supplier-sku-handle")
@Slf4j
public class HubSupplierSkuHandleController {
	
	@Autowired
	HubSupplierSkuGateWay hubSupplierSkuGateWay;
	
	@RequestMapping(value="/supplier-sku",method = RequestMethod.POST)
	public String handleHubPendingSku(@RequestBody SupplierSkuRequestDto request){
		List<HubSupplierSkuDto> hubSupplierSkuDtolists = new ArrayList<>();
		try {
			HubSupplierSkuCriteriaDto dto = new HubSupplierSkuCriteriaDto();
			dto.createCriteria().andSupplierIdEqualTo(request.getSupplierId()).andSupplierSkuIdEqualTo(Long.parseLong(request.getSkuId()));
			hubSupplierSkuDtolists = hubSupplierSkuGateWay.selectByCriteria(dto);
			if(hubSupplierSkuDtolists==null||hubSupplierSkuDtolists.size()==0) {
				return "";
			}
		} catch (Exception e) {
			log.error("====supplierSku处理时发生异常：{}",e);
			e.printStackTrace();
		}
		return hubSupplierSkuDtolists.get(0).getSupplyPrice().toString();
	}
	
}