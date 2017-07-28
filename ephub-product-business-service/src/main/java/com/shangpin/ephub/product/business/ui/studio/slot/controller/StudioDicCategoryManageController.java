package com.shangpin.ephub.product.business.ui.studio.slot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.slot.dto.StudioManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.service.StudioDicCategoryManageService;
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
@RequestMapping(value={"/studiocategory-manage"})
public class StudioDicCategoryManageController {

	@Autowired
	StudioDicCategoryManageService studioDicCategoryManageService;
	
	@RequestMapping(value="/add-studiocategory",method = RequestMethod.POST)
	public HubResponse<?> addStudioCategory(@RequestBody StudioManageQuery studioManageQuery){
		return studioDicCategoryManageService.addStudioCategory(studioManageQuery);
	}
	
	@RequestMapping(value="/update-studiocategory",method = RequestMethod.POST)
	public HubResponse<?> updateStudioCategory(@RequestBody StudioManageQuery studioManageQuery){
		return studioDicCategoryManageService.updateStudioCategory(studioManageQuery);
	}
	
	@RequestMapping(value="/select-studiocategory",method = RequestMethod.POST)
	public HubResponse<?> selectStudioCategory(@RequestBody StudioManageQuery studioManageQuery){
		return studioDicCategoryManageService.selectStudioCategory(studioManageQuery);
	}
}
