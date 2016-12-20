package com.shangpin.ephub.product.business.ui.pendingCrud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.product.business.ui.pendingCrud.dto.PendingQuryDto;
import com.shangpin.ephub.response.HubResponse;

@Controller
@RequestMapping("/pending-product")
public class PendingProductController {

	@RequestMapping(value="list",method=RequestMethod.POST)
	public HubResponse pendingList(@RequestBody PendingQuryDto pendingQuryDto){
		
		//supplierName怎么查找supplierId
		//数据状态到底有几个
		
		return null;
	}
}
