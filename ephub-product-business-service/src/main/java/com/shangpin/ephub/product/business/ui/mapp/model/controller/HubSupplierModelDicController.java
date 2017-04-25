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

import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemDto;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.hubDic.color.service.HubColorDicService;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicResponseWithPageDto;
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
//@RequestMapping("/hub-supplier-color-dic")
@Slf4j
public class HubSupplierModelDicController {
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
			int total = hubColorDicService.countSupplierColorByType(hubSupplierColorDicRequestDto.getType(),hubSupplierColorDicRequestDto.getSupplierColor(),hubSupplierColorDicRequestDto.getColorDicId());
			log.info("返回个数："+total);
			if(total>0){
				List<HubColorDicItemDto> list = hubColorDicService.getSupplierColorByType(hubSupplierColorDicRequestDto.getPageNo(),hubSupplierColorDicRequestDto.getPageSize(),hubSupplierColorDicRequestDto.getType(),hubSupplierColorDicRequestDto.getSupplierColor(),hubSupplierColorDicRequestDto.getColorDicId());
				if (list != null && list.size() > 0) {
					List<HubSupplierColorDicResponseDto> responseList = new ArrayList<HubSupplierColorDicResponseDto>();
					for (HubColorDicItemDto dicDto : list) {
						HubSupplierColorDicResponseDto dic = new HubSupplierColorDicResponseDto();
						dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));
						if(dicDto.getUpdateTime()!=null){
							dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
						}
						dic.setColorDicItemId(dicDto.getColorDicItemId());
						dic.setHubColor(dicDto.getColorItemName());
						dic.setSupplierColor(dicDto.getColorItemName());
						dic.setUpdateUser(dicDto.getUpdateUser());
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

	@RequestMapping(value = "/sp-color-list", method = RequestMethod.POST)
	public HubResponse selectSpColorList(@RequestBody HubSupplierColorDicRequestDto hubSupplierColorDicRequestDto) {
		try {
			
			int total = hubColorDicService.countSupplierColorByType(hubSupplierColorDicRequestDto.getColorDicId());
				List<HubColorDicDto> detail = hubColorDicService.getSpColorList(hubSupplierColorDicRequestDto.getPageNo(),hubSupplierColorDicRequestDto.getPageSize(),hubSupplierColorDicRequestDto.getColorDicId());
				if (detail != null&&detail.size()>0) {
					List<HubSupplierColorDicResponseDto> responseList = new ArrayList<HubSupplierColorDicResponseDto>();
					for(HubColorDicDto dicDto : detail){
						HubSupplierColorDicResponseDto dic = new HubSupplierColorDicResponseDto();
						dic.setColorDicId(dicDto.getColorDicId());
						dic.setHubColor(dicDto.getColorName());
						responseList.add(dic);
					}
					HubSupplierColorDicResponseWithPageDto response = new HubSupplierColorDicResponseWithPageDto();
					response.setTotal(total);
					response.setList(responseList);
					return HubResponse.successResp(response);
				} else {
					return HubResponse.errorResp("列表页为空");
				}
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}
	
//	@RequestMapping(value = "/detail/{id}", method = RequestMethod.POST)
//	public HubResponse selectHubSupplierCateoryDetail(@PathVariable("id") Long id) {
//		try {
//			if (id != null) {
//				HubSupplierCategroyDicDto detail = hubCategoryDicService.getSupplierCategoryById(id);
//				if (detail != null) {
//					List<HubSupplierCategoryDicResponseDto> responseList = new ArrayList<HubSupplierCategoryDicResponseDto>();
//					HubSupplierCategoryDicResponseDto dic = new HubSupplierCategoryDicResponseDto();
//					BeanUtils.copyProperties(detail, dic);
//					responseList.add(dic);
//					return HubResponse.successResp(responseList);
//				} else {
//					return HubResponse.errorResp("列表页为空");
//				}
//			} else {
//				return HubResponse.errorResp("传值为空");
//			}
//		} catch (Exception e) {
//			log.error("获取列表失败：{}", e);
//			return HubResponse.errorResp("获取列表失败");
//		}
//	}

	/**
	 * 导出查询商品
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse save(@RequestBody HubSupplierCategoryDicRequestDto dto) {

		try {
			HubSupplierCategroyDicDto dicDto = new HubSupplierCategroyDicDto();
			BeanUtils.copyProperties(dto, dicDto);
			log.info("======供应商品类映射hub品类变更：{}",dto);
			if(dto.getCategoryType()==4){
				dicDto.setMappingState((byte)1);		
				dicDto.setPushState((byte)1);
			}else{
				dicDto.setMappingState((byte)2);
			}
			dicDto.setUpdateTime(new Date());
//			hubCategoryDicService.updateHubCategoryDicByPrimaryKey(dicDto);
			return HubResponse.successResp("success");
		} catch (Exception e) {
			log.error("保存失败：{}", e);
		}
		return HubResponse.errorResp("保存异常");
	}

	@RequestMapping(value = "/refresh", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse refresh(@RequestBody HubSupplierCategoryDicRequestDto dto) {
		try {
			save(dto);
			if(dto!=null&&dto.getSupplierId()!=null){
				String supplierId = dto.getSupplierId();
				HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
				criteria.createCriteria().andSupplierIdEqualTo(supplierId)
						.andSupplierCategorynameEqualTo(dto.getSupplierCategory())
						.andSupplierGenderEqualTo(dto.getSupplierGender());
				hubSupplierSpuService.updateHubSpuPending(criteria,InfoState.RefreshCategory.getIndex());
				return HubResponse.successResp("success");
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
//				hubCategoryDicService.deleteHubSupplierCateoryById(id);
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
