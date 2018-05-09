package com.shangpin.ephub.product.business.rest.order.service;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
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

	@Autowired
	private HubSupplierSpuGateWay supplierSpuGateWay;

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


	public HubSupplierSpuDto getSupplierSpu(String supplierID,String supplierSpuNo){

		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		criteria.setFields("supplier_spu_no ,supplier_spu_model , supplier_brandname ,supplier_categoryname ");
		criteria.createCriteria().andSupplierIdEqualTo(supplierID).andSupplierSpuNoEqualTo(supplierSpuNo);
		List<HubSupplierSpuDto> hubSupplierSpuDtos = supplierSpuGateWay.selectByCriteria(criteria);
		if(null!=hubSupplierSpuDtos&&hubSupplierSpuDtos.size()>0){
			return hubSupplierSpuDtos.get(0);
		}else{
			return null;
		}
	}
}
