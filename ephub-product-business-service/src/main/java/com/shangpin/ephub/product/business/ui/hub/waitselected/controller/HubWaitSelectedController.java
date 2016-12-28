package com.shangpin.ephub.product.business.ui.hub.waitselected.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.SpuSelectState;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestWithPageDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.hub.gateway.HubWaitSelectGateWay;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponseWithPage;
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
	HubWaitSelectGateWay HubWaitSelectGateWay;
	
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubWaitSelectRequestWithPageDto dto){
	        	
		try {
			HubWaitSelectRequestDto hubWaitSelectRequest = new HubWaitSelectRequestDto();
			BeanUtils.copyProperties(dto, hubWaitSelectRequest);
			hubWaitSelectRequest.setSpuSelectState((byte)SpuSelectState.WAIT_SELECT.getIndex());
			int total = HubWaitSelectGateWay.count(hubWaitSelectRequest);
			if(total<0){
				List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectByPage(dto);
				HubWaitSelectedResponseWithPage HubWaitSelectedResponseWithPageDto = new HubWaitSelectedResponseWithPage();
				HubWaitSelectedResponseWithPageDto.setTotal(total);
				HubWaitSelectedResponseWithPageDto.setList(list);
				return HubResponse.successResp(HubWaitSelectedResponseWithPageDto);
			}else{
				return HubResponse.successResp("列表页为空");
			}
			
		} catch (Exception e) {
			return HubResponse.errorResp("获取列表失败");
		}
    }
}
