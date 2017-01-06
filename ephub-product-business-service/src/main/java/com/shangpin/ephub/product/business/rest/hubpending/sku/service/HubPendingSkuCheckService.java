package com.shangpin.ephub.product.business.rest.hubpending.sku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
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
	
	public String checkHubPendingSku(HubSkuPendingDto hubProduct){
		StringBuffer str = new StringBuffer();
		boolean flag = hubCheckService.checkHubSize(hubProduct.getHubSkuSize());
		if(flag){
			
		}else{
			str.append("尺码不存在");
		}
		return str.toString();
	}

}