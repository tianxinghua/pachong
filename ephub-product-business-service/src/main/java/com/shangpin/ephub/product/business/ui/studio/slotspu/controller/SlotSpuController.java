package com.shangpin.ephub.product.business.ui.studio.slotspu.controller;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SlotSpuSupplier;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.gateway.SpuSupplierUnionGateWay;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuDto;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IHubSpuPendingPicService;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingOriginVo;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.SupplierProductVo;
import com.shangpin.ephub.product.business.ui.studio.pending.service.StudioPendingService;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 *
 */
@RestController
@RequestMapping("/slot-spu")
@Slf4j
public class SlotSpuController {
	
	private static String resultSuccess = "success";
	private static String resultFail = "fail";
	
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

}
