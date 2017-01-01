package com.shangpin.ephub.product.business.ui.pending.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title:PendingProductController </p>
 * <p>Description: 待处理页面</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijiang
 * @date 2016年12月21日 下午5:15:48
 *
 */
@RestController
@RequestMapping("/pending-product")
public class PendingProductController {
	
	private static String resultSuccess = "success";
	private static String resultFail = "fail";
	
	@Autowired
	private IPendingProductService pendingProductService;

    @RequestMapping(value="/list",method=RequestMethod.POST)
    public HubResponse<?> pendingList(@RequestBody PendingQuryDto pendingQuryDto){
        PendingProducts pendingProducts = pendingProductService.findPendingProducts(pendingQuryDto);
        return HubResponse.successResp(pendingProducts);
    }
    @RequestMapping(value="/update",method=RequestMethod.POST)
    public HubResponse<?> updateProduct(@RequestBody PendingProductDto pendingProductDto){
        try {
        	boolean result = pendingProductService.updatePendingProduct(pendingProductDto);
            if(result){
                return HubResponse.successResp(resultSuccess);
            }else{
                return HubResponse.successResp(resultFail);
            }
		} catch (Exception e) {
			return HubResponse.errorResp(e.getMessage());
		}
    }
    @RequestMapping(value="/batch-update",method=RequestMethod.POST)
    public HubResponse<?> batchUpdateProduct(@RequestBody PendingProducts pendingProducts){
        boolean result = pendingProductService.batchUpdatePendingProduct(pendingProducts);
        if(result){
            return HubResponse.successResp(resultSuccess);
        }else{
            return HubResponse.successResp(resultFail);
        }
    }
    @RequestMapping(value="/unable-to-process",method=RequestMethod.POST)
    public HubResponse<?> updateProductToUnableToProcess(@RequestBody String id){
    	try {
    		boolean result = pendingProductService.updatePendingProductToUnableToProcess(id);
	        if(result){
	            return HubResponse.successResp(resultSuccess);
	        }else{
	            return HubResponse.successResp(resultFail);
	        }
		} catch (Exception e) {
			return HubResponse.successResp(resultFail);
		}
       
    }
    @RequestMapping(value="/batch-unable-to-process",method=RequestMethod.POST)
    public HubResponse<?> batchUpdateProductToUnableToProcess(@RequestBody List<String> ids){
        boolean result = pendingProductService.batchUpdatePendingProductToUnableToProcess(ids);
        if(result){
            return HubResponse.successResp(resultSuccess);
        }else{
            return HubResponse.successResp(resultFail);
        }
    }
}
