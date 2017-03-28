package com.shangpin.ephub.product.business.common.service.check.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.hubDic.category.HubCategoryDicService;
import com.shangpin.ephub.product.business.common.service.check.CommonCheckBase;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by zhaogenchun on 2017/03/06.
 * 校验品类
 */
@Component
public class CategoryCheck extends CommonCheckBase {

    static Map<String, Map<String, String>> supplierCategoryMappingStaticMap = null;
    static Map<String, String> hubCategoryMappingStaticMap = null;
	@Autowired
	HubSupplierCategroyDicGateWay hubSupplierCategroyDicGateWay;
	@Autowired
	HubCategoryDicService hubCategoryDicService;
    @Override
    protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception{
    	
    	if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getCatgoryState()!=null&&hubSpuPendingIsExist.getCatgoryState()==CatgoryState.PERFECT_MATCHED.getIndex()){
    		return null;
    	}
    	hubSpuPendingIsExist.setHubCategoryNo(spuPendingDto.getHubCategoryNo());
    	if(checkHubCategory(spuPendingDto.getHubCategoryNo(),spuPendingDto.getSupplierId())){
    		hubSpuPendingIsExist.setCatgoryState((byte)1);
		}else{
			hubSpuPendingIsExist.setCatgoryState((byte)0);
    		return "品类校验失败";
		}
        return null;
    }
    
    @Override
	protected boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception{
    	if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getCatgoryState()!=null&&hubSpuPendingIsExist.getCatgoryState()==CatgoryState.PERFECT_MATCHED.getIndex()){
    		return true;
    	}
		return setCategoryMapping(hubSpuPendingIsExist,spuPendingDto);
	}

    /**
	 * 校验品类
	 * @param categoryNo
	 * @return
	 */
	public boolean checkHubCategory(String categoryNo,String supplierId) throws Exception{
		
		if(hubCategoryMappingStaticMap==null){
			getCategoryMappingMap(supplierId);
		}
		if(hubCategoryMappingStaticMap!=null&&hubCategoryMappingStaticMap.containsKey(supplierId+"_"+categoryNo)){
			return true;
		}else {
			return false;
		}
	}
	
	 public boolean setCategoryMapping(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto hubSpuPending) throws Exception {
	        boolean result = true;
	        String categoryAndStatus = "";
	        Integer mapStatus = 0;
	        // 无品类 无性别时 直接返回
	        if (StringUtils.isBlank(hubSpuPending.getHubCategoryNo()) || StringUtils.isBlank(hubSpuPending.getHubGender())) {
	            return false;
	        }
	        Map<String, Map<String, String>> supplierCategoryMappingMap = this.getCategoryMappingMap(hubSpuPending.getSupplierId());
	        if (supplierCategoryMappingMap.containsKey(hubSpuPending.getSupplierId())) {
	            Map<String, String> categoryMappingMap = supplierCategoryMappingMap.get(hubSpuPending.getSupplierId());

	            if (categoryMappingMap.containsKey(
	            		hubSpuPending.getHubCategoryNo().trim().toUpperCase() + "_" + hubSpuPending.getHubGender().trim().toUpperCase())) {
	                // 包含时转化赋值
	                categoryAndStatus = categoryMappingMap.get(
	                		hubSpuPending.getHubCategoryNo().trim().toUpperCase() + "_" + hubSpuPending.getHubGender().trim().toUpperCase());
	                if (categoryAndStatus.contains("_")) {
	                	hubSpuPendingIsExist.setHubCategoryNo(categoryAndStatus.substring(0, categoryAndStatus.indexOf("_")));
	                    mapStatus = Integer.valueOf(categoryAndStatus.substring(categoryAndStatus.indexOf("_") + 1));
	                    hubSpuPendingIsExist.setCatgoryState(mapStatus.byteValue());
	                    if(hubSpuPending.getCatgoryState()!=null){
	                    	 if(hubSpuPending.getCatgoryState().intValue()!=InfoState.PERFECT.getIndex()){
	 	                        //未达到4级品类
	 	                        result = false;
	 	                    }
	                    }else{
	                    	result = false;
	                    }
	                   
	                } else {
	                    result = false;
	                    hubSpuPendingIsExist.setHubCategoryNo(hubSpuPending.getHubCategoryNo());
	                    hubSpuPendingIsExist.setCatgoryState(InfoState.IMPERFECT.getIndex());
	                }

	            } else {

	                result = false;
	                hubSpuPendingIsExist.setHubCategoryNo(hubSpuPending.getHubCategoryNo());
	                hubSpuPendingIsExist.setCatgoryState(InfoState.IMPERFECT.getIndex());
	                hubCategoryDicService.saveHubCategory(hubSpuPending.getSupplierId(), hubSpuPending.getHubCategoryNo(), hubSpuPending.getHubGender());

	            }
	        } else {
	            result = false;
	            hubSpuPendingIsExist.setHubCategoryNo(hubSpuPending.getHubCategoryNo());
	            hubSpuPendingIsExist.setCatgoryState(InfoState.IMPERFECT.getIndex());
	            hubCategoryDicService.saveHubCategory(hubSpuPending.getSupplierId(), hubSpuPending.getHubCategoryNo(), hubSpuPending.getHubGender());

	        }
	        return result;
	    }
	 
	 /**
	     * 按供货商获取品类的映射关系 主键 category_gender value 尚品的品类编号+"_"+匹配状态
	     *
	     * @param supplierId
	     * @return
	     */
	    protected Map<String, Map<String, String>> getCategoryMappingMap(String supplierId) throws Exception {
	        if (null == supplierCategoryMappingStaticMap) {// 初始化
	            supplierCategoryMappingStaticMap = new HashMap<>();
	            hubCategoryMappingStaticMap = new HashMap<>();
	            this.setSupplierCategoryValueToMap(supplierId);

	        } else {
	            if (!supplierCategoryMappingStaticMap.containsKey(supplierId)) {// 未包含
	                this.setSupplierCategoryValueToMap(supplierId);
	            } else {
	                if (isNeedHandle()) {// 包含 需要重新拉取
	                    this.setSupplierCategoryValueToMap(supplierId);
	                }
	            }
	        }
	        return supplierCategoryMappingStaticMap;
	    }
	    /**
	     * 冗余供货商的性别，直接查询
	     *
	     * @param supplierId
	     * @throws Exception
	     */
	    private void setSupplierCategoryValueToMap(String supplierId) throws Exception {

	        List<HubSupplierCategroyDicDto> hubSupplierCategroyDicDtos = hubCategoryDicService
	                .getSupplierCategoryBySupplierId(supplierId,1,ConstantProperty.MAX_COMMON_QUERY_NUM);
	        if (null != hubSupplierCategroyDicDtos && hubSupplierCategroyDicDtos.size() > 0) {
	            Map<String, String> categoryMap = new HashMap<>();
	            String spCategory = "";

	            for (HubSupplierCategroyDicDto dto : hubSupplierCategroyDicDtos) {
	                // map 的key 供货商的品类 + "_"+供货商的性别 ，value ： 尚品的品类 + "_"+ 匹配状态 (1
	                // :匹配到4级 2：可以匹配但未匹配到4级）
	                // if(hubGenderMap.containsKey(dto.getGenderDicId())){
	                if (StringUtils.isBlank(dto.getSupplierCategory()))
	                    continue;
	                if (StringUtils.isBlank(dto.getSupplierGender()))
	                    continue;
	                spCategory = (null == dto.getHubCategoryNo() ? "" : dto.getHubCategoryNo());
	                categoryMap.put(
	                        dto.getSupplierCategory().trim().toUpperCase() + "_"
	                                + dto.getSupplierGender().trim().toUpperCase(),
	                        spCategory + "_" + dto.getMappingState());
	                hubCategoryMappingStaticMap.put(dto.getSupplierId()+"_"+spCategory,"");
	                // }
	            }
	            supplierCategoryMappingStaticMap.put(supplierId, categoryMap);
	        }

	    }
	
}
