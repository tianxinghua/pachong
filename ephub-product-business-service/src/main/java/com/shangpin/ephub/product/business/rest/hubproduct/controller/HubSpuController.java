package com.shangpin.ephub.product.business.rest.hubproduct.controller;

import com.shangpin.ephub.product.business.rest.hubproduct.dto.SpuNoDTO;
import com.shangpin.ephub.product.business.rest.hubproduct.dto.SpuNoQueryDTO;
import com.shangpin.ephub.product.business.service.hub.impl.HubProductHandler;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HUB入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/hub-spu")
@Slf4j
public class HubSpuController {
	
	@Autowired
	HubProductHandler hubProductHandler;
	
	@RequestMapping(value = "/get-spuno")
	public HubResponse getSpuNo(@RequestBody SpuNoQueryDTO dto){
		String spuNo="",skuNo="";
		SpuNoDTO spuNoDTO = new SpuNoDTO();
		spuNo = hubProductHandler.getHubSpuNo();
		spuNoDTO.setSpuNo(spuNo);
		if(dto.getTotal()>0){
            skuNo = hubProductHandler.getHubSkuNo(spuNo,dto.getTotal());
			spuNoDTO.setSkuNo(skuNo);
		}
		return HubResponse.successResp(spuNoDTO);
	}


}
