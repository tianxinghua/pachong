package com.shangpin.ephub.product.business.ui.mapp.model.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.hubDic.brand.HubBrandModelDicService;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.model.dto.HuBrandModelDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.model.dto.HubBrandModelDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.model.dto.HubBrandModelDicResponseWithPageDto;
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
@RequestMapping("/hub-brand-model-rule-dic")
@Slf4j
public class HubSupplierModelDicController {
	@Autowired
	HubBrandModelDicService hubBrandModelDicService;
	@Autowired
	HubSupplierSpuService hubSupplierSpuService;
	@Autowired
	HubSupplierValueMappingService hubSupplierValueMappingService;
	@Autowired
	SupplierService supplierService;
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public HubResponse selectHubSupplierColorList(
			@RequestBody HubBrandModelDicRequestDto hubBrandModelDicRequestDto) {
		
		try {
			log.info("===货号映射list请求参数：{}",hubBrandModelDicRequestDto);
			int total = hubBrandModelDicService.countBrandModelByHubCategoryAndHubBrandNo(hubBrandModelDicRequestDto.getHubBrandNo(),hubBrandModelDicRequestDto.getHubCategoryNo());
			log.info("返回个数："+total);
			if(total>0){
				List<HubBrandModelRuleDto> list = hubBrandModelDicService.getBrandModelListByHubCategoryAndHubBrandNo(hubBrandModelDicRequestDto.getHubBrandNo(),hubBrandModelDicRequestDto.getHubCategoryNo(),hubBrandModelDicRequestDto.getPageNo(),hubBrandModelDicRequestDto.getPageSize());
				if (list != null && list.size() > 0) {
					List<HuBrandModelDicResponseDto> responseList = new ArrayList<HuBrandModelDicResponseDto>();
					for (HubBrandModelRuleDto dicDto : list) {
						HuBrandModelDicResponseDto dic = new HuBrandModelDicResponseDto();
						BeanUtils.copyProperties(dicDto, dic);
						if(dicDto.getCreateTime()!=null){
							dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));	
						}
						if(dicDto.getUpdateTime()!=null){
							dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
						}
						dic.setUpdateUser(dicDto.getUpdateUser());
						responseList.add(dic);
					}
					HubBrandModelDicResponseWithPageDto response = new HubBrandModelDicResponseWithPageDto();
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

	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.POST)
	public HubResponse selectHubSupplierCateoryDetail(@PathVariable("id") Long id) {
		try {
			log.info("品牌货号规则详情参数："+id);
			if (id != null) {
				HubBrandModelRuleDto detail = hubBrandModelDicService.getHubBrandModelRuleById(id);
				if (detail != null) {
					List<HuBrandModelDicResponseDto> responseList = new ArrayList<HuBrandModelDicResponseDto>();
					HuBrandModelDicResponseDto dic = new HuBrandModelDicResponseDto();
					BeanUtils.copyProperties(detail, dic);
					responseList.add(dic);
					return HubResponse.successResp(responseList);
				} else {
					return HubResponse.errorResp("列表页为空");
				}
			} else {
				return HubResponse.errorResp("传值为空");
			}
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}

	/**
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse save(@RequestBody HubBrandModelDicRequestDto dto) {

		try {
			HubBrandModelRuleDto dicDto = new HubBrandModelRuleDto();
			BeanUtils.copyProperties(dto, dicDto);
			log.info("======保存品牌货号规则参数：{}",dto);
			dicDto.setUpdateTime(new Date());
			dicDto.setCreateTime(new Date());
			dicDto.setUpdateUser(dto.getCreateUser());
			dicDto.setBrandModelRuleId(null);
			hubBrandModelDicService.insertHubBrandModelRule(dicDto);
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("保存失败：{}", e);
		}
		return HubResponse.errorResp("保存异常");
	}
	
	@RequestMapping(value = "/updateAndRefresh", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse update(@RequestBody HubBrandModelDicRequestDto dto) {
		try {
			log.info("修改参数：{}",dto);
			HubBrandModelRuleDto dicDto = new HubBrandModelRuleDto();
			BeanUtils.copyProperties(dto, dicDto);
			hubBrandModelDicService.updateHubBrandModelRuleById(dicDto);
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("刷新失败：{}", e);
		}
		return HubResponse.errorResp("刷新异常");
	}
	
//	@RequestMapping(value = "/refresh", method = { RequestMethod.POST, RequestMethod.GET })
//	public HubResponse refresh(@RequestBody HubBrandModelDicRequestDto dto) {
//		try {
//			HubBrandModelRuleCriteriaDto criteria = new HubBrandModelRuleCriteriaDto();
//			if(StringUtils.isNotBlank(dto.getHubCategoryNo())){
//				criteria.createCriteria().andHubCategoryNoEqualTo(dto.getHubCategoryNo());
//			}
//			hubBrandModelDicService.updateHubSpuPending(criteria);
//			return HubResponse.successResp("success");
//		} catch (Exception e) {
//			log.error("刷新失败：{}", e);
//		}
//		return HubResponse.errorResp("刷新异常");
//	}
//	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public HubResponse deleteHubSupplierCateoryDetail(@PathVariable("id") Long id) {
		try {
			log.info("删除货号规则参数："+id);
			if (id != null) {
				hubBrandModelDicService.deleteHubBrandModelRuleById(id);
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
