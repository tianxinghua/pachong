package com.shangpin.ephub.product.business.ui.pendingCrud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.pendingCrud.dto.Ids;
import com.shangpin.ephub.product.business.ui.pendingCrud.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pendingCrud.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pendingCrud.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pendingCrud.vo.PendingProducts;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title:PendingProductController </p>
 * <p>Description: 待处理页面</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijiang
 * @date 2016年12月21日 下午5:15:48
 *
 */
@Controller
@RequestMapping("/pending-product")
public class PendingProductController {
	
	private static String resultSuccess = "{\"result\":\"success\"}";
	private static String resultFail = "{\"result\":\"fail\"}";
	
	@Autowired
	private IPendingProductService pendingProductService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/list",method=RequestMethod.POST)
    @ResponseBody
    public HubResponse pendingList(@RequestBody PendingQuryDto pendingQuryDto){
        PendingProducts pendingProducts = pendingProductService.findPendingProducts(pendingQuryDto);
        System.out.println("查询的数据========="+JsonUtil.serialize(pendingProducts));
        return HubResponse.successResp(pendingProducts);
    }
    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/update",method=RequestMethod.POST)
    public HubResponse updateProduct(@RequestBody PendingProductDto pendingProductDto){
        boolean result = pendingProductService.updatePendingProduct(pendingProductDto);
        if(result){
            return HubResponse.successResp(resultSuccess);
        }else{
            return HubResponse.successResp(resultFail);
        }
    }
    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/batch-update",method=RequestMethod.POST)
    public HubResponse batchUpdateProduct(@RequestBody List<PendingProductDto> pendingProducts){
        boolean result = pendingProductService.batchUpdatePendingProduct(pendingProducts);
        if(result){
            return HubResponse.successResp(resultSuccess);
        }else{
            return HubResponse.successResp(resultFail);
        }
    }
    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/unable-to-process",method=RequestMethod.POST)
    public HubResponse updateProductToUnableToProcess(@RequestBody Ids ids){
        boolean result = pendingProductService.updatePendingProductToUnableToProcess(ids.getIds().get(0));
        if(result){
            return HubResponse.successResp(resultSuccess);
        }else{
            return HubResponse.successResp(resultFail);
        }
    }
    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/batch-unable-to-process",method=RequestMethod.POST)
    public HubResponse batchUpdateProductToUnableToProcess(@RequestBody Ids ids){
        boolean result = pendingProductService.batchUpdatePendingProductToUnableToProcess(ids.getIds());
        if(result){
            return HubResponse.successResp(resultSuccess);
        }else{
            return HubResponse.successResp(resultFail);
        }
    }
}
