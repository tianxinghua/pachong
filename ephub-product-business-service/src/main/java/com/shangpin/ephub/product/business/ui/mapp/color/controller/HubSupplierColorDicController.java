package com.shangpin.ephub.product.business.ui.mapp.color.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.enumeration.HubColorDic;
import com.shangpin.ephub.product.business.common.hubDic.color.service.HubColorDicService;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.color.dto.HubSupplierColorDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.color.dto.HubSupplierColorDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.color.dto.HubSupplierColorDicResponseWithPageDto;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * HubSpuImportTaskController
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author zhaogenchun
 * @date 2016年12月21日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/hub-supplier-color-dic")
@Slf4j
public class HubSupplierColorDicController {
	@Autowired
	HubColorDicService hubColorDicService;
	@Autowired
	HubSupplierSpuService hubSupplierSpuService;
	@Autowired
	HubSupplierValueMappingService hubSupplierValueMappingService;
	@Autowired
	SupplierService supplierService;
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public HubResponse selectHubSupplierColorList(
			@RequestBody HubSupplierColorDicRequestDto hubSupplierColorDicRequestDto) {
		try {
			log.info("===颜色映射list请求参数：{}",hubSupplierColorDicRequestDto);
			int total =0;
			byte type = hubSupplierColorDicRequestDto.getType();
			if(type==0){
				total = hubColorDicService.countSupplierColorByType(hubSupplierColorDicRequestDto.getType(),hubSupplierColorDicRequestDto.getSupplierColor(),hubSupplierColorDicRequestDto.getColorDicId());
			}else if(type==1){
				total = HubColorDic.getHubColorMap().size();
			}
			log.info("返回个数："+total);
			if(total>0){
				List<HubColorDicItemDto> list = hubColorDicService.getSupplierColorByType(hubSupplierColorDicRequestDto.getPageNo(),hubSupplierColorDicRequestDto.getPageSize(),hubSupplierColorDicRequestDto.getType(),hubSupplierColorDicRequestDto.getSupplierColor(),hubSupplierColorDicRequestDto.getColorDicId());
				Map<Long,HubColorDicItemDto> map = new HashMap<>();
				List<HubSupplierColorDicResponseDto> responseList = new ArrayList<HubSupplierColorDicResponseDto>();
				if (list != null && list.size() > 0) {
					for (HubColorDicItemDto dicDto : list) {
						if(type==0){
							HubSupplierColorDicResponseDto dic = new HubSupplierColorDicResponseDto();
							dic.setColorDicId(dicDto.getColorDicId());
							dic.setColorDicItemId(dicDto.getColorDicItemId());
							dic.setHubColor(HubColorDic.getHubColor(dicDto.getColorDicId()));
							dic.setSupplierColor(dicDto.getColorItemName());
							responseList.add(dic);
						}
						if(type==1){
							if(map.containsKey(dicDto.getColorDicId())){
								HubColorDicItemDto temp = map.get(dicDto.getColorDicId());
								temp.setColorItemName(temp.getColorItemName()+","+dicDto.getColorItemName());
							}else{
								map.put(dicDto.getColorDicId(), dicDto);
							}
						}
					}
				}
				
				if(type==1){
					if(map!=null&&map.size()>0){
						for(Map.Entry<Long,HubColorDicItemDto> entry:map.entrySet()){
							HubColorDicItemDto dicDto = entry.getValue();
							HubSupplierColorDicResponseDto dic = new HubSupplierColorDicResponseDto();
							dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));
							if(dicDto.getUpdateTime()!=null){
								dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
							}
							dic.setColorDicId(dicDto.getColorDicId());
							dic.setColorDicItemId(dicDto.getColorDicItemId());
							dic.setHubColor(HubColorDic.getHubColor(dicDto.getColorDicId()));
							dic.setSupplierColor(dicDto.getColorItemName());
							dic.setUpdateUser(dicDto.getUpdateUser());
							responseList.add(dic);
						}
					}
				}
				
				HubSupplierColorDicResponseWithPageDto response = new HubSupplierColorDicResponseWithPageDto();
				response.setTotal(total);
				response.setList(responseList);
				return HubResponse.successResp(response);
			}
			
			return HubResponse.errorResp("获取列表为空");
			
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}

	@RequestMapping(value = "/sp-color-list", method = RequestMethod.POST)
	public HubResponse selectSpColorList() {
		try {
			int total = HubColorDic.getHubColorMap().size();
			List<HubSupplierColorDicResponseDto> responseList = new ArrayList<HubSupplierColorDicResponseDto>();
			Map<Long,String> map = HubColorDic.getHubColorMap();
			for(Map.Entry<Long,String> entry:map.entrySet()){
				Long key = entry.getKey();
				String color = entry.getValue();
				HubSupplierColorDicResponseDto dic = new HubSupplierColorDicResponseDto();
				dic.setColorDicId(key);
				dic.setHubColor(color);
				responseList.add(dic);
			}
			HubSupplierColorDicResponseWithPageDto response = new HubSupplierColorDicResponseWithPageDto();
			response.setTotal(total);
			response.setList(responseList);
			return HubResponse.successResp(response);
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public HubResponse selectHubColorDetail(@RequestBody HubSupplierColorDicRequestDto hubSupplierColorDicRequestDto) {
		try {
			log.info("颜色详情请求参数：{}",hubSupplierColorDicRequestDto);
			
			int total = hubColorDicService.countHubColorDicByHubColorId(hubSupplierColorDicRequestDto.getColorDicId());
			log.info("返回个数："+total);
			if(total>0){
				List<HubColorDicItemDto> detailList = hubColorDicService.getSupplierColorByHubColorId(hubSupplierColorDicRequestDto.getColorDicId(),hubSupplierColorDicRequestDto.getPageNo(),hubSupplierColorDicRequestDto.getPageSize());
			
				if (detailList != null&&detailList.size()>0) {
					List<HubSupplierColorDicResponseDto> responseList = new ArrayList<HubSupplierColorDicResponseDto>();
					for(HubColorDicItemDto dicDto:detailList){
						HubSupplierColorDicResponseDto dic = new HubSupplierColorDicResponseDto();
						dic.setColorDicId(dicDto.getColorDicId());
						dic.setColorDicItemId(dicDto.getColorDicItemId());
						dic.setHubColor(HubColorDic.getHubColor(dicDto.getColorDicId()));
						dic.setSupplierColor(dicDto.getColorItemName());
						responseList.add(dic);
					}
					HubSupplierColorDicResponseWithPageDto response = new HubSupplierColorDicResponseWithPageDto();
					response.setTotal(total);
					response.setList(responseList);
					return HubResponse.successResp(response);
				} 
			}
			return HubResponse.errorResp("列表页为空");
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}

	/**
	 * 导出查询商品
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse save(@RequestBody HubSupplierColorDicRequestDto dto) {

		try {
			HubColorDicItemDto dicDto = new HubColorDicItemDto();
			BeanUtils.copyProperties(dto, dicDto);
			if(StringUtils.isNotBlank(dto.getSupplierColor())){
				String [] supplierColorArr = dto.getSupplierColor().trim().split(",",-1);
				for(String supplierColor:supplierColorArr){
					List<HubColorDicItemDto> listHubColor = hubColorDicService.getHubColorDicItemBySupplierColor(supplierColor);
					if(listHubColor!=null&&listHubColor.size()>0){
						continue;
					}
					dicDto.setColorItemName(supplierColor);
					dicDto.setCreateTime(new Date());
					dicDto.setUpdateTime(new Date());
					dicDto.setCreateUser(dto.getCreateUser());
					hubColorDicService.saveColorItem(dicDto);
				}
			}
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("保存失败：{}", e);
		}
		return HubResponse.errorResp("保存异常");
	}

	@RequestMapping(value = "/updateAndRefresh", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse refresh(@RequestBody HubSupplierColorDicRequestDto dto) {
		try {
			log.info("颜色修改参数：{}",dto);
			if(StringUtils.isNotBlank(dto.getSupplierColor())){
				String [] supplierColorArr = dto.getSupplierColor().trim().split(",",-1);
				for(String supplierColor:supplierColorArr){
					HubColorDicItemDto dicDto = new HubColorDicItemDto();
					dicDto.setColorDicItemId(dto.getColorDicItemId());
					dicDto.setColorItemName(dto.getSupplierColor());
					dicDto.setUpdateTime(new Date());
					dicDto.setUpdateUser(dto.getUpdateUser());
					dicDto.setColorItemName(supplierColor);
					dicDto.setPushState((byte)1);
					hubColorDicService.updateSupplierColorById(dicDto);
				}
				return	HubResponse.successResp(null);
			}
		} catch (Exception e) {
			log.error("刷新失败：{}", e);
		}
		return HubResponse.errorResp("刷新异常");
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public HubResponse deleteHubSupplierCateoryDetail(@PathVariable("id") Long id) {
		try {
			if (id != null) {
				hubColorDicService.deleteHubSupplierColorById(id);
				return HubResponse.successResp(null);
			} else {
				return HubResponse.errorResp("传值为空");
			}
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}

}
