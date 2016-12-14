package com.shangpin.ephub.data.mysql.sku.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.sku.hub.mapper.HubSkuMapper;

/**
 * <p>Title:HubSkuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubSkuService {

	@Autowired
	private HubSkuMapper hubSkuMapper;
}
