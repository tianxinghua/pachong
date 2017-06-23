package com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.controller;

import com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.bean.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.po.SlotSpuSupplier;
import com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.service.HubSlotSpuSupplierExtendService;
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
@RequestMapping("/hub-slot-spu-supplier")
public class HubSlotSpuSupplierExtendController {
	
	@Autowired
	private HubSlotSpuSupplierExtendService service;

	@RequestMapping(value="/select-by-query")
	public List<SlotSpuSupplier> selectByQuery(@RequestBody SlotSpuSupplierQueryDto queryDto){
		return service.findObjectByQuery(queryDto);
	}
	@RequestMapping(value="/count-by-query")
	public int countByQuery(@RequestBody SlotSpuSupplierQueryDto queryDto){
		return service.countByQuery(queryDto);
	}
}
