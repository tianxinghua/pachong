package com.shangpin.ephub.product.business.ui.hub.all.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangpin.ephub.product.business.ui.hub.all.dto.Id;
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
@RequestMapping("/hub-product")
public class HubProductController {
	
	private static String resultSuccess = "{\"result\":\"success\"}";
	private static String resultFail = "{\"result\":\"fail\"}";
	
	@Autowired
	private IHubProductService hubProductService;

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public HubResponse hubList(@RequestBody HubQuryDto hubQuryDto){	
		
		return HubResponse.successResp(hubProductService.findHubProductds(hubQuryDto));
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/detail",method=RequestMethod.POST)
	@ResponseBody
	public HubResponse productDetail(@RequestBody Id id){
		
		return HubResponse.successResp(hubProductService.findProductDtails(id.getId()));
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public HubResponse editProductDetail(@RequestBody HubProductDetails hubProductDetails){
		boolean ifSucc = hubProductService.updateHubProductDetails(hubProductDetails);
		if(ifSucc){
			return HubResponse.successResp(resultSuccess);
		}else{
			return HubResponse.errorResp(resultFail);
		}
		
	}
}
