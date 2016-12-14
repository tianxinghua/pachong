package com.shangpin.ephub.data.mysql.supplier.config.servcie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.supplier.config.mapper.HubSupplierConfigMapper;

/**
 * <p>Title:HubSupplierConfigService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:22:51
 */
@Service
public class HubSupplierConfigService {

	@Autowired
	private HubSupplierConfigMapper hubSupplierConfigMapper;
}
