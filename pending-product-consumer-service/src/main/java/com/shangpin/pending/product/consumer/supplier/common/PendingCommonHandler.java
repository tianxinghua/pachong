package com.shangpin.pending.product.consumer.supplier.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.DateUtils;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.supplier.dto.ColorDTO;

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

    static Integer isCurrentMin  =   DateUtils.getCurrentMin();

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
    public  Map<String, String>  getSpCategoryValue(String supplierId) throws Exception {
    	
        //先判断设置的是否有值  无值得话  品类重新处理
//    	shangpinRedis.del(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY);
    	Map<String, String> supplierMap = shangpinRedis.hgetAll(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY+"_"+supplierId);
        if(supplierMap==null||supplierMap.size()<1){
        	log.info("redis为空");
            Map<String, String>  categoryMap = new HashMap<>() ;
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
                shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY+"_"+supplierId,categoryMap);
                shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_SUPPLIER_KEY+"_"+supplierId,ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME*1000);
            }
            return categoryMap;
        }
        return supplierMap;

    }

    /**
     * 如果内部Map的成员很多，那么涉及到遍历整个内部Map的操作，
     * 由于Redis单线程模型的缘故，这个遍历操作可能会比较耗时，而另其它客户端的请求完全不响应，这点需要格外注意
     * @return
     */
    @Deprecated
    private Map<String,String> getCategoryMapFromRedis(){
        Long start = System.currentTimeMillis();
        Map<String,String> map =  shangpinRedis.hgetAll(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY);
        log.debug("获取品类对照耗时：" + (System.currentTimeMillis() - start )+" 毫秒");
        return   map;
    }
    public String  getSpCategoryValueFromRedis(String supplierCategory,String gender){
        List<String> mapValue = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY,supplierCategory.trim().toUpperCase()+"_"+gender.trim().toUpperCase()) ;
        if(null!=mapValue&&mapValue.size()>0){
            return mapValue.get(0);
        }else{
            return supplierCategory;
        }
    }




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
    
    public boolean getCategoryMap(PendingSpu spu,HubSpuPendingDto hubSpuPending) throws Exception{
    	 boolean result = true;
         String categoryAndStatus = "";
         Integer mapStatus = 0;
    	 Map<String, String> categoryMappingMap = this.getSpCategoryValue(spu.getSupplierId());
         if (categoryMappingMap.containsKey(
        		 spu.getHubCategoryNo().trim().toUpperCase() + "_" + spu.getHubGender().trim().toUpperCase())) {
             // 包含时转化赋值
             categoryAndStatus = categoryMappingMap.get(
            		 spu.getHubCategoryNo().trim().toUpperCase() + "_" + spu.getHubGender().trim().toUpperCase());
             if (categoryAndStatus.contains("_")) {
                 hubSpuPending.setHubCategoryNo(categoryAndStatus.substring(0, categoryAndStatus.indexOf("_")));
                 mapStatus = Integer.valueOf(categoryAndStatus.substring(categoryAndStatus.indexOf("_") + 1));
                 hubSpuPending.setCatgoryState(mapStatus.byteValue());
                 if(hubSpuPending.getCatgoryState().intValue()!=PropertyStatus.MESSAGE_HANDLED.getIndex()){
                     //未达到4级品类
                     result = false;
                 }
             } else {
                 result = false;
                 hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
             }
         } 
         return result;
    }

	public void getColorMap(PendingSpu spuPendingDto, HubSpuPendingDto updateSpuPending) {
		
	}
	
	  protected Map<String, String> getSupplierColorMap() {
		  Map<String, String> colorStaticMap = shangpinRedis.hgetAll(ConstantProperty.REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY);
          if(colorStaticMap==null||colorStaticMap.size()<1){
        	log.info("redis为空");
             colorStaticMap = new HashMap<>() ;
            List<ColorDTO> colorDTOS = dataServiceHandler.getColorDTO();
            for (ColorDTO dto : colorDTOS) {
            	if(StringUtils.isNotBlank(dto.getSupplierColor())&&StringUtils.isNotBlank(dto.getHubColorName())){
            		 colorStaticMap.put(dto.getSupplierColor().toUpperCase(), dto.getHubColorName());
            	}
            }
                shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY,colorStaticMap);
                shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_SUPPLIER_COLOR_MAPPING_MAP_KEY,ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME*1000);
            }
	        return colorStaticMap;
	    }
	  protected Map<String, String> getHubColorMap() {
		  
		  Map<String, String> hubColorStaticMap = shangpinRedis.hgetAll(ConstantProperty.REDIS_EPHUB_HUB_COLOR_MAPPING_MAP_KEY);
	        if(hubColorStaticMap==null||hubColorStaticMap.size()<1){
	        	log.info("redis为空");
	            hubColorStaticMap = new HashMap<>() ;
	            List<ColorDTO> colorDTOS = dataServiceHandler.getColorDTO();
	            for (ColorDTO dto : colorDTOS) {
	            	if(dto.getSupplierColor()!=null){
	                     hubColorStaticMap.put(dto.getHubColorName(), "");
	            	}
	            }
	                shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_HUB_COLOR_MAPPING_MAP_KEY,hubColorStaticMap);
	                shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_HUB_COLOR_MAPPING_MAP_KEY,ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME*1000);
	            }
	        return hubColorStaticMap;
	    }
}
