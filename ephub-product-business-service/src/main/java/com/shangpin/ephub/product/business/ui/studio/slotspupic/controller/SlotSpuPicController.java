package com.shangpin.ephub.product.business.ui.studio.slotspupic.controller;

import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuDto;
import com.shangpin.ephub.product.business.service.studio.hubslotpic.SlotPicService;
import com.shangpin.ephub.response.HubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**

 *
 */
@RestController
@RequestMapping("/slot-spu-pic")
public class SlotSpuPicController {
	
	@Autowired
    SlotPicService slotPicService;



    @RequestMapping(value="/list",method=RequestMethod.POST)
    public HubResponse<?> pendingList(@RequestBody SpuSupplierQueryDto quryDto){

        List<SlotSpuDto> slotSpu = null ;// slotPicService.findSpuPicByBrandNoAndSpuModel(quryDto);

        return HubResponse.successResp(slotSpu);
    }







}
