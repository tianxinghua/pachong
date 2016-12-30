package com.shangpin.ephub.product.business.ui.pending.controller;

import com.shangpin.ephub.product.business.service.pending.PendingService;
import com.shangpin.ephub.product.business.ui.pending.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.product.business.ui.pending.service.impl.PendingProductService;
import com.shangpin.ephub.response.HubResponse;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pending-audit")
@Slf4j
public class PendingAuditController {
	
	@Autowired
	PendingService pendingService;


	@RequestMapping(value="query-spumodel",method=RequestMethod.POST)
	public HubResponse querySpuModel(@RequestBody SpuPendingAuditQueryVO queryVO){

		SpuModelMsgVO spuModel = pendingService.getSpuModel(queryVO);

		return HubResponse.successResp(spuModel);


	}


	@RequestMapping(value="query-spupending",method=RequestMethod.POST)
	public HubResponse querySpuPending(@RequestBody SpuModelVO queryVO){

		List<SpuPendingVO> spuPendingVOList = pendingService.getSpuPendingByBrandNoAndSpuModel(queryVO.getBrandNo(), queryVO.getSpuModel());
		return HubResponse.successResp(spuPendingVOList);
	}


	@RequestMapping(value="audit-spupendig",method=RequestMethod.POST)
	public HubResponse querySpuPending(@RequestBody SpuPendingAuditVO auditVO){

		try {
			pendingService.audit(auditVO);
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("待审核失败 ：" + " reason :" +  e.getMessage(),e);
			return HubResponse.errorResp(e.getMessage());
		}
		return HubResponse.successResp(true);
	}


}
