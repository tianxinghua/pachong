package com.shangpin.ephub.product.business.rest.hubpending.spu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.product.business.hubpending.spu.dto.NohandleReason;
import com.shangpin.ephub.product.business.ui.pending.service.HubSpuPendingNohandleReasonService;

@RestController
@RequestMapping(value = "/pending-nohandle-reason")
public class PendingNohandleReasonController {
	
	@Autowired
	private HubSpuPendingNohandleReasonService service;

	@RequestMapping(value = "/insert")
	public boolean insertNohandleReason(@RequestBody NohandleReason nohandleReason){
		return service.insertNohandleReason(nohandleReason);
	}
}
