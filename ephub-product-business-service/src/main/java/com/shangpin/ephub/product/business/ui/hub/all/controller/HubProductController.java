package com.shangpin.ephub.product.business.ui.hub.all.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.hub.all.service.IHubProductService;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductDetails;
import com.shangpin.ephub.product.business.ui.hub.common.dto.HubQuryDto;
import com.shangpin.ephub.response.HubResponse;

/**
 * <p>Title:HubProductController </p>
 * <p>Description: hub页面</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月21日 下午5:16:03
 *
 */
@RestController
@RequestMapping("/hub-product")
public class HubProductController {
	
	private static String resultSuccess = "success";
	private static String resultFail = "fail";
	
	@Autowired
	private IHubProductService hubProductService;

	@RequestMapping(value="/list",method=RequestMethod.POST)
	public HubResponse<?> hubList(@RequestBody HubQuryDto hubQuryDto){	
		
		return HubResponse.successResp(hubProductService.findHubProductds(hubQuryDto));
	}
	
	@RequestMapping(value="/detail",method=RequestMethod.POST)
	public HubResponse<?> productDetail(@RequestBody String id){
		
		return HubResponse.successResp(hubProductService.findProductDtails(id));
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public HubResponse<?> editProductDetail(@RequestBody HubProductDetails hubProductDetails){
		boolean ifSucc = hubProductService.updateHubProductDetails(hubProductDetails);
		if(ifSucc){
			return HubResponse.successResp(resultSuccess);
		}else{
			return HubResponse.errorResp(resultFail);
		}
		
	}
}
