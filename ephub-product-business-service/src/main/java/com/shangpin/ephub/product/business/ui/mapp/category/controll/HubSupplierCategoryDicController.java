package com.shangpin.ephub.product.business.ui.mapp.category.controll;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.product.business.common.hubDic.category.HubCategoryDicService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicResponseDto;
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
@RequestMapping("/hub-supplier-category-dic")
@Slf4j
public class HubSupplierCategoryDicController {
	@Autowired
	HubCategoryDicService hubCategoryDicService;
	@Autowired
	HubSupplierSpuService hubSupplierSpuService;
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse selectHubSupplierCateoryList(@RequestBody HubSupplierCategoryDicRequestDto hubSupplierCategoryDicRequestDto){
		try {
			String supplierId = hubSupplierCategoryDicRequestDto.getSupplierId();
			if(StringUtils.isNotBlank(supplierId)){
				List<HubSupplierCategroyDicDto> list = hubCategoryDicService.getSupplierCategoryBySupplierId(supplierId,hubSupplierCategoryDicRequestDto.getPageNo(),hubSupplierCategoryDicRequestDto.getPageSize());
				if(list!=null&&list.size()>0){
					List<HubSupplierCategoryDicResponseDto> responseList = new ArrayList<HubSupplierCategoryDicResponseDto>();
					for(HubSupplierCategroyDicDto dicDto : list){
						HubSupplierCategoryDicResponseDto dic = new HubSupplierCategoryDicResponseDto();
						BeanUtils.copyProperties(dicDto, dic);
						responseList.add(dic);
					}
					return HubResponse.successResp(responseList);
				}else{
					return HubResponse.successResp("列表页为空");
				}
			}else{
				return HubResponse.errorResp("请选择供应商");
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
	@RequestMapping(value = "/save",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse exportProduct(@RequestBody HubSupplierCategoryDicRequestDto dto){
	        	
		try {
			hubCategoryDicService.updateHubCategoryDic(dto.getId(),dto.getHubCategoryNo(),dto.getUpdateUser());
			return HubResponse.successResp("success");
		} catch (Exception e) {
			log.error("保存失败：{}",e);
		}
		return HubResponse.errorResp("保存异常");
    }
	
	/**
	 * 导出查询商品
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/refresh",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse refresh(@RequestBody HubSupplierCategoryDicRequestDto dto){
		try {
			HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
			criteria.createCriteria().andSupplierIdEqualTo(dto.getSupplierId()).andSupplierCategorynameEqualTo(dto.getSupplierCategoryName()).andSupplierGenderEqualTo(dto.getSupplierGender());
			hubSupplierSpuService.updateHubSpuPending(criteria, (byte)6);
			return HubResponse.successResp("success");
		} catch (Exception e) {
			log.error("保存失败：{}",e);
		}
		return HubResponse.errorResp("保存异常");
    }
	
}
