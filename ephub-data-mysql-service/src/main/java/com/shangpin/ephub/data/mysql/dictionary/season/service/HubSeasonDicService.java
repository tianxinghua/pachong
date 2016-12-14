package com.shangpin.ephub.data.mysql.dictionary.season.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.season.mapper.HubSeasonDicMapper;

/**
 * <p>Title:HubSeasonDicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:36:33
 */
@Service
public class HubSeasonDicService {

	@Autowired
	private HubSeasonDicMapper hubSeasonDicMapper;
}
