package com.shangpin.ephub.product.business.rest.supplier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.business.supplier.dto.SupplierInHubDto;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.product.business.rest.price.dto.PriceQuery;
import com.shangpin.ephub.product.business.rest.price.service.PriceService;
import com.shangpin.ephub.product.business.rest.price.vo.PriceChangeRecordDto;
import com.shangpin.ephub.product.business.rest.price.vo.ProductPrice;
import com.shangpin.ephub.product.business.service.supplier.SupplierInHubService;
import com.shangpin.ephub.product.business.service.supplier.dto.SupplierChannelDto;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 */

@RestController
@RequestMapping("/supplier-in-hub")
@Slf4j
public class SupplierController {
	
	@Autowired
	private SupplierInHubService supplierInHubService;


	
	@RequestMapping(value="/is-need-send",method=RequestMethod.POST)
	public Boolean  isNeedSend(@RequestBody String  supplierId){

		if(supplierInHubService.isNeedSendSupplier(supplierId)){
			return true;
		}else{
			return false;
		}
	}


	@RequestMapping(value="/get-supplier",method=RequestMethod.POST)
	public SupplierInHubDto getSupplierMsg(@RequestBody String  supplierId){

		return supplierInHubService.getSupplierInHubBySupplierId(supplierId);
	}



	@RequestMapping(value="/supplierChannel",method=RequestMethod.GET)
	public SupplierChannelDto getSupplierMsg(@RequestParam(name="supplierId",required=false,defaultValue = "") String  supplierId, @RequestParam(name="supplierNo",required=false,defaultValue = "") String supplierNo){
		return supplierInHubService.getSupplierChannelByMap(supplierId,supplierNo);
	}
}
