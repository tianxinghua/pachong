package com.shangpin.ephub.product.business.ui.hub.waitselected.controller;

import java.util.ArrayList;
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
import com.shangpin.ephub.product.business.ui.hub.waitselected.dto.HubWaitSelectStateDto;
import com.shangpin.ephub.product.business.ui.hub.waitselected.service.HubWaitSelectedService;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponse;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponseWithPage;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class HubWaitSelectedController {
	@Autowired
	HubWaitSelectGateWay HubWaitSelectGateWay;
	@Autowired
	HubWaitSelectedService hubWaitSelectedService;
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubWaitSelectRequestWithPageDto dto){
	        	
		try {
			log.info("待选品请求参数：{}",dto);
			HubWaitSelectRequestDto hubWaitSelectRequest = new HubWaitSelectRequestDto();
			BeanUtils.copyProperties(dto, hubWaitSelectRequest);
			hubWaitSelectRequest.setSupplierSelectState((byte)0);
			Long total = HubWaitSelectGateWay.count(hubWaitSelectRequest);
			log.info("待选品查询到数据总数："+total);
			if(total>0){
				dto.setPageNo(dto.getPageNo()-1);
				dto.setSupplierSelectState((byte)0);
				List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectByPage(dto);
				List<HubWaitSelectedResponse> arr = new ArrayList<HubWaitSelectedResponse>();
				for(HubWaitSelectResponseDto hubWaitSelectResponseDto:list){
					HubWaitSelectedResponse HubWaitSelectResponse = new HubWaitSelectedResponse();
					BeanUtils.copyProperties(hubWaitSelectResponseDto, HubWaitSelectResponse);
					arr.add(HubWaitSelectResponse);
				}
				HubWaitSelectedResponseWithPage HubWaitSelectedResponseWithPageDto = new HubWaitSelectedResponseWithPage();
				HubWaitSelectedResponseWithPageDto.setTotal(Integer.parseInt(String.valueOf(total)));
				HubWaitSelectedResponseWithPageDto.setList(arr);
				return HubResponse.successResp(HubWaitSelectedResponseWithPageDto);
			}else{
				return HubResponse.successResp("列表页为空");
			}
			
		} catch (Exception e) {
			log.error("待选品获取列表失败：{}",e);
			return HubResponse.errorResp("获取列表失败");
		}
    }
	@RequestMapping(value = "/update-select-state",method = RequestMethod.POST)
    public HubResponse updateSelectState(@RequestBody List<HubWaitSelectStateDto> dto){
		try{
			hubWaitSelectedService.updateSelectState(dto);
	        return HubResponse.successResp(null);
		}catch(Exception e){
			 return HubResponse.errorResp("选品失败，请重新选品");
		}
		
	}
	
}
