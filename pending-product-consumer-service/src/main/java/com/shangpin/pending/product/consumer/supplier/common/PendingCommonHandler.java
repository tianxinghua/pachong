package com.shangpin.pending.product.consumer.supplier.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.pending.product.consumer.common.DateUtils;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.supplier.dto.ColorDTO;
import com.shangpin.pending.product.consumer.supplier.dto.MaterialDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/2/8.
 */
@Component
@Slf4j
public class PendingCommonHandler {
	@Autowired
	private DataServiceHandler dataServiceHandler;
	@Autowired
	private IShangpinRedis shangpinRedis;
	static Integer isCurrentMin = DateUtils.getCurrentMin();

	public Map<String, String> getHubSupplierSeasonMap(String supplierId) {
		Map<String, String> supplierSeasonMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY+"_"+supplierId);
		if (supplierSeasonMap == null || supplierSeasonMap.size() < 1) {
			log.info("supplierSeason的redis为空");
			supplierSeasonMap = new HashMap<>();
			List<HubSeasonDicDto> hubSeasonDicDto = dataServiceHandler.getHubSeasonDic();
			for (HubSeasonDicDto dto : hubSeasonDicDto) {
				if(StringUtils.isNotBlank(dto.getSupplierSeason())&&StringUtils.isNotBlank(dto.getHubSeason())&&StringUtils.isNotBlank(dto.getHubMarketTime()))
				supplierSeasonMap.put(dto.getSupplierSeason().toLowerCase().trim(), dto.getHubMarketTime()+"_"+dto.getHubSeason());
			}
			if (supplierSeasonMap.size() > 0) {
				shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY+"_"+supplierId, supplierSeasonMap);
				shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY+"_"+supplierId,
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
			}
		}
		return supplierSeasonMap;
	}
	
	/**
	 * 	材质字典redis映射
	 * @return
	 */
	public Map<String, String> getFirstMaterialMap() {
		Map<String, String> firstStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_FIRST_MATERIAL_MAPPING_MAP_KEY);
		if (firstStaticMap == null || firstStaticMap.size() < 1) {
			log.info("第一级别材质的redis为空");
			firstStaticMap = new HashMap<>();
			List<MaterialDTO> materialDTO = dataServiceHandler.getMaterialMappingByMappingLevel((byte) 1);
			for (MaterialDTO dto : materialDTO) {
				firstStaticMap.put(dto.getSupplierMaterial().toLowerCase().trim(), dto.getHubMaterial());
			}
			if (firstStaticMap.size() > 0) {
				shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_FIRST_MATERIAL_MAPPING_MAP_KEY, firstStaticMap);
				shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_FIRST_MATERIAL_MAPPING_MAP_KEY,
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
			}
		}
		return firstStaticMap;
	}

	public Map<String, String> getSecondMaterialMap() {
		Map<String, String> firstStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_SECOND_MATERIAL_MAPPING_MAP_KEY);
		if (firstStaticMap == null || firstStaticMap.size() < 1) {
			log.info("第二级别材质的redis为空");
			firstStaticMap = new HashMap<>();
			List<MaterialDTO> materialDTO = dataServiceHandler.getMaterialMappingByMappingLevel((byte) 2);
			for (MaterialDTO dto : materialDTO) {
				firstStaticMap.put(dto.getSupplierMaterial().toLowerCase().trim(), dto.getHubMaterial());
			}
			if (firstStaticMap.size() > 0) {
				shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_SECOND_MATERIAL_MAPPING_MAP_KEY, firstStaticMap);
				shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_SECOND_MATERIAL_MAPPING_MAP_KEY,
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
			}
		}
		return firstStaticMap;
	}

	public Map<String, String> getThreeMaterialMap() {
		Map<String, String> firstStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_THREE_MATERIAL_MAPPING_MAP_KEY);
		if (firstStaticMap == null || firstStaticMap.size() < 1) {
			log.info("第三级别材质的redis为空");
			firstStaticMap = new HashMap<>();
			List<MaterialDTO> materialDTO = dataServiceHandler.getMaterialMappingByMappingLevel((byte) 3);
			for (MaterialDTO dto : materialDTO) {
				firstStaticMap.put(dto.getSupplierMaterial().toLowerCase().trim(), dto.getHubMaterial());
			}
			if (firstStaticMap.size() > 0) {
				shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_THREE_MATERIAL_MAPPING_MAP_KEY, firstStaticMap);
				shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_THREE_MATERIAL_MAPPING_MAP_KEY,
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
			}
		}
		return firstStaticMap;
	}

	public Map<String, String> getReplaceMaterialMap() {
		Map<String, String> firstStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_REPLACE_MATERIAL_MAPPING_MAP_KEY);
		if (firstStaticMap == null || firstStaticMap.size() < 1) {
			log.info("替换级别材质的redis为空");
			firstStaticMap = new HashMap<>();
			List<MaterialDTO> materialDTO = dataServiceHandler.getMaterialMappingByMappingLevel((byte) 4);
			for (MaterialDTO dto : materialDTO) {
				firstStaticMap.put(dto.getSupplierMaterial().toLowerCase().trim(), "");
			}
			if (firstStaticMap.size() > 0) {
				shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_REPLACE_MATERIAL_MAPPING_MAP_KEY, firstStaticMap);
				shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_REPLACE_MATERIAL_MAPPING_MAP_KEY,
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
			}
		}
		return firstStaticMap;
	}

	public Map<String, String> getSpCategoryValue(String supplierId) throws Exception {

		// 先判断设置的是否有值 无值得话 品类重新处理
		Map<String, String> supplierMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY + "_" + supplierId);
		if (supplierMap == null || supplierMap.size() < 1) {
			log.info("品类的redis为空");
			Map<String, String> categoryMap = new HashMap<>();
			List<HubSupplierCategroyDicDto> supplierCategoryMappingDtos = dataServiceHandler
					.getAllSupplierCategoryBySupplierId(supplierId);
			if (null != supplierCategoryMappingDtos && supplierCategoryMappingDtos.size() > 0) {
				String spCategory = "";
				for (HubSupplierCategroyDicDto dto : supplierCategoryMappingDtos) {
					if (StringUtils.isBlank(dto.getSupplierCategory()))
						continue;
					if (StringUtils.isBlank(dto.getSupplierGender()))
						continue;
					spCategory = (null == dto.getHubCategoryNo() ? "" : dto.getHubCategoryNo());
					categoryMap.put(
							dto.getSupplierCategory().trim().toUpperCase() + "_"
									+ dto.getSupplierGender().trim().toUpperCase(),
							spCategory + "_" + dto.getMappingState());
				}
				shangpinRedis.hmset(
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY + "_" + supplierId,
						categoryMap);
				shangpinRedis.expire(
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY + "_" + supplierId,
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
			}
			return categoryMap;
		}
		return supplierMap;

	}

	/**
	 * 在指定时间段 重新获取所有数据
	 *
	 * @return
	 */
	public boolean isNeedHandle() {
		int min = DateUtils.getCurrentMin();
		if (min - isCurrentMin >= 5 || min - isCurrentMin < 0) {
			isCurrentMin = min;
			return true;
		} else {
			return false;
		}
	}

	public boolean getCategoryMap(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
		boolean result = true;
		String categoryAndStatus = "";
		Integer mapStatus = 0;
		Map<String, String> categoryMappingMap = this.getSpCategoryValue(spu.getSupplierId());
		if (categoryMappingMap.containsKey(
				spu.getHubCategoryNo().trim().toUpperCase() + "_" + spu.getHubGender().trim().toUpperCase())) {
			// 包含时转化赋值
			categoryAndStatus = categoryMappingMap
					.get(spu.getHubCategoryNo().trim().toUpperCase() + "_" + spu.getHubGender().trim().toUpperCase());
			if (categoryAndStatus.contains("_")) {
				hubSpuPending.setHubCategoryNo(categoryAndStatus.substring(0, categoryAndStatus.indexOf("_")));
				mapStatus = Integer.valueOf(categoryAndStatus.substring(categoryAndStatus.indexOf("_") + 1));
				hubSpuPending.setCatgoryState(mapStatus.byteValue());
				if (hubSpuPending.getCatgoryState().intValue() != PropertyStatus.MESSAGE_HANDLED.getIndex()) {
					// 未达到4级品类
					result = false;
				}
			} else {
				result = false;
				hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
			}
		}
		return result;
	}

	protected Map<String, String> getSupplierColorMap() {
		Map<String, String> colorStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY);
		if (colorStaticMap == null || colorStaticMap.size() < 1) {
			log.info("供应商颜色的redis为空");
			colorStaticMap = new HashMap<>();
			List<ColorDTO> colorDTOS = dataServiceHandler.getColorDTO();
			for (ColorDTO dto : colorDTOS) {
				if (StringUtils.isNotBlank(dto.getSupplierColor()) && StringUtils.isNotBlank(dto.getHubColorName())) {
					colorStaticMap.put(dto.getSupplierColor().toUpperCase(), dto.getHubColorName());
				}
			}
			shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY, colorStaticMap);
			shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY,
					ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
		}
		return colorStaticMap;
	}

	protected Map<String, String> getHubColorMap() {

		Map<String, String> hubColorStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_HUB_COLOR_MAPPING_MAP_KEY);
		if (hubColorStaticMap == null || hubColorStaticMap.size() < 1) {
			log.info("hub颜色redis为空");
			hubColorStaticMap = new HashMap<>();
			List<ColorDTO> colorDTOS = dataServiceHandler.getColorDTO();
			for (ColorDTO dto : colorDTOS) {
				if (dto.getSupplierColor() != null) {
					hubColorStaticMap.put(dto.getHubColorName(), "");
				}
			}
			shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_HUB_COLOR_MAPPING_MAP_KEY, hubColorStaticMap);
			shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_HUB_COLOR_MAPPING_MAP_KEY,
					ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 1000);
		}
		return hubColorStaticMap;
	}
}
