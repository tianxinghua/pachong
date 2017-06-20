package com.shangpin.ephub.data.mysql.slot.spusupplierunion.controller;

import com.shangpin.ephub.data.mysql.slot.spusupplierunion.bean.SpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.po.SlotSpuSupplier;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.service.HubSupplierPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
  spu supplier
 *
 */
@RestController
@RequestMapping("/spu-supplier-search")
public class SpuSupplierSearchController {
	
	@Autowired
	private HubSupplierPriceService service;

	@RequestMapping(value="/list")
	public List<SlotSpuSupplier> listByQuery(@RequestBody SpuSupplierQueryDto queryDto){
		return service.findObjectByQuery(queryDto);
	}
	@RequestMapping(value="/count-by-query")
	public int countByQuery(@RequestBody SpuSupplierQueryDto queryDto){
		return service.countByQuery(queryDto);
	}
}
