package com.shangpin.ephub.product.business.pendingPage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.shangpin.ephub.product.business.pendingPage.dto.SpSkuDTO;

import org.apache.http.protocol.ResponseContent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pending-product")
public class PendingProductController {

	public String pendingList(HttpServletRequest request,HttpServletResponse response,String query){


		return null;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "setspskuno",method = RequestMethod.POST)
	public ResponseContent countByCriteria(@RequestBody SpSkuDTO dto){

        return null;

	}
}
