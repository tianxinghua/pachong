package com.shangpin.ephub.data.mysql.sku.supplier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.sku.supplier.mapper.HubSupplierSkuMapper;

/**
 * <p>Title:HubSupplierSkuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:14:20
 */
@Service
public class HubSupplierSkuService {

	@Autowired
	private HubSupplierSkuMapper hubSupplierSkuMapper;
}
