package com.shangpin.ephub.product.business.ui.studio.slot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.slot.dto.StudioManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.service.StudioDicSlotManageService;
import com.shangpin.ephub.response.HubResponse;

/**
 * <p>Title: OpenBoxController</p>
 * <p>Description: 批次基础信息管理</p>
 * <p>Company: </p> 
 * @author wangchao
 * @date 2017年7月24日 
 *
 */
@RestController
@RequestMapping(value={"/studiodicslot-manage"})
public class StudioDicSlotManageController {

	@Autowired
	StudioDicSlotManageService studioSlotManageService;
	
	@RequestMapping(value="/update-studiodic",method = RequestMethod.POST)
	public HubResponse<?> updateStudio(@RequestBody StudioManageQuery studioManageQuery){
		return studioSlotManageService.updateStudioDicSlot(studioManageQuery);
	}
	
	@RequestMapping(value="/select-studiodic",method = RequestMethod.POST)
	public HubResponse<?> selectStudio(@RequestBody StudioManageQuery studioManageQuery){
		return studioSlotManageService.selectStudioDicSlot(studioManageQuery);
	}
}
