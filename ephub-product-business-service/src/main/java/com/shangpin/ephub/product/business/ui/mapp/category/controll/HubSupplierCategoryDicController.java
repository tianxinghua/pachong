package com.shangpin.ephub.product.business.ui.mapp.category.controll;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.product.business.common.hubDic.category.HubCategoryDicService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicResponseDto;
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
@RequestMapping("/hub-supplier-category-dic")
@Slf4j
public class HubSupplierCategoryDicController {
	@Autowired
	HubCategoryDicService hubCategoryDicService;
	@Autowired
	HubSupplierSpuService hubSupplierSpuService;

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public HubResponse selectHubSupplierCateoryList(
			@RequestBody HubSupplierCategoryDicRequestDto hubSupplierCategoryDicRequestDto) {
		
		try {
			String supplierId = hubSupplierCategoryDicRequestDto.getSupplierId();
			if (StringUtils.isNotBlank(supplierId)) {
				List<HubSupplierCategroyDicDto> list = hubCategoryDicService.getSupplierCategoryBySupplierIdAndType(supplierId,
						hubSupplierCategoryDicRequestDto.getPageNo(), hubSupplierCategoryDicRequestDto.getPageSize(),hubSupplierCategoryDicRequestDto.getCategoryType());
				if (list != null && list.size() > 0) {
					List<HubSupplierCategoryDicResponseDto> responseList = new ArrayList<HubSupplierCategoryDicResponseDto>();
					for (HubSupplierCategroyDicDto dicDto : list) {
						HubSupplierCategoryDicResponseDto dic = new HubSupplierCategoryDicResponseDto();
						BeanUtils.copyProperties(dicDto, dic);
						responseList.add(dic);
					}
					return HubResponse.successResp(responseList);
				} else {
					return HubResponse.successResp("列表页为空");
				}
			} else {
				return HubResponse.errorResp("请选择供应商");
			}
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.POST)
	public HubResponse selectHubSupplierCateoryDetail(@PathVariable("id") Long id) {
		try {
			if (id != null) {
				HubSupplierCategroyDicDto detail = hubCategoryDicService.getSupplierCategoryById(id);
				if (detail != null) {
					List<HubSupplierCategoryDicResponseDto> responseList = new ArrayList<HubSupplierCategoryDicResponseDto>();
					HubSupplierCategoryDicResponseDto dic = new HubSupplierCategoryDicResponseDto();
					BeanUtils.copyProperties(detail, dic);
					responseList.add(dic);
					return HubResponse.successResp(responseList);
				} else {
					return HubResponse.successResp("列表页为空");
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
			hubCategoryDicService.updateHubCategoryDicByPrimaryKey(dicDto);
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
			HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
			criteria.createCriteria().andSupplierIdEqualTo(dto.getSupplierId())
					.andSupplierCategorynameEqualTo(dto.getSupplierCategory())
					.andSupplierGenderEqualTo(dto.getSupplierGender());
			hubSupplierSpuService.updateHubSpuPending(criteria,InfoState.RefreshCategory.getIndex());
			return HubResponse.successResp("success");
		} catch (Exception e) {
			log.error("刷新失败：{}", e);
		}
		return HubResponse.errorResp("刷新异常");
	}

}
