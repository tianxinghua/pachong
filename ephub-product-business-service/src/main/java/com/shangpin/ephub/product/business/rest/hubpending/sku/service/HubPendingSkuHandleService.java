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
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.SpSkuSizeState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.hubDic.origin.service.HubOriginDicService;
import com.shangpin.ephub.product.business.common.pending.sku.HubPendingSkuService;
import com.shangpin.ephub.product.business.common.pending.spu.HubPendingSpuService;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;
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

	@PostConstruct
	public void init() {
	}

	// 有顺序的 不能打乱顺序
	static Map<String, String> sizeMap = new LinkedHashMap<String, String>() {
		{
			put("½U", ".5");
			put("½", ".5");
			put("+", ".5");
			put("2/3", ".5");
			put("UNIQUE", "均码");
			put("Unica", "均码");
			put("One size", "均码");
			put("UNI", "均码");
			put("TU", "均码");
			put("U", "均码");
			put("Medium", "M");
			put("Small", "S");
			put("VIII", "8");
			put("VI", "6");
			put("III", "3");
			put("II", "2");
			put("I", "1");
		}
	};

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

		if (hubSkuPendingIsExist.getSkuState() == SpuState.HANDLED.getIndex()
				|| hubSkuPendingIsExist.getSkuState() == SpuState.HANDLING.getIndex()
				|| hubSkuPendingIsExist.getSkuState() == SpuState.INFO_IMPECCABLE.getIndex()) {
			// 如果spustate状态为已处理、审核中或者已完善 ，则不更新
			return;
		}
		hubSkuPendingDto.setSkuPendingId(hubSkuPendingIsExist.getSkuPendingId());
		matchSize(hubSkuPendingDto);
		hubPendingSkuService.updateHubSkuPendingByPrimaryKey(hubSkuPendingDto);
	}

	private void handleNewHubSkuPending(HubSkuPendingDto hubSkuPendingDto)
			throws Exception {
		
		setSkuPendingValue(hubSkuPendingDto);
		Map<String, String> sizeMap = getSupplierSizeMapping(hubSkuPendingDto.getSupplierId());
		//映射尺码
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
		//匹配尺码类型
		matchSize(hubSkuPendingDto);
		try{
			hubPendingSkuService.insertHubSkuPending(hubSkuPendingDto);
		}catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				throw e;
			}
		}
	
	}

	private void matchSize(HubSkuPendingDto hubSkuPendingDto){
		if(hubSkuPendingDto.getSpuPendingId()!=null){
			HubSpuPendingDto hubSpuPendingDto = hubPendingSpuService.findHubSpuPendingByPrimary(hubSkuPendingDto.getSpuPendingId());
			if (hubSpuPendingDto!=null&&hubSpuPendingDto.getSpuBrandState() == SpuBrandState.HANDLED.getIndex()
					&& hubSpuPendingDto.getCatgoryState() == CatgoryState.PERFECT_MATCHED.getIndex()) {
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
	public String sizeCommonReplace(String size) {
		if (size == null) {
			return size;
		}
		Map<String, String> commonSizeMap = getCommonSupplierSizeMapping();
		if (null != commonSizeMap && commonSizeMap.size() > 0) {

			Set<String> sizeSet = commonSizeMap.keySet();// sizeMap.keySet();
			String replaceKey = "";
			for (String sizeKey : sizeSet) {
				if ("++".equals(sizeKey)) {
					replaceKey = "\\++";
				} else if ("+".equals(sizeKey)) {
					replaceKey = "\\+";
				} else {
					replaceKey = sizeKey;
				}
				if (size.indexOf(sizeKey) >= 0) {
					size = size.replaceAll(replaceKey, sizeMap.get(sizeKey));
				}
			}
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
