package com.shangpin.ephub.product.business.rest.redis.brand.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.product.business.common.hubDic.brand.HubBrandDicService;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubBrandMappingMapController.java </p>
 * <p>Description: HUB、SUPPLIER品牌字典redis缓存</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2017年07月04日 下午3:52:56
 */
@RestController
@RequestMapping(value = "/hub-supplier-brand-mapping-map")
@Slf4j
public class HubSupplierBrandMappingMapController {
	
	@Autowired
	IShangpinRedis shangpinRedis;
	@Autowired
	HubBrandDicService hubBrandDicService;
	
	@RequestMapping(value = "/supplier-brand-map")
	public Map<String,String> getSupplierBrandMap(@RequestBody BrandModelDto dto){
		Map<String, String> supplierBrandMap = null;
		try{
			supplierBrandMap = shangpinRedis
					.hgetAll(ConstantProperty.REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY);
			if (supplierBrandMap == null || supplierBrandMap.size() < 1) {
				log.info("redis为空");
				supplierBrandMap = new HashMap<>();
				List<HubBrandDicDto> brandDicDtos = hubBrandDicService.getBrand();
				for (HubBrandDicDto hubBrandDicDto : brandDicDtos) {
					if(StringUtils.isNotBlank(hubBrandDicDto.getSupplierBrand())&&StringUtils.isNotBlank(hubBrandDicDto.getHubBrandNo())){
						supplierBrandMap.put(hubBrandDicDto.getSupplierBrand().trim().toUpperCase(),
								hubBrandDicDto.getHubBrandNo());	
					}
				}
				if (supplierBrandMap.size() > 0) {
					shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY, supplierBrandMap);
					shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY,
							ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
				}
			}
		}catch(Exception e){
			
		}
		return supplierBrandMap;
	}

}
