package com.shangpin.ephub.data.mysql.picture.pending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.picture.pending.mapper.HubSpuPendingPicMapper;

/**
 * <p>Title:HubSpuPendingPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:52:45
 */
@Service
public class HubSpuPendingPicService {

	@Autowired
	private HubSpuPendingPicMapper hubSpuPendingPicMapper;
}
