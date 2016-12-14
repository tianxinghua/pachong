package com.shangpin.ephub.data.mysql.task.spuimport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.task.spuimport.mapper.HubSpuImportTaskMapper;

/**
 * <p>Title:HubSpuImportTaskService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:25:06
 */
@Service
public class HubSpuImportTaskService {

	@Autowired
	private HubSpuImportTaskMapper hubSpuImportTaskMapper;
}
