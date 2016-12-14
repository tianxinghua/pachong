package com.shangpin.ephub.data.mysql.spu.pending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.spu.pending.mapper.HubSpuPendingMapper;

/**
 * <p>Title:HubSpuPendingService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:19:15
 */
@Service
public class HubSpuPendingService {

	@Autowired
	private HubSpuPendingMapper hubSpuPendingMapper;
}
