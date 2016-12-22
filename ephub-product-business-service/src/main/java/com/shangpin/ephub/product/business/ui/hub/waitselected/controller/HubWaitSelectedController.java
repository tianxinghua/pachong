package com.shangpin.ephub.product.business.ui.hub.waitselected.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.hub.waitselected.dao.HubWaitSelectedRequestDto;
import com.shangpin.ephub.product.business.ui.hub.waitselected.service.HubWaitSelectedService;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponseDto;
import com.shangpin.ephub.response.HubResponse;


/**
 * <p>HubSpuImportTaskController </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月21日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/hub-waitSelected")
public class HubWaitSelectedController {
	
	@Autowired
	HubWaitSelectedService hubWaitSelectedService;
	
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubWaitSelectedRequestDto dto){
	        	
		try {
			List<HubWaitSelectedResponseDto> list = hubWaitSelectedService.findHubWaitSelectedList(dto);
			return HubResponse.successResp(list);
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表失败");
		}
    }
}
