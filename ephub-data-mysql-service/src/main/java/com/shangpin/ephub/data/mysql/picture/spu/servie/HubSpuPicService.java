package com.shangpin.ephub.data.mysql.picture.spu.servie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.picture.spu.mapper.HubSpuPicMapper;

/**
 * <p>Title:HubSpuPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:54:30
 */
@Service
public class HubSpuPicService {

	@Autowired
	private HubSpuPicMapper hubSpuPicMapper;
}
