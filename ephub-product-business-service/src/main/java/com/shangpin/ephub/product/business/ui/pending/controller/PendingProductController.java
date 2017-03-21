package com.shangpin.ephub.product.business.ui.pending.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingOriginVo;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.SupplierProductVo;
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
    	return pendingProductService.updatePendingProduct(pendingProductDto);
    }
    @RequestMapping(value="/batch-update",method=RequestMethod.POST)
    public HubResponse<?> batchUpdateProduct(@RequestBody PendingProducts pendingProducts){
        return pendingProductService.batchUpdatePendingProduct(pendingProducts);
    }
    @RequestMapping(value="/unable-to-process/{updateUser}",method=RequestMethod.POST)
    public HubResponse<?> updateProductToUnableToProcess(@PathVariable String updateUser,@RequestBody String id){
    	try {
    		boolean result = pendingProductService.updatePendingProductToUnableToProcess(updateUser,id);
	        if(result){
	            return HubResponse.successResp(resultSuccess);
	        }else{
	            return HubResponse.errorResp(resultFail);
	        }
		} catch (Exception e) {
			return HubResponse.errorResp(e.getMessage());
		}
       
    }
    @RequestMapping(value="/batch-unable-to-process/{updateUser}",method=RequestMethod.POST)
    public HubResponse<?> batchUpdateProductToUnableToProcess(@PathVariable String updateUser,@RequestBody List<String> ids){
        boolean result = pendingProductService.batchUpdatePendingProductToUnableToProcess(updateUser,ids);
        if(result){
            return HubResponse.successResp(resultSuccess);
        }else{
            return HubResponse.errorResp(resultFail);
        }
    }
    @RequestMapping(value="/origin",method=RequestMethod.POST)
    public HubResponse<?> findOrigin(@RequestBody PendingQuryDto pendingQuryDto){
    	PendingProducts products = pendingProductService.findPendingProducts(pendingQuryDto);
    	PendingProductDto pendingProduct = products.getProduts().get(0);
    	SupplierProductVo supplierProduct = pendingProductService.findSupplierProduct(pendingProduct.getSupplierSpuId());
    	HubBrandModelRuleDto modelRule = pendingProductService.findHubBrandModelRule(pendingProduct.getHubBrandNo());
    	PendingOriginVo pendingOriginVo = new PendingOriginVo();
    	pendingOriginVo.setPendingProduct(pendingProduct);
    	pendingOriginVo.setSupplierProduct(supplierProduct);
    	pendingOriginVo.setModelRule(null != modelRule ? modelRule.getModelRule() : ""); 
    	return HubResponse.successResp(pendingOriginVo); 
    }
}
