package com.shangpin.ephub.data.mysql.spu.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.spu.hub.mapper.HubSpuMapper;

/**
 * <p>Title:HubSpuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:17:25
 */
@Service
public class HubSpuService {

	@Autowired
	private HubSpuMapper hubSpuMapper;
}
