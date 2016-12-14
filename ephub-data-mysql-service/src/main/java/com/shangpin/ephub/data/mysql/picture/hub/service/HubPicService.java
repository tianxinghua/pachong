package com.shangpin.ephub.data.mysql.picture.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.picture.hub.mapper.HubPicMapper;

/**
 * <p>Title:HubPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:50:03
 */
@Service
public class HubPicService {

	@Autowired
	private HubPicMapper hubPicMapper;
}
