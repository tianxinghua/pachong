package com.shangpin.pending.product.consumer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubFilterRequest;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubFilterResponse;
import com.shangpin.ephub.client.data.mysql.hub.gateway.HubFilterGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HubFilterService {
	
	private static List<String> supplierIds = new ArrayList<String>();
	static{
		//stefaniamode
		supplierIds.add("2015092401528");
		//atelierO
		supplierIds.add("2015082701461");
		//tony
		supplierIds.add("2015101501608");
		//monnierfreres
		supplierIds.add("2015101001587");
		//brunarosso
		supplierIds.add("2015091801507");
		//Tessabit
		supplierIds.add("2015091701503");
		//Clutcher
		supplierIds.add("2015102201625");
		//Giglio
		supplierIds.add("2015091801508");
		//coltorti
		supplierIds.add("2015081701440");
	}
	@Autowired
	IShangpinRedis shangpinRedis;
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateway;
	@Autowired
	HubFilterGateWay hubFilterGateWay;
	public Map<String, String> refreshHubFilter(String hubCategoryNo) {
	    Map<String, String>  categoryMap = new HashMap<>() ;
	    List<HubFilterResponse> supplierCategoryMappingDtos = getAllBrandNoByHubCategoryNo(hubCategoryNo);
        if (null != supplierCategoryMappingDtos && supplierCategoryMappingDtos.size() > 0) {
            for (HubFilterResponse dto : supplierCategoryMappingDtos) {
            	 if (StringUtils.isBlank(dto.getHubBrandNo()))
                     continue;
                 categoryMap.put(dto.getHubBrandNo(),hubCategoryNo);
            }
        }
	    if(categoryMap.size()>0){
	    	HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
	    	List<Byte> liByte = new ArrayList<Byte>();
	    	liByte.add((byte)0);
	    	liByte.add((byte)4);
			criteria.createCriteria().andHubCategoryNoLike(hubCategoryNo+"%").andSpuBrandStateEqualTo((byte)1).
			andSpuStateIn(liByte).andSupplierIdNotIn(supplierIds);
			int totalSize = hubSpuPendingGateway.countByCriteria(criteria);
			int pageCount = getPageCount(totalSize, 100);// 页数
			for (int i = 1; i <= pageCount; i++) {
				criteria.setPageNo(i);
				criteria.setPageSize(100);
				List<HubSpuPendingDto> list = hubSpuPendingGateway.selectByCriteria(criteria);
				if(list!=null&&list.size()>0){
					for(HubSpuPendingDto spu:list){
						if(categoryMap.containsKey(spu.getHubBrandNo())){
							if(spu.getSpuState()==4){
								updateSpuStateFilter(spu.getSpuPendingId(),(byte)0);
							}
						}else{
							updateSpuStateFilter(spu.getSpuPendingId(),(byte)4);
						}
					}
				}
			}
	    }
	
//		Map<String, String> supplierMap = shangpinRedis.hgetAll(ConstantProperty.REDIS_EPHUB_CATEGORY_BRNAD_MAPPING_MAP_KEY+hubCategoryNo);
//        if(supplierMap==null||supplierMap.size()<1){
//        	log.info("redis为空");
//            List<HubCategroyBrandDto> supplierCategoryMappingDtos = getAllBrandNoByHubCategoryNo(hubCategoryNo);
//            if (null != supplierCategoryMappingDtos && supplierCategoryMappingDtos.size() > 0) {
//                for (HubCategroyBrandDto dto : supplierCategoryMappingDtos) {
//                	 if (StringUtils.isBlank(dto.getHubBrandNo()))
//                         continue;
//                     categoryMap.put(dto.getHubBrandNo(),hubCategoryNo);
//                }
////                shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_CATEGORY_BRNAD_MAPPING_MAP_KEY+"_"+hubCategoryNo,categoryMap);
////                shangpinRedis.expire(ConstantProperty.REDIS_EPHUB_CATEGORY_BRNAD_MAPPING_MAP_KEY+"_"+hubCategoryNo,ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME*1000);
//            }
//        }
        return categoryMap;
	}
	/**
	 * 获取总页数
	 * 
	 * @param totalSize
	 *            总计路数
	 * @param pagesize
	 *            每页记录数
	 * @return
	 */
	public Integer getPageCount(Integer totalSize, Integer pageSize) {
		if (totalSize % pageSize == 0) {
			return totalSize / pageSize;
		} else {
			return (totalSize / pageSize) + 1;
		}
	}

	private void updateSpuStateFilter(Long spuPendingId,Byte spuState) {
		HubSpuPendingDto spu = new HubSpuPendingDto();
		spu.setSpuPendingId(spuPendingId);
		spu.setSpuState(spuState);
		spu.setMemo("程序自动过滤不要的品牌");
		spu.setUpdateTime(new Date());
		hubSpuPendingGateway.updateByPrimaryKeySelective(spu);
	}

	private List<HubFilterResponse> getAllBrandNoByHubCategoryNo(String hubCategoryNo) {
		HubFilterRequest request = new HubFilterRequest();
		request.setFilterType((byte)1);
		request.setHubCategoryNo(hubCategoryNo);
		return hubFilterGateWay.select(request);
	}
}