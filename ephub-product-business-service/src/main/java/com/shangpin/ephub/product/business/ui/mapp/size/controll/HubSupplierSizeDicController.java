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

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.hubDic.size.HubSizeDicService;
import com.shangpin.ephub.product.business.common.supplier.sku.HubSupplierSkuService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicResponseWithPageDto;
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
	@Autowired
	SupplierService supplierService;
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse selectHubSupplierCateoryList(@RequestBody HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto){
		try {
			if(hubSupplierSizeDicRequestDto!=null&&hubSupplierSizeDicRequestDto.getType()!=null){
				return getCommonSizeMapp(hubSupplierSizeDicRequestDto);
			}
			return HubResponse.errorResp("传值为空");
		} catch (Exception e) {
			log.error("获取尺码映射列表失败：{}",e);
			return HubResponse.errorResp("获取列表失败");
		}
    }
	
	private HubResponse getCommonSizeMapp(HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto) {
		
		String supplierNo = hubSupplierSizeDicRequestDto.getSupplierNo();
		if(StringUtils.isNotBlank(supplierNo)){
			if("quanju".equals(supplierNo)){
				hubSupplierSizeDicRequestDto.setSupplierId("quanju");
			}else{
				SupplierDTO supplierDto = supplierService.getSupplier(supplierNo);
				if(supplierDto==null){
					return HubResponse.errorResp("查询供应商返回为空");	
				}
				hubSupplierSizeDicRequestDto.setSupplierId(supplierDto.getSopUserNo());
			}
			int total = 0;
			total = hubSizeDicService.countHubSupplierValueMapping(hubSupplierSizeDicRequestDto,
					SupplierValueMappingType.TYPE_SIZE.getIndex());
			if (total > 0) {
				List<HubSupplierValueMappingDto> list = hubSizeDicService
						.getHubSupplierValueMappingBySupplierIdAndType(hubSupplierSizeDicRequestDto,
								SupplierValueMappingType.TYPE_SIZE.getIndex());
				if (list != null && list.size() > 0) {

					HubSupplierSizeDicResponseWithPageDto page = new HubSupplierSizeDicResponseWithPageDto();

					List<HubSupplierSizeDicResponseDto> responseList = new ArrayList<HubSupplierSizeDicResponseDto>();
					for (HubSupplierValueMappingDto dicDto : list) {
						HubSupplierSizeDicResponseDto dic = new HubSupplierSizeDicResponseDto();
						BeanUtils.copyProperties(dicDto, dic);
						if(dicDto.getCreateTime()!=null){
							dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));	
						}
						if(dicDto.getUpdateTime()!=null){
							dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
						}
						responseList.add(dic);
					}
					page.setList(responseList);
					page.setTotal(total);
					return HubResponse.successResp(page);
				}
			}
		}
		return HubResponse.errorResp("列表页为空");
		
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
					return HubResponse.errorResp("列表页为空");
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
			hubSupplierValueMappingDto.setHubSupplierValMappingId(dto.getHubSupplierValMappingId());
			hubSupplierValueMappingDto.setHubVal(dto.getHubVal());
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
			exportProduct(dto);
			HubSupplierSkuCriteriaDto criteriaSku = new HubSupplierSkuCriteriaDto();
			criteriaSku.setPageNo(1);
			criteriaSku.setPageSize(10000);
			Byte type = dto.getType();
			if(type==1){
				criteriaSku.createCriteria().andSupplierSkuSizeEqualTo(dto.getSupplierVal());
			}else if(type==2){
				criteriaSku.createCriteria().andSupplierIdEqualTo(dto.getSupplierId()).andSupplierSkuSizeEqualTo(dto.getSupplierVal());
			}else if(type==3){
				criteriaSku.createCriteria().andSupplierSkuSizeEqualTo(dto.getSupplierVal());
			}
			
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
