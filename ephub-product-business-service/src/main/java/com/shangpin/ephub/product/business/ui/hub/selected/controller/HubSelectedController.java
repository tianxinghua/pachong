package com.shangpin.ephub.product.business.ui.hub.selected.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestWithPageDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.hub.gateway.HubWaitSelectGateWay;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.ui.hub.selected.service.HubSelectedService;
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
@RequestMapping("/hub-selected")
@Slf4j
public class HubSelectedController {
	@Autowired
	HubWaitSelectGateWay HubWaitSelectGateWay;
	@Autowired
	HubSelectedService HubSelectedService;
	
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse selectList(@RequestBody HubWaitSelectRequestWithPageDto dto){
	        	
		try {
			log.info("已选品请求参数：{}",dto);
			HubWaitSelectRequestDto hubWaitSelectRequest = new HubWaitSelectRequestDto();
			BeanUtils.copyProperties(dto, hubWaitSelectRequest);
//			if(dto.getSupplierSelectState().intValue()==-1){
//				hubWaitSelectRequest.setSupplierSelectState(null);	
//			}
			Long total = HubWaitSelectGateWay.count(hubWaitSelectRequest);
			log.info("已选品查询到总数："+total);
			if(total>0){
				dto.setPageNo(dto.getPageNo()-1);
//				if(dto.getSupplierSelectState().intValue()==-1){
//					hubWaitSelectRequest.setSupplierSelectState(null);	
//				}
				List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectByPage(dto);
				List<HubWaitSelectedResponse> arr = new ArrayList<HubWaitSelectedResponse>();
				for(HubWaitSelectResponseDto hubWaitSelectResponseDto:list){
					HubWaitSelectedResponse HubWaitSelectResponse = new HubWaitSelectedResponse();
					BeanUtils.copyProperties(hubWaitSelectResponseDto, HubWaitSelectResponse);
					HubWaitSelectResponse.setUpdateTime(DateTimeUtil.getTime(hubWaitSelectResponseDto.getUpdateTime()));
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
			log.error("已选品获取列表失败：{}",e);
			return HubResponse.errorResp("获取列表失败");
		}
    }
	/**
	 * 导出查询商品
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-product",method ={RequestMethod.POST,RequestMethod.GET})
    public void exportProduct(@RequestBody HubWaitSelectRequestWithPageDto dto,HttpServletResponse response){
	        	
		try {
			log.info("导出查询商品请求参数：{}",dto);
//			dto.setSupplierSelectState((byte)SupplierSelectState.SELECTED.getIndex());
			dto.setPageNo(0);
			dto.setPageSize(100000);
			List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectByPage(dto);
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-Disposition", "attachment;filename="+"selected_product_" + System.currentTimeMillis()+".xls");    
			OutputStream ouputStream = response.getOutputStream();
			  
			HubSelectedService.exportExcel(list,ouputStream);
			
		} catch (Exception e) {
			log.error("导出查询商品失败：{}",e);
		}
    }
	

	/**
	 * 导出查询图片
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-select-pic",method = RequestMethod.POST)
    public void exportSelectPic(@RequestBody HubWaitSelectRequestWithPageDto dto){
	        	
		try {
			dto.setPageNo(0);
			dto.setPageSize(100000);
			log.info("导出查询图片请求参数：{}",dto);
		} catch (Exception e) {
			log.error("导出查询图片获取失败：{}",e);
		}
    }
	/**
	 * 导出勾选图片
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-check-pic",method = RequestMethod.POST)
    public void exportCheckPic(@RequestBody HubWaitSelectRequestWithPageDto dto){
	        	
		try {
			log.info("导出勾选图片请求参数：{}",dto);
		
			
		} catch (Exception e) {
			log.error("导出勾选图片失败：{}",e);
		}
    }
}
