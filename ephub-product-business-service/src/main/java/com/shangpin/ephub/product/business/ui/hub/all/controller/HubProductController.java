package com.shangpin.ephub.product.business.ui.hub.all.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangpin.ephub.product.business.ui.hub.all.service.IHubProductService;
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
	
	@Autowired
	private IHubProductService hubProductService;

	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public HubResponse hubList(@RequestBody HubQuryDto hubQuryDto){
		return HubResponse.successResp("");
	}
}
