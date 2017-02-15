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
		HubPendingSkuCheckResult result = null;
		String message = null;
		boolean flag = false;
		log.info("检验尺码参数：{}",hubProduct);
		if("尺码".equals(hubProduct.getSpecificationType())||StringUtils.isBlank(hubProduct.getSpecificationType())){
			if(StringUtils.isNotBlank(hubProduct.getSizeType())){
				result = hubCheckService.hubSizeExist(hubProduct.getCategoryNo(),hubProduct.getBrandNo(),hubProduct.getSizeType(),hubProduct.getSkuSize());
				return result;
			}else{
				flag = false;
				message = "尺码类型为空";
			}
		}else if("排除".equals(hubProduct.getSpecificationType())){
			message = "尺码已过滤不处理";
			flag = true;
		}else if("尺寸".equals(hubProduct.getSpecificationType())){
			flag = true;
			message = "尺码类型为尺寸不校验";
		}else{
			flag = false;
			message = "规格类型无效";
		}
		
		result = new HubPendingSkuCheckResult();
		if(flag){
			result.setPassing(true);
			result.setMessage(message);
			result.setSizeType(hubProduct.getSpecificationType());
			result.setSizeValue(hubProduct.getSkuSize());
		}else{
			result.setPassing(false);
			result.setMessage(message);
			result.setSizeType(hubProduct.getSpecificationType());
			result.setSizeValue(hubProduct.getSkuSize());
		}
		return result;
	}

}
