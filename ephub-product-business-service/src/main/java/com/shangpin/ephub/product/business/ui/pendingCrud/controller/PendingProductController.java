package com.shangpin.ephub.product.business.ui.pendingCrud.controller;

import java.util.List;

import com.shangpin.ephub.product.business.ui.pendingCrud.vo.SpuPendingAuditQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.product.business.ui.pendingCrud.dto.Ids;
import com.shangpin.ephub.product.business.ui.pendingCrud.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pendingCrud.service.impl.PendingProductService;
import com.shangpin.ephub.product.business.ui.pendingCrud.vo.PendingProductDto;
import com.shangpin.ephub.response.HubResponse;

@Controller
@RequestMapping("/pending-product")
public class PendingProductController {
	
	private static String resultSuccess = "{\"result\":\"success\"}";
	private static String resultFail = "{\"result\":\"fail\"}";
	
	@Autowired
	private PendingProductService pendingProductService;

	@RequestMapping(value="list",method=RequestMethod.GET)
	public HubResponse pendingList(@RequestBody PendingQuryDto pendingQuryDto){
		List<PendingProductDto> produts = pendingProductService.findPendingProduct(pendingQuryDto);
		return HubResponse.successResp(produts);
	}
	@RequestMapping(value="update",method=RequestMethod.POST)
	public HubResponse updateProduct(@RequestBody PendingProductDto pendingProductDto){
		boolean result = pendingProductService.updatePendingProduct(pendingProductDto);
		if(result){
			return HubResponse.successResp(resultSuccess);
		}else{
			return HubResponse.successResp(resultFail);
		}
	}
	@RequestMapping(value="batch-update",method=RequestMethod.POST)
	public HubResponse batchUpdateProduct(@RequestBody List<PendingProductDto> pendingProducts){
		boolean result = pendingProductService.batchUpdatePendingProduct(pendingProducts);
		if(result){
			return HubResponse.successResp(resultSuccess);
		}else{
			return HubResponse.successResp(resultFail);
		}
	}
	@RequestMapping(value="unable-to-process",method=RequestMethod.POST)
	public HubResponse updateProductToUnableToProcess(@RequestBody Ids ids){
		boolean result = pendingProductService.updatePendingProductToUnableToProcess(ids.getIds().get(0));
		if(result){
			return HubResponse.successResp(resultSuccess);
		}else{
			return HubResponse.successResp(resultFail);
		}
	}
	@RequestMapping(value="batch-unable-to-process",method=RequestMethod.POST)
	public HubResponse batchUpdateProductToUnableToProcess(@RequestBody Ids ids){
		boolean result = pendingProductService.batchUpdatePendingProductToUnableToProcess(ids.getIds());
		if(result){
			return HubResponse.successResp(resultSuccess);
		}else{
			return HubResponse.successResp(resultFail);
		}
	}

	@RequestMapping(value="query-spumodel",method=RequestMethod.POST)
	public HubResponse querySpuModel(@RequestBody SpuPendingAuditQueryVO queryVO){
		return null;
	}


}
