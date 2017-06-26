package com.shangpin.ephub.product.business.ui.studio.slot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.product.business.ui.studio.slot.dto.SlotManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.service.IStudioSlotService;
import com.shangpin.ephub.response.HubResponse;
/**
 * @author wangchao
 * @date 2017年06月26日 下午12:16:41
 *
 */
@RestController
@RequestMapping("/studio-export")
public class StudioSlotExportController {
	
	@Autowired
	private IStudioSlotService studioSlotService;

	@RequestMapping(value="/slot",method=RequestMethod.POST)
	public HubResponse<?> exportSpu(@RequestBody SlotManageQuery slotManageQuery){
		return studioSlotService.exportSpu(slotManageQuery,TaskType.EXPORT_SUTDIO_SLOT);
	}
}
