package com.shangpin.ephub.product.business.rest.hubpending.sku.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubSkuCheckDto;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.rest.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.product.business.rest.hubpending.spu.result.HubSizeCheckResult;

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
		log.info("检验尺码参数：{}",hubProduct);
		if("尺码".equals(hubProduct.getSpecificationType())||StringUtils.isBlank(hubProduct.getSpecificationType())){
			if(StringUtils.isNotBlank(hubProduct.getSizeType())){
				HubSizeCheckResult hubSizeExist = hubCheckService.hubSizeExist(hubProduct.getCategoryNo(),hubProduct.getBrandNo(),hubProduct.getSkuSize());
				if(hubSizeExist.isPassing()){
					result.setPassing(true);
					result.setResult(hubSizeExist.getScreenSizeStandardValueId()+","+hubProduct.getSizeType()+":"+hubProduct.getSkuSize());
				}else{
					result.setPassing(false);
					result.setResult(hubSizeExist.getResult());
				}
			}else{
				result.setPassing(false);
				result.setResult("尺码类型为空");
			}
			
		}else{
			result.setPassing(true);
			result.setResult(hubProduct.getSkuSize());
		}
		return result;
	}

}
