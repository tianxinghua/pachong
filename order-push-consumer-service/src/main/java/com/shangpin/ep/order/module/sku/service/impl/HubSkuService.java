package com.shangpin.ep.order.module.sku.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.module.sku.bean.HubSku;
import com.shangpin.ep.order.module.sku.bean.HubSkuCriteria;
import com.shangpin.ep.order.module.sku.mapper.HubSkuMapper;
import com.shangpin.ep.order.module.sku.service.IHubSkuService;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:HubSku.java </p>
 * <p>Description: 涉及到一些sku的service方法 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:47:30
 */
@Service
@Slf4j
public class HubSkuService implements IHubSkuService {
	
	@Autowired
    private HubSkuMapper skuDAO;

	@Override
    public HubSku getSku(String supplierId,String supplierSkuId){
		try {
    		HubSkuCriteria skuCriteria  = new HubSkuCriteria();
        	skuCriteria.createCriteria().andSupplierIdEqualTo(supplierId).andSkuIdEqualTo(supplierSkuId);
        	return skuDAO.selectByExample(skuCriteria).get(0);
		} catch (Exception e) {	
			log.error("根据supplierId("+supplierId+")和供应商sku("+supplierSkuId+")查找产品失败："+e.getMessage(),e);
			return null;
		}
    }
}
