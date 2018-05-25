package com.shangpin.ephub.product.business.rest.hubpending.sku.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.SpSkuSizeState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubSkuCheckDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.hubDic.origin.service.HubOriginDicService;
import com.shangpin.ephub.product.business.common.pending.sku.HubPendingSkuService;
import com.shangpin.ephub.product.business.common.pending.spu.HubPendingSpuService;
import com.shangpin.ephub.product.business.rest.hubpending.sku.dto.SupplierSizeMappingDto;
import com.shangpin.ephub.product.business.rest.size.dto.MatchSizeDto;
import com.shangpin.ephub.product.business.rest.size.service.MatchSizeService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:HubCheckRuleService.java
 * </p>
 * <p>
 * Description: hua商品校验实现
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubPendingSkuHandleService {
	@Autowired
	IShangpinRedis shangpinRedis;
	@Autowired
	HubPendingSkuService hubPendingSkuService;
	@Autowired
	HubPendingSpuService hubPendingSpuService;
	@Autowired
	HubOriginDicService hubOriginDicService;
	@Autowired
	MatchSizeService matchSizeService;
	@Autowired
	HubPendingSkuCheckService hubCheckService;
	
	@PostConstruct
	public void init() {
	}

	public void handleHubPendingSku(HubSkuPendingDto hubSkuPendingDto) throws Exception {

		HubSkuPendingDto hubSkuPendingIsExist = hubPendingSkuService.findHubSkuPendingBySupplierIdAndSupplierSkuNo(
				hubSkuPendingDto.getSupplierId(), hubSkuPendingDto.getSupplierSkuNo());
		if (hubSkuPendingIsExist != null) {
			handleOldHubSkuPending(hubSkuPendingIsExist, hubSkuPendingDto);
		} else {
			handleNewHubSkuPending(hubSkuPendingDto);
		}
	}
	private void handleOldHubSkuPending(HubSkuPendingDto hubSkuPendingIsExist, HubSkuPendingDto hubSkuPendingDto) throws Exception {
		
		if (hubSkuPendingIsExist.getSkuState()!=null&&(hubSkuPendingIsExist.getSkuState() == SpuState.HANDLED.getIndex()
				|| hubSkuPendingIsExist.getSkuState() == SpuState.HANDLING.getIndex())) {
			// 如果skustate状态为已处理、审核中 或者已完善，则不更新
			return;
		}
		
		hubSkuPendingDto.setSkuPendingId(hubSkuPendingIsExist.getSkuPendingId());
		hubSkuPendingDto.setSpuPendingId(hubSkuPendingIsExist.getSpuPendingId());
		//临时的，解决sku状态已完成，但未进入选品的数据
		if(StringUtils.isNotBlank(hubSkuPendingIsExist.getHubSkuSizeType())&&!"排除".equals(hubSkuPendingIsExist.getHubSkuSizeType())){
			hubSkuPendingDto.setHubSkuSize(hubSkuPendingIsExist.getHubSkuSize());
			hubSkuPendingDto.setHubSkuSizeType(hubSkuPendingIsExist.getHubSkuSizeType());
		}else if("排除".equals(hubSkuPendingIsExist.getHubSkuSizeType())||hubSkuPendingIsExist.getFilterFlag()==FilterFlag.INVALID.getIndex()){
			return;
		}
		
		String sizeType = hubSkuPendingDto.getHubSkuSizeType();
		if(StringUtils.isNotBlank(sizeType)){
			checkSkuSizeType(hubSkuPendingDto);
		}else{
			setSizeMapp(hubSkuPendingDto);
			matchSizeType(hubSkuPendingDto);
		}
		hubPendingSkuService.updateHubSkuPendingByPrimaryKey(hubSkuPendingDto);
	}

	private void checkSkuSizeType(HubSkuPendingDto hubSkuPendingDto) {
				
		HubSpuPendingDto hubSpuPending = hubPendingSpuService.findHubSpuPendingByPrimary(hubSkuPendingDto.getSpuPendingId());
		if(checkBrandAndCategoryState(hubSpuPending)){
			HubSkuCheckDto hubSkuCheckDto = new HubSkuCheckDto();
			hubSkuCheckDto.setBrandNo(hubSpuPending.getHubBrandNo());
			hubSkuCheckDto.setCategoryNo(hubSpuPending.getHubCategoryNo());
			hubSkuCheckDto.setSizeType(hubSkuPendingDto.getHubSkuSizeType());
			hubSkuCheckDto.setSkuSize(hubSkuPendingDto.getHubSkuSize());
			if("尺寸".equals(hubSkuPendingDto.getHubSkuSizeType())){
				hubSkuCheckDto.setSpecificationType("尺寸");	
			}else{
				hubSkuCheckDto.setSpecificationType("尺码");
			}
			
			HubPendingSkuCheckResult hubPendingSkuCheckResult = hubCheckService.checkHubPendingSku(hubSkuCheckDto);
			log.info("校验结果：{}",hubPendingSkuCheckResult);
			
			if (hubPendingSkuCheckResult.isPassing()) {
				hubSkuPendingDto.setScreenSize(hubPendingSkuCheckResult.getSizeId());
				log.info("getHubSpuNo：{}",hubSpuPending.getHubSpuNo());
				if(hubSpuPending.getHubSpuNo()!=null){
					hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
				}else{
					hubSkuPendingDto.setSkuState((byte) SpuState.INFO_IMPECCABLE.getIndex());
				}
				hubSkuPendingDto.setSpSkuSizeState((byte) 1);
				hubSkuPendingDto.setFilterFlag((byte)1);
			} else {
				if(hubPendingSkuCheckResult.isFilter()){
					hubSkuPendingDto.setMemo("此尺码过滤不处理");
					hubSkuPendingDto.setFilterFlag((byte)0);
					hubSkuPendingDto.setHubSkuSizeType("排除");
				}else{
					hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
					hubSkuPendingDto.setMemo(hubPendingSkuCheckResult.getMessage());
					hubSkuPendingDto.setFilterFlag((byte)1);
				}
			}
		}else{
			hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
			hubSkuPendingDto.setMemo("品类品牌未校验通过");
			hubSkuPendingDto.setSpSkuSizeState((byte) 0);
			hubSkuPendingDto.setFilterFlag((byte)1);
		}
	}

	private void setSizeMapp(HubSkuPendingDto hubSkuPendingDto) {
		setSkuPendingValue(hubSkuPendingDto);
		Map<String, String> sizeMap = getSupplierSizeMapping(hubSkuPendingDto.getSupplierId());
		//映射尺码
		hubSkuPendingDto.setHubSkuSize(StringUtils.deleteWhitespace(hubSkuPendingDto.getHubSkuSize()));
		if (sizeMap.containsKey(hubSkuPendingDto.getHubSkuSize())) {
			String spSize = sizeMap.get(hubSkuPendingDto.getHubSkuSize());
			String spSizeTypeAndSize = spSize.substring(0, spSize.indexOf(","));
			if (spSizeTypeAndSize.indexOf(":") >= 0) {

				hubSkuPendingDto.setHubSkuSizeType(spSizeTypeAndSize.substring(0, spSizeTypeAndSize.indexOf(":")));
				hubSkuPendingDto.setHubSkuSize(
						spSizeTypeAndSize.substring(spSizeTypeAndSize.indexOf(":"), spSizeTypeAndSize.length()));
			} else {
				hubSkuPendingDto.setHubSkuSize(spSizeTypeAndSize);
			}
			hubSkuPendingDto.setScreenSize(spSize.substring(spSize.indexOf(",") + 1, spSize.length()));
		} else {
			hubSkuPendingDto.setHubSkuSize(sizeCommonReplace(hubSkuPendingDto.getHubSkuSize()));
		}
	}

	private void handleNewHubSkuPending(HubSkuPendingDto hubSkuPendingDto)
			throws Exception {
		
		setSizeMapp(hubSkuPendingDto);
		//匹配尺码类型
		matchSizeType(hubSkuPendingDto);
		try{
			hubPendingSkuService.insertHubSkuPending(hubSkuPendingDto);
		}catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				throw e;
			}
		}
	
	}

	private void matchSizeType(HubSkuPendingDto hubSkuPendingDto){
		if(hubSkuPendingDto.getSpuPendingId()!=null){
			HubSpuPendingDto hubSpuPendingDto = hubPendingSpuService.findHubSpuPendingByPrimary(hubSkuPendingDto.getSpuPendingId());
			if (checkBrandAndCategoryState(hubSpuPendingDto)) {
				MatchSizeDto match = new MatchSizeDto();
				match.setHubBrandNo(hubSpuPendingDto.getHubBrandNo());
				match.setHubCategoryNo(hubSpuPendingDto.getHubCategoryNo());
				match.setSize(hubSkuPendingDto.getHubSkuSize());
				MatchSizeResult result = matchSizeService.matchSize(match);
				if (result.isPassing()) {
					hubSkuPendingDto.setHubSkuSizeType(result.getSizeType());
					hubSkuPendingDto.setSpSkuSizeState(SpSkuSizeState.HANDLED.getIndex());
					hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
					if (hubSpuPendingDto.getHubSpuNo() != null) {
						// 说明hubSpu已存在
						hubSkuPendingDto.setSkuState(SpuState.HANDLING.getIndex());
					} else {
						hubSkuPendingDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());
					}
					return;
				}
			}
		}
		hubSkuPendingDto.setSpSkuSizeState(SpSkuSizeState.UNHANDLED.getIndex());
		hubSkuPendingDto.setSkuState(SpuState.INFO_PECCABLE.getIndex());
	}
	private boolean checkBrandAndCategoryState(HubSpuPendingDto hubSpuPendingDto) {
		if(hubSpuPendingDto!=null&&hubSpuPendingDto.getSpuBrandState()!=null&&hubSpuPendingDto.getCatgoryState()!=null&&hubSpuPendingDto.getSpuBrandState() == SpuBrandState.HANDLED.getIndex()
				&& hubSpuPendingDto.getCatgoryState() == CatgoryState.PERFECT_MATCHED.getIndex()){
			return true;
		}else{
			return false;
		}
	}

	public String sizeCommonReplace(String size) {
		if (size == null) {
			return size;
		}
		Map<String, String> commonSizeMap = getCommonSupplierSizeMapping();
		if (null != commonSizeMap && commonSizeMap.size() > 0) {
			Set<String> sizeSet = commonSizeMap.keySet();// sizeMap.keySet();
			String replaceKey = "";
			for (String sizeKey : sizeSet) {
				System.out.println(sizeKey);
				if ("++".equals(sizeKey)) {
					replaceKey = "\\++";
				} else if ("+".equals(sizeKey)) {
					replaceKey = "\\+";
				}else {
					replaceKey = sizeKey;
				}
				if (size.indexOf(sizeKey) >= 0) {
					size = size.replaceAll(replaceKey, commonSizeMap.get(sizeKey));
				}
			}
		}
		if(size.indexOf(".") >= 0){
			size = size.split("\\.")[0].trim() +"." +size.split("\\.")[1].trim();
		}
		return size;
	}

	public Map<String, String> getCommonSupplierSizeMapping() {
		String supplierValueMapping = shangpinRedis.get(ConstantProperty.REDIS_EPHUB_SUPPLIER_COMMON_SIZE_MAPPING_KEY);
		List<SupplierSizeMappingDto> supplierSizeMappingDtos = null;
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotBlank(supplierValueMapping)) {
			try {
				supplierSizeMappingDtos = mapper.readValue(supplierValueMapping,
						new TypeReference<List<SupplierSizeMappingDto>>() {
						});
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			try {
				supplierSizeMappingDtos = new ArrayList<>();
				List<HubSupplierValueMappingDto> supplierValueMappingDtos = hubOriginDicService.getSupplierCommonSizeValueMapping();
				if (null != supplierValueMappingDtos && supplierValueMappingDtos.size() > 0) {

					for (HubSupplierValueMappingDto dto : supplierValueMappingDtos) {
						SupplierSizeMappingDto sizeMappingDto = new SupplierSizeMappingDto();
						sizeMappingDto.setSpSize(dto.getHubVal());
						sizeMappingDto.setSupplierSize(dto.getSupplierVal());
						supplierSizeMappingDtos.add(sizeMappingDto);

					}
					shangpinRedis.setex(ConstantProperty.REDIS_EPHUB_SUPPLIER_COMMON_SIZE_MAPPING_KEY,
							1000 * ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_TIME,
							mapper.writeValueAsString(supplierSizeMappingDtos));
				}
			} catch (Exception e) {
				log.error("handle size mapping error. reason :  " + e.getMessage(), e);
				e.printStackTrace();
			}
		}

		Map<String, String> map = new LinkedHashMap<>();
		if (null != supplierSizeMappingDtos) {
			for (SupplierSizeMappingDto dto : supplierSizeMappingDtos) {
				map.put(dto.getSupplierSize(), dto.getSpSize());
			}
		}
		return map;

	}

	/**
	 * 先进入redis查找 没有查找数据库
	 * 
	 * @param supplierId
	 * @return 返回 尚品的尺码+"," +尚品的前端的选择尺码的ＩＤ
	 */
	public Map<String, String> getSupplierSizeMapping(String supplierId) {

		String supplierValueMapping = shangpinRedis
				.get(ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY + "_" + supplierId);
		List<SupplierSizeMappingDto> supplierSizeMappingDtos = null;
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotBlank(supplierValueMapping)) {

			try {
				supplierSizeMappingDtos = mapper.readValue(supplierValueMapping,
						new TypeReference<List<SupplierSizeMappingDto>>() {
						});
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			try {
				supplierSizeMappingDtos = new ArrayList<>();
				List<HubSupplierValueMappingDto> supplierValueMappingDtos = hubOriginDicService
						.getHubSupplierValueMappingBySupplierIdAndType(supplierId,
								SupplierValueMappingType.TYPE_SIZE.getIndex());
				if (null != supplierValueMappingDtos && supplierValueMappingDtos.size() > 0) {

					for (HubSupplierValueMappingDto dto : supplierValueMappingDtos) {
						SupplierSizeMappingDto sizeMappingDto = new SupplierSizeMappingDto();
						sizeMappingDto.setSpSize(dto.getHubVal());
						sizeMappingDto.setSupplierSize(dto.getSupplierVal());
						sizeMappingDto.setSpScreenSizeId(null == dto.getHubValNo() ? "" : dto.getHubValNo());
						supplierSizeMappingDtos.add(sizeMappingDto);

					}
					shangpinRedis.setex(ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY + "_" + supplierId,
							1000 * ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_TIME,
							mapper.writeValueAsString(supplierSizeMappingDtos));
				}
			} catch (Exception e) {
				log.error("handle size mapping error. reason :  " + e.getMessage(), e);
				e.printStackTrace();
			}

		}

		Map<String, String> map = new HashMap<>();
		if (null != supplierSizeMappingDtos) {
			for (SupplierSizeMappingDto dto : supplierSizeMappingDtos) {
				map.put(dto.getSupplierSize(), dto.getSpSize() + "," + dto.getSpScreenSizeId());
			}
		}
		return map;

	}

	protected void setSkuPendingValue(HubSkuPendingDto hubSkuPending) {
		// baracode 需要特殊处理
		if (StringUtils.isBlank(hubSkuPending.getSupplierBarcode())) {
			hubSkuPending.setSupplierBarcode(hubSkuPending.getSupplierSkuNo());
		}
		hubSkuPending.setCreateTime(new Date());
		hubSkuPending.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
		hubSkuPending.setSpSkuSizeState(SpSkuSizeState.UNHANDLED.getIndex());
		hubSkuPending.setUpdateTime(new Date());
		hubSkuPending.setDataState(DataState.NOT_DELETED.getIndex());
	}

}