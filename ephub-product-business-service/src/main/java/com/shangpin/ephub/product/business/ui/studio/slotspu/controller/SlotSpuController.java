package com.shangpin.ephub.product.business.ui.studio.slotspu.controller;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
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
	private IPendingProductService pendingProductService;
	@Autowired
	private IHubSpuPendingPicService pendingPicService;

	@Autowired
	StudioPendingService studioPendingService;

    @RequestMapping(value="/list",method=RequestMethod.POST)
    public HubResponse<?> pendingList(@RequestBody PendingQuryDto pendingQuryDto){
        PendingProducts pendingProducts = pendingProductService.findPendingProducts(pendingQuryDto,false);
        return HubResponse.successResp(pendingProducts);
    }


}
