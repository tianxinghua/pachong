package com.shangpin.ephub.data.mysql.task.spuimport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.task.spuimport.service.HubSpuImportTaskService;

/**
 * <p>Title:HubSpuImportTaskController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:25:30
 */
@RestController
@RequestMapping("/hub-spu-import-task")
public class HubSpuImportTaskController {

	@Autowired
	private HubSpuImportTaskService hubSpuImportTaskService;
}
