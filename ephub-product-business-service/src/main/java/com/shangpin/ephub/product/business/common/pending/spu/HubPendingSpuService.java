package com.shangpin.ephub.product.business.common.pending.spu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;

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
public class HubPendingSpuService{
	
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	
	public void updateHubSpuPendingByPrimaryKey(HubSpuPendingDto hubPendingSpuDto){
		hubSpuPendingGateWay.updateByPrimaryKeySelective(hubPendingSpuDto);
	}
	public void updateHubSpuPending(HubSpuPendingDto hubPendingSpuDto,HubSpuPendingCriteriaDto criteria){
		HubSpuPendingWithCriteriaDto hubSpuPendingWithCriteriaDto = new HubSpuPendingWithCriteriaDto();
		hubSpuPendingWithCriteriaDto.setHubSpuPending(hubPendingSpuDto);
		hubSpuPendingWithCriteriaDto.setCriteria(criteria);
		hubSpuPendingGateWay.updateByCriteriaSelective(hubSpuPendingWithCriteriaDto);
		
		hubSpuPendingGateWay.updateByPrimaryKeySelective(hubPendingSpuDto);
	}
	public Long insertHubSpuPending(HubSpuPendingDto hubPendingSpuDto){
		return hubSpuPendingGateWay.insert(hubPendingSpuDto);
	}

	public HubSpuPendingDto findHubSpuPendingBySupplierIdAndSupplierSpuNo(String supplierId, String supplierSpuNo) {
		
		HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
		List<HubSpuPendingDto> listSpu = hubSpuPendingGateWay.selectByCriteria(criteria);
		if(listSpu!=null&&listSpu.size()>0){
			return listSpu.get(0);
		}
		return null;
	}
	
	public HubSpuPendingDto findHubSpuPendingBySpuModelAndBrandNoAndSpuState(String spuModel,String hubBrandNo,Byte spuState) {
		HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
		criteria.createCriteria().andSpuModelEqualTo(spuModel).andHubBrandNoEqualTo(hubBrandNo).andSpuStateEqualTo(spuState);
		List<HubSpuPendingDto> listSpu = hubSpuPendingGateWay.selectByCriteria(criteria);
		if(listSpu!=null&&listSpu.size()>0){
			return listSpu.get(0);
		}
		return null;
	}
	
	public HubSpuPendingDto findHubSpuPendingByPrimary(Long spuPendingId) {
		HubSpuPendingDto listSpu = hubSpuPendingGateWay.selectByPrimaryKey(spuPendingId);
		return listSpu;
	}
}
