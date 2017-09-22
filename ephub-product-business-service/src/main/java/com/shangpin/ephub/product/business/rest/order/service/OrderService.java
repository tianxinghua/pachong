package com.shangpin.ephub.product.business.rest.order.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;

@Service
public class OrderService {
	
	@Autowired
	private HubSupplierSkuGateWay supplierSkuGateWay;

	public String findProductSize(String supplierId, String supplierSkuNo){
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.setFields("supplier_sku_size");
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSupplierSkuDto> list = supplierSkuGateWay.selectByCriteria(criteria );
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getSupplierSkuSize();
		}else{
			return "";
		}
	}
}
