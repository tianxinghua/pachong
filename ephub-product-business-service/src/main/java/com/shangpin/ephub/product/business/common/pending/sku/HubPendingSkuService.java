package com.shangpin.ephub.product.business.common.pending.sku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: hua商品校验实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubPendingSkuService{
	
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	
	public void updateHubSkuPendingByPrimaryKey(HubSkuPendingDto hubPendingSkuDto){
		hubSkuPendingGateWay.updateByPrimaryKeySelective(hubPendingSkuDto);
	}
	public void updateHubSkuPending(HubSkuPendingDto hubPendingSkuDto,HubSkuPendingCriteriaDto criteria){
		HubSkuPendingWithCriteriaDto hubSkuPendingWithCriteriaDto = new HubSkuPendingWithCriteriaDto();
		hubSkuPendingWithCriteriaDto.setHubSkuPending(hubPendingSkuDto);
		hubSkuPendingWithCriteriaDto.setCriteria(criteria);
		hubSkuPendingGateWay.updateByCriteriaSelective(hubSkuPendingWithCriteriaDto);
		
		hubSkuPendingGateWay.updateByPrimaryKeySelective(hubPendingSkuDto);
	}
	public void insertHubSkuPending(HubSkuPendingDto hubPendingSkuDto){
		 hubSkuPendingGateWay.insert(hubPendingSkuDto);
}

	public HubSkuPendingDto findHubSkuPendingBySupplierIdAndSupplierSkuNo(String supplierId, String supplierSkuNo) {
		
		if(supplierSkuNo!=null&&supplierId!=null){
			HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
			criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
			List<HubSkuPendingDto> listSku = hubSkuPendingGateWay.selectByCriteria(criteria);
			if(listSku!=null&&listSku.size()>0){
				return listSku.get(0);
			}
		}
		return null;
	}
	public List<HubSkuPendingDto> findHubSkuPendingBySpuPendingId(Long spuPendingId) {
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
		criteria.setPageNo(1);
		criteria.setPageSize(10000);
		List<HubSkuPendingDto> listSku = hubSkuPendingGateWay.selectByCriteria(criteria);
		if(listSku!=null&&listSku.size()>0){
			return listSku;
		}
		return null;
	}
	
}
