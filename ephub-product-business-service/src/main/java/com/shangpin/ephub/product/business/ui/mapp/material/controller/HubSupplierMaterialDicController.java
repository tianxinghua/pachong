package com.shangpin.ephub.product.business.ui.mapp.material.controller;

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
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.hubDic.material.service.HubMaterialDicService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.material.dto.HubSupplierMaterialDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.material.dto.HubSupplierMaterialDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.material.dto.HubSupplierMaterialDicResponseWithPageDto;
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
@RequestMapping("/hub-supplier-material-dic")
@Slf4j
public class HubSupplierMaterialDicController {
	@Autowired
	HubMaterialDicService hubMaterialDicService;
	@Autowired
	SupplierService supplierService;
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public HubResponse selectHubSupplierMaterialList(
			@RequestBody HubSupplierMaterialDicRequestDto hubSupplierMaterialDicRequestDto) {
		try {
			log.info("===颜色映射list请求参数：{}",hubSupplierMaterialDicRequestDto);
			int total =0;
			byte type = hubSupplierMaterialDicRequestDto.getType();
			total = hubMaterialDicService.countSupplierMaterialByType(type,hubSupplierMaterialDicRequestDto.getSupplierMaterial(),hubSupplierMaterialDicRequestDto.getHubMaterial());
			log.info("返回个数："+total);
			if(total>0){
				List<HubMaterialMappingDto> list = hubMaterialDicService.getSupplierMaterialByType(hubSupplierMaterialDicRequestDto.getPageNo(),hubSupplierMaterialDicRequestDto.getPageSize(),hubSupplierMaterialDicRequestDto.getType(),hubSupplierMaterialDicRequestDto.getSupplierMaterial(),
						hubSupplierMaterialDicRequestDto.getHubMaterial());
				Map<String,HubMaterialMappingDto> map = new HashMap<>();
				List<HubSupplierMaterialDicResponseDto> responseList = new ArrayList<HubSupplierMaterialDicResponseDto>();
				if (list != null && list.size() > 0) {
					for (HubMaterialMappingDto dicDto : list) {
						if(type==0){
							HubSupplierMaterialDicResponseDto dic = new HubSupplierMaterialDicResponseDto();
							dic.setMaterialMappingId(dicDto.getMaterialMappingId());
							dic.setHubMaterial(dicDto.getHubMaterial());
							dic.setSupplierMaterial(dicDto.getSupplierMaterial());
							responseList.add(dic);
						}
						if(type==1){
							if(map.containsKey(dicDto.getHubMaterial())){
								HubMaterialMappingDto temp = map.get(dicDto.getHubMaterial());
								temp.setSupplierMaterial(temp.getHubMaterial()+","+dicDto.getHubMaterial());
							}else{
								map.put(dicDto.getHubMaterial(), dicDto);
							}
						}
					}
				}
				if(type==1){
					if(map!=null&&map.size()>0){
						for(Map.Entry<String,HubMaterialMappingDto> entry:map.entrySet()){
							HubMaterialMappingDto dicDto = entry.getValue();
							HubSupplierMaterialDicResponseDto dic = new HubSupplierMaterialDicResponseDto();
							dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));
							if(dicDto.getUpdateTime()!=null){
								dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
							}
							BeanUtils.copyProperties(dicDto, dic);
							dic.setUpdateUser(dicDto.getUpdateUser());
							responseList.add(dic);
						}
					}
				}
				
				HubSupplierMaterialDicResponseWithPageDto response = new HubSupplierMaterialDicResponseWithPageDto();
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
	public HubResponse selectHubMaterialDetail(@RequestBody HubSupplierMaterialDicRequestDto hubSupplierMaterialDicRequestDto) {
		try {
			log.info("颜色详情请求参数：{}",hubSupplierMaterialDicRequestDto);
			
			int total = hubMaterialDicService.countHubMaterialDicByHubMaterialId(hubSupplierMaterialDicRequestDto.getHubMaterial());
			log.info("返回个数："+total);
			if(total>0){
				List<HubMaterialMappingDto> detailList = hubMaterialDicService.getSupplierMaterialByHubMaterialId(hubSupplierMaterialDicRequestDto.getHubMaterial(),
						hubSupplierMaterialDicRequestDto.getPageNo(),hubSupplierMaterialDicRequestDto.getPageSize());
				if (detailList != null&&detailList.size()>0) {
					List<HubSupplierMaterialDicResponseDto> responseList = new ArrayList<HubSupplierMaterialDicResponseDto>();
					for(HubMaterialMappingDto dicDto:detailList){
						HubSupplierMaterialDicResponseDto dic = new HubSupplierMaterialDicResponseDto();
						dic.setMaterialMappingId(dicDto.getMaterialMappingId());
						dic.setHubMaterial(dicDto.getHubMaterial());
						dic.setSupplierMaterial(dicDto.getSupplierMaterial());
						responseList.add(dic);
					}
					HubSupplierMaterialDicResponseWithPageDto response = new HubSupplierMaterialDicResponseWithPageDto();
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
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse save(@RequestBody HubSupplierMaterialDicRequestDto dto) {

		try {
			HubMaterialMappingDto dicDto = new HubMaterialMappingDto();
			BeanUtils.copyProperties(dto, dicDto);
			if(StringUtils.isNotBlank(dto.getSupplierMaterial())){
				String [] supplierMaterialArr = dto.getSupplierMaterial().trim().split(",",-1);
				for(String supplierMaterial:supplierMaterialArr){
					List<HubMaterialMappingDto> listHubMaterial = hubMaterialDicService.getHubMaterialDic(supplierMaterial);
					if(listHubMaterial!=null&&listHubMaterial.size()>0){
						continue;
					}
					dicDto.setCreateTime(new Date());
					dicDto.setUpdateTime(new Date());
					dicDto.setCreateUser(dto.getCreateUser());
					hubMaterialDicService.saveHubSupplierMaterial(dicDto);
				}
			}
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("保存失败：{}", e);
		}
		return HubResponse.errorResp("保存异常");
	}

	@RequestMapping(value = "/updateAndRefresh", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse refresh(@RequestBody HubSupplierMaterialDicRequestDto dto) {
		try {
			log.info("颜色修改参数：{}",dto);
			if(StringUtils.isNotBlank(dto.getSupplierMaterial())){
				String [] supplierMaterialArr = dto.getSupplierMaterial().trim().split(",",-1);
				for(String supplierMaterial:supplierMaterialArr){
					HubMaterialMappingDto dicDto = new HubMaterialMappingDto();
					dicDto.setUpdateTime(new Date());
					dicDto.setUpdateUser(dto.getUpdateUser());
					dicDto.setSupplierMaterial(supplierMaterial);
					hubMaterialDicService.updateSupplierMaterialById(dicDto);
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
				hubMaterialDicService.deleteHubSupplierMaterialById(id);
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
