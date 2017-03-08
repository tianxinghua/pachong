package com.shangpin.ephub.product.business.rest.hubpending.spu.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuHandleService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HubPendingSpu入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/pending-spu-handle")
@Slf4j
public class HubPendingSpuHandleController {
	
	@Autowired
	private HubPendingSpuHandleService hubPendingSpuHandleService;
	
	@RequestMapping(value = "/pending-spu")
	public HubSpuPendingDto handleHubPendingSpu(@RequestBody HubSpuPendingDto hubSpuPendingDto){
		log.info("pendingSpu处理接受到数据：{}",hubSpuPendingDto);
		System.out.println(JSONObject.toJSONString(hubSpuPendingDto));
		try {
			return hubPendingSpuHandleService.handleHubPendingSpu(hubSpuPendingDto);
		} catch (Exception e) {
			log.error("spuPending处理发生异常：{}",e);
			e.printStackTrace();
		}
		return null;
	}
	
//	@RequestMapping(value = "/check-spu")
//	public String checkSpu(@RequestBody HubSpuPendingDto hubSpuPendingDto){
//		log.info("pendingSpu校验接受到数据：{}",hubSpuPendingDto);
//		String returnStr =null;
//		try {
//			returnStr = hubPendingSpuHandleService.checkHubPendingSpuPropert(hubSpuPendingDto);
//		} catch (Exception e) {
//			log.error("spuPending校验发生异常：{}",e);
//			e.printStackTrace();
//		}
//		return returnStr;
//	}
//	
	
	@RequestMapping(value = "/update-spu/{spuPendingId}")
	public String updateSpuState(@PathVariable(value="spuPendingId") Long spuPendingId){
		log.info("pendingSpu更新状态接受到数据：{}",spuPendingId);
		String returnStr =null;
		try {
			returnStr = hubPendingSpuHandleService.updateSpuPendingState(spuPendingId);
		} catch (Exception e) {
			log.error("spuPending校验发生异常：{}",e);
			e.printStackTrace();
		}
		return returnStr;
	}
}