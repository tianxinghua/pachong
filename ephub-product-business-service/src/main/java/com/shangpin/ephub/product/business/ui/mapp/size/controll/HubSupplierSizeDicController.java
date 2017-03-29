package com.shangpin.ephub.product.business.ui.mapp.size.controll;

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
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.hubDic.category.HubCategoryDicService;
import com.shangpin.ephub.product.business.common.hubDic.origin.service.HubOriginDicService;
import com.shangpin.ephub.product.business.common.hubDic.size.HubSizeDicService;
import com.shangpin.ephub.product.business.common.supplier.sku.HubSupplierSkuService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicResponseDto;
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
@RequestMapping("/hub-supplier-size-dic")
@Slf4j
public class HubSupplierSizeDicController {
	@Autowired
	HubSizeDicService hubSizeDicService;
	@Autowired
	HubSupplierSpuService hubSupplierSpuService;
	@Autowired
	HubSupplierSkuService hubSupplierSkuService;
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse selectHubSupplierCateoryList(@RequestBody HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto){
		try {
			String supplierId = hubSupplierSizeDicRequestDto.getSupplierId();
			if(StringUtils.isNotBlank(supplierId)){
				List<HubSupplierValueMappingDto> list = hubSizeDicService.getHubSupplierValueMappingBySupplierIdAndType(supplierId,SupplierValueMappingType.TYPE_SIZE.getIndex(),hubSupplierSizeDicRequestDto.getPageNo(),hubSupplierSizeDicRequestDto.getPageSize());
				if(list!=null&&list.size()>0){
					List<HubSupplierSizeDicResponseDto> responseList = new ArrayList<HubSupplierSizeDicResponseDto>();
					for(HubSupplierValueMappingDto dicDto : list){
						HubSupplierSizeDicResponseDto dic = new HubSupplierSizeDicResponseDto();
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
			log.error("获取尺码映射列表失败：{}",e);
			return HubResponse.errorResp("获取列表失败");
		}
    }
	
	@RequestMapping(value = "/detail/{id}",method = RequestMethod.POST)
    public HubResponse selectHubSupplierCateoryDetail(@PathVariable("id") Long id){
		try {
			if(id!=null){
				HubSupplierValueMappingDto detail = hubSizeDicService.getHubSupplierValueMappingById(id);
				if(detail!=null){
					List<HubSupplierSizeDicResponseDto> responseList = new ArrayList<HubSupplierSizeDicResponseDto>();
					HubSupplierSizeDicResponseDto dic = new HubSupplierSizeDicResponseDto();
					BeanUtils.copyProperties(detail, dic);
					responseList.add(dic);
					return HubResponse.successResp(responseList);
				}else{
					return HubResponse.successResp("列表页为空");
				}
			}else{
				return HubResponse.errorResp("传值为空");
			}
		} catch (Exception e) {
			log.error("获取尺码映射列表失败：{}",e);
			return HubResponse.errorResp("获取列表失败");
		}
    }
	
	
	/**
	 * 保存更新尺码映射
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse exportProduct(@RequestBody HubSupplierSizeDicRequestDto dto){
	        	
		try {
			HubSupplierValueMappingDto hubSupplierValueMappingDto = new HubSupplierValueMappingDto();
			BeanUtils.copyProperties(dto, hubSupplierValueMappingDto);
			hubSizeDicService.updateHubSupplierValueMappingByPrimaryKey(hubSupplierValueMappingDto);
			return HubResponse.successResp("success");
		} catch (Exception e) {
			log.error("保存失败：{}",e);
		}
		return HubResponse.errorResp("保存异常");
    }
	
	@RequestMapping(value = "/refresh",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse refresh(@RequestBody HubSupplierSizeDicRequestDto dto){
		
		try {
			HubSupplierSkuCriteriaDto criteriaSku = new HubSupplierSkuCriteriaDto();
			criteriaSku.setPageNo(1);
			criteriaSku.setPageSize(10000);
			criteriaSku.createCriteria().andSupplierIdEqualTo(dto.getSupplierId()).andSupplierSkuSizeEqualTo(dto.getSupplierVal());
			List<HubSupplierSkuDto> listSku = hubSupplierSkuService.selectListBySupplierIdAndSize(criteriaSku);

			if(listSku!=null&&listSku.size()>0){
				HubSupplierSpuDto spuDto = new HubSupplierSpuDto();
				spuDto.setSupplierSpuId(listSku.get(0).getSupplierSpuId());
				spuDto.setInfoState(InfoState.RefreshSize.getIndex());
				hubSupplierSpuService.updateHubSupplierSpuByPrimaryKey(spuDto);
			}
			return HubResponse.successResp("success");
		} catch (Exception e) {
			log.error("刷新失败：{}",e);
		}
		return HubResponse.errorResp("刷新异常");
    }
	
}
