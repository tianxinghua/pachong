package com.shangpin.ephub.product.business.ui.hub.waitselected.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectDetailRequest;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestWithPageDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.hub.gateway.HubWaitSelectGateWay;
import com.shangpin.ephub.product.business.ui.hub.waitselected.dto.HubWaitSelectStateDto;
import com.shangpin.ephub.product.business.ui.hub.waitselected.service.HubWaitSelectedService;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedDetailResponse;
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
	
	SimpleDateFormat sim  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	HubWaitSelectGateWay HubWaitSelectGateWay;
	@Autowired
	HubWaitSelectedService hubWaitSelectedService;
	
	/**
	 * 待选品列表
	 * @param dto
	 * @return 
	 */
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse importSpuList(@RequestBody HubWaitSelectRequestWithPageDto dto){
		try {
			HubWaitSelectRequestDto hubWaitSelectRequest = new HubWaitSelectRequestDto();
			BeanUtils.copyProperties(dto, hubWaitSelectRequest);
			
			Long total = HubWaitSelectGateWay.count(hubWaitSelectRequest);
			log.info("待选品查询到数据总数："+total);
			if(total>0){
				int pageNo = dto.getPageNo();
				int pageSize = dto.getPageSize();
				dto.setPageNo(pageSize*(pageNo-1));
				dto.setPageSize(pageSize);
				log.info("待选品请求参数：{}",dto);
				List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectByPage(dto);
				List<HubWaitSelectedResponse> arr = new ArrayList<HubWaitSelectedResponse>();
				for(HubWaitSelectResponseDto hubWaitSelectResponseDto:list){
					HubWaitSelectedResponse HubWaitSelectResponse = new HubWaitSelectedResponse();
					BeanUtils.copyProperties(hubWaitSelectResponseDto, HubWaitSelectResponse);
					HubWaitSelectResponse.setUpdateTime(sim.format(hubWaitSelectResponseDto.getUpdateTime()));
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
	
	/**
	 * 待选品详情页
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/detail",method = RequestMethod.POST)
    public HubResponse detail(@RequestBody HubWaitSelectDetailRequest dto){
	        	
		try {
			log.info("待选品详情请求参数：{}",dto);
				List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectDetail(dto);
				log.info("待选品详情返回list:=============》"+list.toString());
				List<HubWaitSelectedDetailResponse> arr = new ArrayList<HubWaitSelectedDetailResponse>();
				for(HubWaitSelectResponseDto hubWaitSelectResponseDto:list){
					HubWaitSelectedDetailResponse HubWaitSelectResponse = new HubWaitSelectedDetailResponse();
					log.info("待选品详情的detail数据：{}",hubWaitSelectResponseDto);
					BeanUtils.copyProperties(hubWaitSelectResponseDto, HubWaitSelectResponse);
					arr.add(HubWaitSelectResponse);
				}
				log.info("待选品详情返回的数据：{}",arr);
				return HubResponse.successResp(arr);
			
		} catch (Exception e) {
			log.error("待选品获取列表失败：{}",e);
			return HubResponse.errorResp("获取列表失败");
		}
    }

	/**
	 * 待选品选中更新
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/update-select-state",method = RequestMethod.POST)
    public HubResponse updateProductDetail(@RequestBody List<HubWaitSelectStateDto> dto){
		try{
			hubWaitSelectedService.updateSelectState(dto);
	        return HubResponse.successResp(null);
		}catch(Exception e){
			 return HubResponse.errorResp("选品失败，请重新选品");
		}
	}
	
	/**
	 * 待选品批量更新
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/batch-update-select-state",method = RequestMethod.POST)
    public HubResponse batchUpdateSelectState(@RequestBody HubWaitSelectRequestWithPageDto dto){
		try{
			
				log.info("待选品请求参数：{}",dto);
					hubWaitSelectedService.batchUpdateSelectState(dto);
					return HubResponse.successResp(null);
		}catch(Exception e){
			 return HubResponse.errorResp("选品失败，请重新选品");
		}
		
	}
	
}
