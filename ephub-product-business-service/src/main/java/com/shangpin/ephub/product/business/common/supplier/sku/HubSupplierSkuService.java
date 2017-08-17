package com.shangpin.ephub.product.business.common.supplier.sku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;

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
public class HubSupplierSkuService{
	
	@Autowired
	HubSupplierSkuGateWay hubSupplierSkuGateWay;
	
	public void updateHubSupplierSpuByPrimaryKey(HubSupplierSkuDto hubPendingSkuDto){
		hubSupplierSkuGateWay.updateByPrimaryKeySelective(hubPendingSkuDto);
	}
	
	public List<HubSupplierSkuDto> selectListBySupplierIdAndSize(HubSupplierSkuCriteriaDto criteria){
		return hubSupplierSkuGateWay.selectByCriteria(criteria);
	}

	public List<HubSupplierSkuDto> selectListSkuBySpuId(
			Long supplierSpuId) {
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
		return hubSupplierSkuGateWay.selectByCriteria(criteria);
	}

	public void insertSku(HubSupplierSkuDto supplierSku) {
		hubSupplierSkuGateWay.insertSelective(supplierSku);		
	}
}
