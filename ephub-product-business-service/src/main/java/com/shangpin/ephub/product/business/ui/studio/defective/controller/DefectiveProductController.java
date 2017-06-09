package com.shangpin.ephub.product.business.ui.studio.defective.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.response.HubResponse;

@RestController
@RequestMapping("/defective-product")
public class DefectiveProductController {
	
	@RequestMapping(value="/list",method = RequestMethod.POST)
	public HubResponse<?> list(){
		return null;
	}

	@RequestMapping(value="/add",method = RequestMethod.POST)
	public HubResponse<?> add(@RequestBody String slotNoSpuId){
		return null;
	}
	
	@RequestMapping(value="/modification", method = RequestMethod.POST)
	public HubResponse<?> modify(){
		return null;
	}
	
	@RequestMapping(value="/detail", method = RequestMethod.POST)
	public HubResponse<?> detail(){
		return null;
	}
}
