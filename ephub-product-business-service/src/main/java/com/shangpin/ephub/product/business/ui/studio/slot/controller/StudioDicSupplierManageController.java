package com.shangpin.ephub.product.business.ui.studio.slot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.slot.dto.StudioManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.service.StudioDicSupplierManageService;
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
@RequestMapping(value={"/studioDicSupplier-manage"})
public class StudioDicSupplierManageController {

	@Autowired
	StudioDicSupplierManageService studioDicSupplierManageService;
	
	@RequestMapping(value="/add-studioDicSupplier",method = RequestMethod.POST)
	public HubResponse<?> addStudioDicSupplier(@RequestBody StudioManageQuery studioManageQuery){
		return studioDicSupplierManageService.addStudioDicSupplier(studioManageQuery);
	}
	
	@RequestMapping(value="/update-studioDicSupplier",method = RequestMethod.POST)
	public HubResponse<?> updateStudioDicSupplier(@RequestBody StudioManageQuery studioManageQuery){
		return studioDicSupplierManageService.updateStudioDicSupplier(studioManageQuery);
	}
	
	@RequestMapping(value="/select-studioDicSupplier",method = RequestMethod.POST)
	public HubResponse<?> selectStudioDicSupplier(@RequestBody StudioManageQuery studioManageQuery){
		return studioDicSupplierManageService.selectStudioDicSupplier(studioManageQuery);
	}
}
