package com.shangpin.ephub.product.business.common.supplier.spu;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;

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
public class HubSupplierSpuService{
	
	@Autowired
	HubSupplierSpuGateWay hubSupplierSpuGateWay;
	
	public void updateHubSupplierSpuByPrimaryKey(HubSupplierSpuDto hubPendingSpuDto){
		hubSupplierSpuGateWay.updateByPrimaryKeySelective(hubPendingSpuDto);
	}
	public void updateHubSpuPending(HubSupplierSpuCriteriaDto criteria,Byte infoState){
		HubSupplierSpuWithCriteriaDto hubSpuPendingWithCriteriaDto = new HubSupplierSpuWithCriteriaDto();
		HubSupplierSpuDto hubSupplierSpu = new HubSupplierSpuDto();
		hubSupplierSpu.setInfoState(infoState);
		hubSpuPendingWithCriteriaDto.setHubSupplierSpu(hubSupplierSpu);
		hubSpuPendingWithCriteriaDto.setCriteria(criteria);
		hubSupplierSpuGateWay.updateByCriteriaSelective(hubSpuPendingWithCriteriaDto);
	}
	public HubSupplierSpuDto selectHubSupplierSpuById(Long supplierSpuId) {
		return hubSupplierSpuGateWay.selectByPrimaryKey(supplierSpuId);
	}
	public Long insert(HubSupplierSpuDto supplierSpu) {
		return hubSupplierSpuGateWay.insertSelective(supplierSpu);
	}
}
