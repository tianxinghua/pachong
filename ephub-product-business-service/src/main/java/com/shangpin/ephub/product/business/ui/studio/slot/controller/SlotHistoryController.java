package com.shangpin.ephub.product.business.ui.studio.slot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.slot.service.SlotHistoryService;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.export.SlotsSendDetailVo;

@RestController
@RequestMapping(value="/api/airstudio/slot-history")
public class SlotHistoryController {

	@Autowired
	private SlotHistoryService slotHistoryService;
	
	@RequestMapping(value="/download",method = RequestMethod.POST)
	public SlotsSendDetailVo download(@RequestBody String slotNo){
		return slotHistoryService.selectDetail(slotNo);
	}
	
	@RequestMapping(value="/import",method = RequestMethod.POST)
	public String importExcel(@RequestBody SlotsSendDetailVo detailVo){
		if(slotHistoryService.importExcel(detailVo)){
			return "{\"code\":0}";
		}else{
			return "{\"code\":1}";
		}
	}
	
	
}
