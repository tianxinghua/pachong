package com.shangpin.ephub.product.business.ui.studio.slot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.SlotManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.service.SlotManageService;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title: OpenBoxController</p>
 * <p>Description: 批次管理接口</p>
 * <p>Company: </p> 
 * @author zhaogenchun
 * @date 2017年6月12日 
 *
 */
@RestController
@RequestMapping("/slot-manage")
public class SlotManageController {

	@Autowired
	SlotManageService slotManageService;
	@RequestMapping(value="/slot-list",method = RequestMethod.POST)
	public HubResponse<?> slotList(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.findSlotManageList(slotManageQuery);
	}
	@RequestMapping(value="/slot-detail",method = RequestMethod.POST)
	public HubResponse<?> slotDetail(@RequestBody String slotNo){
		return null;
	}
	@RequestMapping(value="/slot-export",method = RequestMethod.POST)
	public HubResponse<?> slotDetailCheck(@RequestBody SlotManageQuery slotManageQuery){
		return null;
	}
	
	@RequestMapping(value="/save-return",method = RequestMethod.POST)
	public HubResponse<?> saveSlotReturnDetailAndMaster(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.createSlotReturnDetailAndMaster(slotManageQuery);
	}
	
	@RequestMapping(value="/return-master",method = RequestMethod.POST)
	public HubResponse<?> selectSlotReturnMaster(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.selectSlotReturnMaster(slotManageQuery);
	}
	
	@RequestMapping(value="/return-slot",method = RequestMethod.POST)
	public HubResponse<?> selectSlotReturnDetail(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.selectSlotReturnDetail(slotManageQuery);
	}
}
