package com.shangpin.ephub.product.business.rest.hubpending.sku.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubSkuCheckDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: hua商品校验实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubPendingSkuCheckService {
	
	@Autowired
	HubCheckService hubCheckService;
	
	public HubPendingSkuCheckResult checkHubPendingSku(HubSkuCheckDto hubProduct){
		StringBuffer str = new StringBuffer();
		HubPendingSkuCheckResult result = new HubPendingSkuCheckResult();
		String message = null;
		boolean flag = false;
		
		if("排除".equals(hubProduct.getSizeType())){
			result.setPassing(false);
			result.setSizeType("排除");
			result.setSizeValue(hubProduct.getSkuSize());
			result.setMessage("尺码排除");
			result.setFilter(true);
        	return result;
		}
		
		 if("尺寸".equals(hubProduct.getSpecificationType())||"尺寸".equals(hubProduct.getSizeType())){
				flag = true;
				message = "尺码类型为尺寸不校验";
		 }else if("尺码".equals(hubProduct.getSpecificationType())||StringUtils.isBlank(hubProduct.getSpecificationType())){
			if(StringUtils.isNotBlank(hubProduct.getSizeType())){
				result = hubCheckService.hubSizeExist(hubProduct.getCategoryNo(),hubProduct.getBrandNo(),hubProduct.getSizeType(),hubProduct.getSkuSize());
				return result;
			}else{
				flag = false;
				message = "尺码类型为空";
			}
		}else{
			flag = false;
			message = "规格类型无效";
		}
		
		if(flag){
			result.setPassing(true);
			result.setMessage(message);
			result.setSizeType(hubProduct.getSizeType());
			result.setSizeValue(hubProduct.getSkuSize());
		}else{
			result.setPassing(false);
			result.setMessage(message);
			result.setSizeType(hubProduct.getSizeType());
			result.setSizeValue(hubProduct.getSkuSize());
		}
		return result;
	}

}
