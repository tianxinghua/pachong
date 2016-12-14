package com.shangpin.ephub.data.mysql.sku.pending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.sku.pending.mapper.HubSkuPendingMapper;

/**
 * <p>Title:HubSkuPendingService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:12:15
 */
@Service
public class HubSkuPendingService {

	@Autowired
	private HubSkuPendingMapper hubSkuPendingMapper;
}
