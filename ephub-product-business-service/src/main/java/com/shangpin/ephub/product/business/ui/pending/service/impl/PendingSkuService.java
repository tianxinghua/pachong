package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * 待处理页面逻辑中处理sku的实现类
 * <p>Title: PendingSkuService</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月1日 下午5:59:59
 *
 */
@Service
@Slf4j
public abstract class PendingSkuService extends PendingSpuService{

	@Override
    public HubResponse<?> exportSku(PendingQuryDto pendingQuryDto){
    	try {
    		HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
            int total = hubSpuPendingGateWay.countByCriteria(criteriaDto);
            pendingQuryDto.setPageSize(total);
        	HubSpuImportTaskDto taskDto = saveTaskIntoMysql(pendingQuryDto.getCreateUser(),TaskType.EXPORT_PENDING_SKU.getIndex());
        	sendMessageToTask(taskDto.getTaskNo(),TaskType.EXPORT_PENDING_SKU.getIndex(),JsonUtil.serialize(pendingQuryDto)); 
        	return HubResponse.successResp(taskDto.getTaskNo()+":"+pendingQuryDto.getCreateUser()+"_" + taskDto.getTaskNo()+".xls");
		} catch (Exception e) {
			log.error("导出sku失败，服务器发生错误:"+e.getMessage(),e);
			return HubResponse.errorResp("导出失败，服务器发生错误");
		}
    }
	
	@Override
    public Map<Long,List<HubSkuPendingDto>> findPendingSku(List<Long> spuPendingIds,boolean allFlag) throws Exception{
    	Map<Long,List<HubSkuPendingDto>> pendigSkus = new HashMap<>();
    	try {
            HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
            criteriaDto.setPageNo(1);
            criteriaDto.setPageSize(1000); 
            criteriaDto.setOrderByClause("spu_pending_id,hub_sku_size");
            if(allFlag){
            	 criteriaDto.createCriteria().andSpuPendingIdIn(spuPendingIds);
            }else{
            	List<Byte> listSkuState = new ArrayList<Byte>();
                listSkuState.add(SpuState.HANDLED.getIndex());
                criteriaDto.createCriteria().andSpuPendingIdIn(spuPendingIds).andSkuStateNotIn(listSkuState).andHubSkuNoIsNull();
                criteriaDto.or(criteriaDto.createCriteria().andSpuPendingIdIn(spuPendingIds).andSkuStateIsNull().andHubSkuNoIsNull());
            }
            List<HubSkuPendingDto> skus = hubSkuPendingGateWay.selectByCriteria(criteriaDto);
            if(CollectionUtils.isNotEmpty(skus)){
            	for(HubSkuPendingDto sku : skus){
            		if(FilterFlag.INVALID.getIndex() == sku.getFilterFlag()){
            			sku.setHubSkuSizeType("排除"); 
            		}
            		Long spuPendingId = sku.getSpuPendingId();
					if(pendigSkus.containsKey(spuPendingId)){
						pendigSkus.get(spuPendingId).add(sku);
            		}else{
            			List<HubSkuPendingDto> list = new ArrayList<>();
            			list.add(sku);
            			pendigSkus.put(spuPendingId, list);
            		}
            	}
            }
        } catch (Exception e) {
            log.error("pending表根据spu id查询sku时出错："+e.getMessage(),e);
        }
    	return pendigSkus;
    }
}
