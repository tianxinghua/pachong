package com.shangpin.pending.product.consumer.supplier.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
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
	static Integer isCurrentMin = DateUtils.getCurrentMin();
    @Autowired
    private IShangpinRedis shangpinRedis;
    static Integer seasonCurrenMin = DateUtils.getCurrentMin();

    static Integer seasonFlagCurrenMin = DateUtils.getCurrentMin();

    static Integer colorCurrenMin = DateUtils.getCurrentMin();

    static Integer materialCurrenMin = DateUtils.getCurrentMin();

    static Integer brandCurrenMin = DateUtils.getCurrentMin();

    static Integer supplierBrandCurrenMin = DateUtils.getCurrentMin();

    static Integer originCurrenMin = DateUtils.getCurrentMin();

    static Integer categoryCurrenMin = DateUtils.getCurrentMin();


    static Integer genderCurrenMin = DateUtils.getCurrentMin();
//	
    



    public  boolean isNeedHandleSeason() {
        int min = DateUtils.getCurrentMin();
        if(min-seasonCurrenMin>=30||min-seasonCurrenMin<0){
            seasonCurrenMin = min;
            return true;
        } else {
            return false;
        }
    }

    public  boolean isNeedHandleSeasonFlag() {
        int min = DateUtils.getCurrentMin();
        if(min-seasonFlagCurrenMin>=30||min-seasonFlagCurrenMin<0){
            seasonFlagCurrenMin = min;
            return true;
        } else {
            return false;
        }
    }


    public  boolean isNeedHandleCorlor() {
        int min = DateUtils.getCurrentMin();
        if(min-colorCurrenMin>=30||min-colorCurrenMin<0){
            colorCurrenMin = min;
            return true;
        } else {
            return false;
        }
    }


    public  boolean isNeedHandleMaterial() {
        int min = DateUtils.getCurrentMin();
        if(min-materialCurrenMin>=30||min-materialCurrenMin<0){
            materialCurrenMin = min;
            return true;
        } else {
            return false;
        }
    }

    public  boolean isNeedHandleOrigin() {
        int min = DateUtils.getCurrentMin();
        if(min-originCurrenMin>=30||min-originCurrenMin<0){
            originCurrenMin = min;
            return true;
        } else {
            return false;
        }
    }

    public  boolean isNeedHandleBrand() {
        int min = DateUtils.getCurrentMin();
        if(min-brandCurrenMin>=30||min-brandCurrenMin<0){
            brandCurrenMin = min;
            return true;
        } else {
            return false;
        }
    }

    public  boolean isNeedHandleSupplierBrand() {
        int min = DateUtils.getCurrentMin();
        if(min-supplierBrandCurrenMin>=30||min-supplierBrandCurrenMin<0){
            supplierBrandCurrenMin = min;
            return true;
        } else {
            return false;
        }
    }

    public  boolean isNeedHandleCategory() {
        int min = DateUtils.getCurrentMin();
        if(min-categoryCurrenMin>=30||min-categoryCurrenMin<0){
            categoryCurrenMin = min;
            return true;
        } else {
            return false;
        }
    }


    public  boolean isNeedHandleGender() {
        int min = DateUtils.getCurrentMin();
        if(min-genderCurrenMin>=30||min-genderCurrenMin<0){
            genderCurrenMin = min;
            return true;
        } else {
            return false;
        }
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


	/**
	 * 如果内部Map的成员很多，那么涉及到遍历整个内部Map的操作，
	 * 由于Redis单线程模型的缘故，这个遍历操作可能会比较耗时，而另其它客户端的请求完全不响应，这点需要格外注意
	 * @return
	 */
	@Deprecated
	public Map<String, String> getSupplierHubCategoryMap(String supplierId) throws Exception {

		// 先判断设置的是否有值 无值得话 品类重新处理
		Map<String, String> supplierMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY + "_" + supplierId);
		if (supplierMap == null || supplierMap.size() < 1) {
			setSupplierCategoryToRedis(supplierId);
			return this.getSupplierHubCategoryMap(supplierId);
		}
		return supplierMap;

	}

	public String  getSupplierHubCategoryFromRedis(String supplierId ,String supplierCategory,String gender){
		if(!shangpinRedis.exists(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + supplierId)){

			setSupplierCategoryToRedis(supplierId);
		}
		List<String> mapValue = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + supplierId,supplierCategory.trim().toUpperCase()+"_"+gender.trim().toUpperCase()) ;
		if(null!=mapValue&&mapValue.size()>0){
			return mapValue.get(0);
		}else{
			return "";
		}
	}

	private void setSupplierCategoryToRedis(String supplierId) {
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
                    ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 6 * 24);
        }
	}

	public Map<String, String> getHubSupplierSeasonMap(String supplierId) {
		Map<String, String> supplierSeasonMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY+"_"+supplierId);
		if (supplierSeasonMap == null || supplierSeasonMap.size() < 1) {
			log.info("supplierSeason的redis为空");
			supplierSeasonMap = new HashMap<>();
			List<HubSeasonDicDto> hubSeasonDicDto = dataServiceHandler.getHubSeasonDic();
			for (HubSeasonDicDto dto : hubSeasonDicDto) {
				if(StringUtils.isNotBlank(dto.getSupplierSeason())&&StringUtils.isNotBlank(dto.getHubSeason())&&StringUtils.isNotBlank(dto.getHubMarketTime()))
					supplierSeasonMap.put(dto.getSupplierSeason().trim().toLowerCase(), dto.getHubMarketTime()+"_"+dto.getHubSeason());
			}
			if (supplierSeasonMap.size() > 0) {
				shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY+"_"+supplierId, supplierSeasonMap);
				shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY+"_"+supplierId,
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 6 * 24);
			}
		}
		return supplierSeasonMap;
	}

	public String  getHubSeasonFromRedis(String supplierId ,String supplierSeason){
		if(!shangpinRedis.exists(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY + "_" + supplierId)){
			getHubSupplierSeasonMap(supplierId);
		}
		List<String> mapValue = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY + "_" + supplierId,supplierSeason.trim().toLowerCase()) ;
		if(null!=mapValue&&mapValue.size()>0){
			return mapValue.get(0);
		}else{
			return "";
		}
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
					ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 6 * 24);
		}
		return colorStaticMap;
	}

	public String  getHubColorFromRedis(String supplierColor){
		if(!shangpinRedis.exists(ConstantProperty.REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY )){
			//只赋值
			getSupplierColorMap();
		}
		List<String> mapValue = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY ,supplierColor.toUpperCase()) ;
		if(null!=mapValue&&mapValue.size()>0){
			return mapValue.get(0);
		}else{
			return "";
		}
	}



	public Map<String, String> getSupplierHubBrandMap() {
		Map<String, String> supplierBrandStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY);
		if (supplierBrandStaticMap == null || supplierBrandStaticMap.size() < 1) {
			log.info("supplierBrand的redis为空");
			supplierBrandStaticMap = new HashMap<>();
			List<HubBrandDicDto> brandDTOS = dataServiceHandler.getBrand();
			for (HubBrandDicDto dto : brandDTOS) {
				if (dto.getSupplierBrand() != null) {
					supplierBrandStaticMap.put(dto.getSupplierBrand().toUpperCase(), dto.getHubBrandNo());
				}
			}
			shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY, supplierBrandStaticMap);
			shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY,
					ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 6 * 24);
		}
		return supplierBrandStaticMap;
	}

	public Map<String, String> getSupplierHubOriginMap() {
		Map<String, String> supplierOriginStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_SUPPLIER_ORIGIN_MAPPING_MAP_KEY);
		if (supplierOriginStaticMap == null || supplierOriginStaticMap.size() < 1) {
			log.info("supplierOrigin的redis为空");
			supplierOriginStaticMap = new HashMap<>();
			List<HubSupplierValueMappingDto> originDTOS = dataServiceHandler.getHubSupplierValueMappingByType(3);
			for (HubSupplierValueMappingDto dto : originDTOS) {
				if (dto.getSupplierVal() != null) {
					supplierOriginStaticMap.put(dto.getSupplierVal().toUpperCase(), dto.getHubVal());
				}
			}
			shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_SUPPLIER_ORIGIN_MAPPING_MAP_KEY, supplierOriginStaticMap);
			shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_SUPPLIER_ORIGIN_MAPPING_MAP_KEY,
					ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 6 * 24);
		}
		return supplierOriginStaticMap;
	}

	public String  getHubOriginFromRedis(String supplierOrigin){
		if(!shangpinRedis.exists(ConstantProperty.REDIS_EPHUB_SUPPLIER_ORIGIN_MAPPING_MAP_KEY )){
			//只赋值
			getSupplierHubOriginMap();
		}
		List<String> mapValue = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_SUPPLIER_ORIGIN_MAPPING_MAP_KEY ,supplierOrigin.toUpperCase()) ;
		if(null!=mapValue&&mapValue.size()>0){
			return mapValue.get(0);
		}else{
			return "";
		}
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
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 6 *12 );
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
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME *  6 *12);
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
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME *  6 *12);
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
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME *  6 *12);
			}
		}
		return firstStaticMap;
	}

	/**
	 * 获取儿童品类对应的品牌
	 * @return
	 */
	public Map<String, String> getKidBrandMap() {
		Map<String, String> kidBrandStaticMap = shangpinRedis
				.hgetAll(ConstantProperty.REDIS_EPHUB_KID_BRAND_MAPPING_MAP_KEY);
		if (kidBrandStaticMap == null || kidBrandStaticMap.size() < 1) {
			kidBrandStaticMap = new HashMap<>();
			List<HubSupplierValueMappingDto> supplierValueMappList = dataServiceHandler.getHubSupplierValueMappingByType(6);
			for (HubSupplierValueMappingDto supplierValueMapp : supplierValueMappList) {
				kidBrandStaticMap.put(supplierValueMapp.getSupplierVal(),supplierValueMapp.getHubVal());
			}
			if (kidBrandStaticMap.size() > 0) {
				shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_REPLACE_MATERIAL_MAPPING_MAP_KEY, kidBrandStaticMap);
				shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_REPLACE_MATERIAL_MAPPING_MAP_KEY,
						ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME * 6 * 24);
			}
		}
		return kidBrandStaticMap;
	}

}
