package com.shangpin.ephub.product.business.ui.task.refreshDic.category.controller;

import java.text.SimpleDateFormat;
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

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.hubDic.category.HubCategoryDicService;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.category.dto.HubSupplierCategoryDicResponseWithPageDto;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
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
//@RestController
//@RequestMapping("/hub-supplier-category-diccategory")
@Slf4j
public class HubSupplierCategoryDicController {
	@Autowired
	HubCategoryDicService hubCategoryDicService;
	@Autowired
	HubSupplierSpuService hubSupplierSpuService;
	@Autowired
	HubSupplierValueMappingService hubSupplierValueMappingService;
	@Autowired
	SupplierService supplierService;
	@Autowired
	TaskImportService taskImportService;
	@Autowired
	IShangpinRedis shangpinRedis;
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public HubResponse selectHubSupplierCateoryList(
			@RequestBody HubSupplierCategoryDicRequestDto hubSupplierCategoryDicRequestDto) {
		
		try {
			log.info("===品类映射list请求参数：{}",hubSupplierCategoryDicRequestDto);
			String supplierNo = hubSupplierCategoryDicRequestDto.getSupplierNo();
			String supplierId = null;
			if(StringUtils.isNotBlank(supplierNo)){
				SupplierDTO supplierDto = supplierService.getSupplier(supplierNo);
				if(supplierDto==null){
					return HubResponse.errorResp("供应商为空");
				}	
				supplierId = supplierDto.getSopUserNo();
			}
			
			int total = hubCategoryDicService.countSupplierCategoryBySupplierIdAndType(supplierId,hubSupplierCategoryDicRequestDto.getCategoryType(),hubSupplierCategoryDicRequestDto.getSupplierCategory(),hubSupplierCategoryDicRequestDto.getSupplierGender());
			log.info("返回个数："+total);
			if(total>0){
				List<HubSupplierCategroyDicDto> list = hubCategoryDicService.getSupplierCategoryBySupplierIdAndType(supplierId,
						hubSupplierCategoryDicRequestDto.getPageNo(), hubSupplierCategoryDicRequestDto.getPageSize(),hubSupplierCategoryDicRequestDto.getCategoryType(),hubSupplierCategoryDicRequestDto.getSupplierCategory(),hubSupplierCategoryDicRequestDto.getSupplierGender());
				if (list != null && list.size() > 0) {
					List<HubSupplierCategoryDicResponseDto> responseList = new ArrayList<HubSupplierCategoryDicResponseDto>();
					for (HubSupplierCategroyDicDto dicDto : list) {
						HubSupplierCategoryDicResponseDto dic = new HubSupplierCategoryDicResponseDto();
						List<HubSupplierValueMappingDto> listMapp = hubSupplierValueMappingService.getHubSupplierValueMappingByTypeAndSupplierId((byte)5,dicDto.getSupplierId());
						if(listMapp!=null&&listMapp.size()>0){
							dic.setSupplierNo(listMapp.get(0).getHubValNo());
							dic.setSupplierName(listMapp.get(0).getHubVal());
						}	
						dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));
						if(dicDto.getUpdateTime()!=null){
							dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
						}
						BeanUtils.copyProperties(dicDto, dic);
						responseList.add(dic);
					}
					HubSupplierCategoryDicResponseWithPageDto response = new HubSupplierCategoryDicResponseWithPageDto();
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
			if (id != null) {
				HubSupplierCategroyDicDto detail = hubCategoryDicService.getSupplierCategoryById(id);
				if (detail != null) {
					List<HubSupplierCategoryDicResponseDto> responseList = new ArrayList<HubSupplierCategoryDicResponseDto>();
					HubSupplierCategoryDicResponseDto dic = new HubSupplierCategoryDicResponseDto();
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
			if(dto.getCategoryType()!=0||dto.getCategoryType()!=1){
				Date date = new Date();
				String taskNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
				taskImportService.saveTask(taskNo, "品类映射:"+dto.getSupplierCategory()+"=>"+dto.getHubCategoryNo(), dto.getUpdateUser(), TaskType.REFRESH_DIC.getIndex());
				dto.setRefreshDicType((byte)4);
				taskImportService.sendTaskMessage(taskNo,TaskType.REFRESH_DIC.getIndex(),JsonUtil.serialize(dto));
				shangpinRedis.del(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY+"_"+dto.getSupplierId());
			}
		} catch (Exception e) {
			log.error("刷新失败：{}", e);
			return HubResponse.errorResp("刷新异常");
		}
		return HubResponse.successResp(null);
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public HubResponse deleteHubSupplierCateoryDetail(@PathVariable("id") Long id) {
		try {
			if (id != null) {
				hubCategoryDicService.deleteHubSupplierCateoryById(id);
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
