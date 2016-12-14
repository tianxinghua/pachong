package com.shangpin.ephub.data.mysql.spu.supplier.servcie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.spu.supplier.mapper.HubSupplierSpuMapper;

/**
 * <p>Title:HubSupplierSpuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:21:05
 */
@Service
public class HubSupplierSpuService {

	@Autowired
	private HubSupplierSpuMapper hubSupplierSpuMapper;
}
