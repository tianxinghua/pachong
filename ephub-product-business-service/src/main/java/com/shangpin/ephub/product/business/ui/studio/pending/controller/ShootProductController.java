package com.shangpin.ephub.product.business.ui.studio.pending.controller;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IHubSpuPendingPicService;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingOriginVo;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.SupplierProductVo;
import com.shangpin.ephub.product.business.ui.studio.pending.service.StudioPendingService;
import com.shangpin.ephub.product.business.ui.studio.pending.vo.ShootPendingVo;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

  拍照待处理页面
 *
 */
@RestController
@RequestMapping("/shoot-product")
@Slf4j
public class ShootProductController {
	
	private static String resultSuccess = "success";
	private static String resultFail = "fail";
	

	@Autowired
	private IHubSpuPendingPicService pendingPicService;

	@Autowired
	private StudioPendingService studioPendingService;

    @RequestMapping(value="/list",method=RequestMethod.POST)
    public HubResponse<?> pendingList(@RequestBody PendingQuryDto pendingQuryDto){
		pendingQuryDto.setShoot(true);
        PendingProducts pendingProducts = studioPendingService.findPendingProducts(pendingQuryDto,false);
        return HubResponse.successResp(pendingProducts);
    }
    @RequestMapping(value="/update",method=RequestMethod.POST)
    public HubResponse<?> updateProduct(@RequestBody PendingProductDto pendingProductDto){
    	return studioPendingService.updatePendingProduct(pendingProductDto);
    }




    @RequestMapping(value="/batch-update",method=RequestMethod.POST)
    public HubResponse<?> batchUpdateProduct(@RequestBody PendingProducts pendingProducts){
        return studioPendingService.batchUpdatePendingProduct(pendingProducts);
    }

    @RequestMapping(value="/cancel-view/{operator}",method=RequestMethod.POST)
    public HubResponse<?>  cancelView(@PathVariable("operator") String operator  ,@RequestBody List<Long> ids){
        try {

            if(null!=ids&&ids.size()>0) {
                ShootPendingVo vo  = new ShootPendingVo();
                vo.setOperator(operator);
                vo.setSpuPendingIds(ids);
                studioPendingService.updateSlotStateToNoNeedHandle(vo.getSpuPendingIds(), vo.getOperator());
                return HubResponse.successResp(true);
            }else{
                return HubResponse.errorResp("参数为空，更新失败");
            }

        } catch (Exception e) {
            return HubResponse.errorResp("发生异常，更新失败");
        }

    }

}