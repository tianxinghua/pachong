package com.shangpin.ephub.product.business.ui.studio.slotspu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuExportDto;
import com.shangpin.ephub.response.HubResponse;

/**

 *
 */
@RestController
@RequestMapping("/slot-spu")
public class SlotSpuController {
	
	@Autowired
    HubSlotSpuService slotSpuService;



    @RequestMapping(value="/list",method=RequestMethod.POST)
    public HubResponse<?> pendingList(@RequestBody SpuSupplierQueryDto quryDto){

        List<SlotSpuDto> slotSpu = slotSpuService.findSlotSpu(quryDto);

        return HubResponse.successResp(slotSpu);
    }


    @RequestMapping(value="/count",method=RequestMethod.POST)
    public HubResponse<?> count(@RequestBody SpuSupplierQueryDto quryDto){

        Integer  count  = slotSpuService.countSlotSpu(quryDto);

        return HubResponse.successResp(count);
    }
    
    @RequestMapping(value="/commited-export",method=RequestMethod.POST)
    public HubResponse<?> commitedExport(@RequestBody SpuSupplierQueryDto quryDto){
    	List<SlotSpuExportDto> list = slotSpuService.exportSlotSpu(quryDto);
    	return HubResponse.successResp(list);
    }




}
