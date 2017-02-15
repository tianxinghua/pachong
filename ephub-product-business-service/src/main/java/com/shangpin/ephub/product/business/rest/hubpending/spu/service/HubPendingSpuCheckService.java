package com.shangpin.ephub.product.business.rest.hubpending.spu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: huaPendingSpu校验实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubPendingSpuCheckService {
	
	@Autowired
	HubCheckService hubCheckService;
	public HubPendingSpuCheckResult  checkHubPendingSpu(HubSpuPendingDto hubProduct){
		
		HubPendingSpuCheckResult returnStr = hubCheckService.checkSpu(hubProduct);
		
		return returnStr;
	}
	

}
