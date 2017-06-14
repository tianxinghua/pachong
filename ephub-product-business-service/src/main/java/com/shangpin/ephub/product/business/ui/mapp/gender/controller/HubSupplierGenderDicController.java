package com.shangpin.ephub.product.business.ui.mapp.gender.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.hubDic.gender.service.HubGenderDicService;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.gender.dto.HubSupplierGenderDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.gender.dto.HubSupplierGenderDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.gender.dto.HubSupplierGenderDicResponseWithPageDto;
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
@RequestMapping("/hub-supplier-gender-dic")
@Slf4j
public class HubSupplierGenderDicController {
	@Autowired
	HubGenderDicService hubGenderDicService;
	@Autowired
	HubSupplierSpuService hubSupplierSpuService;
	@Autowired
	HubSupplierValueMappingService hubSupplierValueMappingService;
	@Autowired
	SupplierService supplierService;
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public HubResponse selectHubSupplierGenderList(
			@RequestBody HubSupplierGenderDicRequestDto hubSupplierGenderDicRequestDto) {
		try {
			log.info("===性别映射list请求参数：{}",hubSupplierGenderDicRequestDto);
			int total =0;
			total = hubGenderDicService.countSupplierGenderByType(hubSupplierGenderDicRequestDto.getType(),hubSupplierGenderDicRequestDto.getSupplierGender(),hubSupplierGenderDicRequestDto.getHubGender());
			log.info("返回个数："+total);
			if(total>0){
				List<HubGenderDicDto> list = hubGenderDicService.getSupplierGenderByType(hubSupplierGenderDicRequestDto.getPageNo(),hubSupplierGenderDicRequestDto.getPageSize(),hubSupplierGenderDicRequestDto.getType(),hubSupplierGenderDicRequestDto.getSupplierGender(),hubSupplierGenderDicRequestDto.getHubGender());
				List<HubSupplierGenderDicResponseDto> responseList = new ArrayList<HubSupplierGenderDicResponseDto>();
				if (list != null && list.size() > 0) {
					for (HubGenderDicDto dicDto : list) {
						HubSupplierGenderDicResponseDto dic = new HubSupplierGenderDicResponseDto();
						dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));
						if(dicDto.getUpdateTime()!=null){
							dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
						}
						dic.setGenderDicId(dicDto.getGenderDicId());
						dic.setHubGender(dicDto.getHubGender());
						dic.setSupplierGender(dicDto.getSupplierGender());
						dic.setUpdateUser(dicDto.getUpdateUser());
						responseList.add(dic);
					}
				}
				HubSupplierGenderDicResponseWithPageDto response = new HubSupplierGenderDicResponseWithPageDto();
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

	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public HubResponse selectHubGenderDetail(@RequestBody HubSupplierGenderDicRequestDto hubSupplierGenderDicRequestDto) {
		try {
			log.info("性别详情请求参数：{}",hubSupplierGenderDicRequestDto);
			
			int total = hubGenderDicService.countHubGenderDicByHubGender(hubSupplierGenderDicRequestDto.getHubGender());
			log.info("返回个数："+total);
			if(total>0){
				List<HubGenderDicDto> detailList = hubGenderDicService.getSupplierGenderByHubGender(hubSupplierGenderDicRequestDto.getHubGender(),hubSupplierGenderDicRequestDto.getPageNo(),hubSupplierGenderDicRequestDto.getPageSize());
			
				if (detailList != null&&detailList.size()>0) {
					List<HubSupplierGenderDicResponseDto> responseList = new ArrayList<HubSupplierGenderDicResponseDto>();
					for(HubGenderDicDto dicDto:detailList){
						HubSupplierGenderDicResponseDto dic = new HubSupplierGenderDicResponseDto();
						dic.setGenderDicId(dicDto.getGenderDicId());
						dic.setHubGender(dicDto.getHubGender());
						dic.setSupplierGender(dicDto.getSupplierGender());
						responseList.add(dic);
					}
					HubSupplierGenderDicResponseWithPageDto response = new HubSupplierGenderDicResponseWithPageDto();
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
	public HubResponse save(@RequestBody HubSupplierGenderDicRequestDto dto) {
		log.info("性别保存参数：{}",dto);
		try {
			HubGenderDicDto dicDto = new HubGenderDicDto();
			BeanUtils.copyProperties(dto, dicDto);
			if(StringUtils.isNotBlank(dto.getSupplierGender())){
				String  supplierGender = dto.getSupplierGender();
					List<HubGenderDicDto> listHubGender = hubGenderDicService.getHubGenderDicItemBySupplierGender(supplierGender);
					if(listHubGender!=null&&listHubGender.size()>0){
						return HubResponse.errorResp("添加失败，已存在");
					}
					dicDto.setSupplierGender(supplierGender);
					dicDto.setCreateTime(new Date());
					dicDto.setUpdateTime(new Date());
					dicDto.setPushState((byte)1);
					dicDto.setCreateUser(dto.getCreateUser());
					dicDto.setUpdateUser(dto.getCreateUser());
					hubGenderDicService.saveGenderItem(dicDto);
			}
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("保存失败：{}", e);
		}
		return HubResponse.errorResp("保存异常");
	}

	@RequestMapping(value = "/updateAndRefresh", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse refresh(@RequestBody HubSupplierGenderDicRequestDto dto) {
		
		try {
			log.info("性别修改参数：{}",dto);
			if(StringUtils.isNotBlank(dto.getHubGender())){
				String [] supplierGenderArr = dto.getHubGender().trim().split(",",-1);
				for(String hubGender:supplierGenderArr){
					HubGenderDicDto dicDto = new HubGenderDicDto();
					dicDto.setGenderDicId(dto.getGenderDicId());
					dicDto.setUpdateTime(new Date());
					dicDto.setUpdateUser(dto.getUpdateUser());
					dicDto.setHubGender(hubGender);
					dicDto.setPushState((byte)1);
					hubGenderDicService.updateHubGenderById(dicDto);
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
				hubGenderDicService.deleteHubSupplierGenderById(id);
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
