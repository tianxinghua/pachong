package com.shangpin.ephub.product.business.ui.studio.slot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.slot.dto.StudioManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.service.StudioManageService;
import com.shangpin.ephub.response.HubResponse;

/**
 * <p>Title: OpenBoxController</p>
 * <p>Description: 摄影棚基础信息管理</p>
 * <p>Company: </p> 
 * @author wangchao
 * @date 2017年7月24日 
 *
 */
@RestController
@RequestMapping(value={"/studio-manage"})
public class StudioManageController {

	@Autowired
	StudioManageService studioManageService;
	
	@RequestMapping(value="/add-studio",method = RequestMethod.POST)
	public HubResponse<?> addStudio(@RequestBody StudioManageQuery studioManageQuery){
		return studioManageService.addStudio(studioManageQuery);
	}
	
	@RequestMapping(value="/update-studio",method = RequestMethod.POST)
	public HubResponse<?> updateStudio(@RequestBody StudioManageQuery studioManageQuery){
		return studioManageService.updateStudio(studioManageQuery);
	}
	
	@RequestMapping(value="/select-studio",method = RequestMethod.POST)
	public HubResponse<?> selectStudio(@RequestBody StudioManageQuery studioManageQuery){
		return studioManageService.selectStudio(studioManageQuery);
	}
}
