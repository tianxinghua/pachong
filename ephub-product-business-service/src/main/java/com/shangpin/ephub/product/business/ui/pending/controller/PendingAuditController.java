package com.shangpin.ephub.product.business.ui.pending.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.product.business.ui.pending.service.impl.PendingProductService;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuModelVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditQueryVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditVO;
import com.shangpin.ephub.response.HubResponse;

@Controller
@RequestMapping("/pending-audit")
public class PendingAuditController {
	
	private static String resultSuccess = "{\"result\":\"success\"}";
	private static String resultFail = "{\"result\":\"fail\"}";
	
	@Autowired
	private PendingProductService pendingProductService;



	@RequestMapping(value="query-spumodel",method=RequestMethod.POST)
	public HubResponse querySpuModel(@RequestBody SpuPendingAuditQueryVO queryVO){

		return HubResponse.successResp(null);
	}


	@RequestMapping(value="query-spupending",method=RequestMethod.POST)
	public HubResponse querySpuPending(@RequestBody SpuModelVO queryVO){

		return HubResponse.successResp(null);
	}


	@RequestMapping(value="audit-spupendig",method=RequestMethod.POST)
	public HubResponse querySpuPending(@RequestBody SpuPendingAuditVO auditVO){

		return HubResponse.successResp(null);
	}


}
