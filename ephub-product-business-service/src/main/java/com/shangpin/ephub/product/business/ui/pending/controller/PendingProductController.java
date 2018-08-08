package com.shangpin.ephub.product.business.ui.pending.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.service.hub.HubSpuCommonService;
import com.shangpin.ephub.product.business.service.pending.PendingCommonService;
import com.shangpin.ephub.product.business.service.pending.WebSpiderService;
import com.shangpin.ephub.product.business.ui.pending.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.product.business.ui.pending.dto.Page;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.dto.ReasonRequestDto;
import com.shangpin.ephub.product.business.ui.pending.dto.Reasons;
import com.shangpin.ephub.product.business.ui.pending.service.HubSpuPendingNohandleReasonService;
import com.shangpin.ephub.product.business.ui.pending.service.IHubSpuPendingPicService;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PendingProductController {
	
	private static String resultSuccess = "success";
	private static String resultFail = "fail";
	
	@Autowired
	private IPendingProductService pendingProductService;
	@Autowired
	private IHubSpuPendingPicService pendingPicService;
	@Autowired
	private HubSpuPendingNohandleReasonService reasonService;

	@Autowired
	private WebSpiderService webSpiderService;
	@Autowired
	private PendingCommonService pendingCommonService;

	@Autowired
	private HubSpuCommonService spuCommonService;

	int pageSize = 200;

    @RequestMapping(value="/list",method=RequestMethod.POST)
    public HubResponse<?> pendingList(@RequestBody PendingQuryDto pendingQuryDto){
        PendingProducts pendingProducts = pendingProductService.findPendingProducts(pendingQuryDto,false);
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


	@RequestMapping(value="/batch-update-all",method=RequestMethod.GET)
	public HubResponse<?> batchUpdateWaitHandleProduct(@RequestParam(value="supplierid", required=false) String supplierId,
													   @RequestParam(value="startdate", required=false) String startDate,
													   @RequestParam(value="enddate", required=false) String endDate){
		HubResponse<String> response = new HubResponse<>();
		response.setCode("0");
    	Date start = null;
    	Date end = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	if(StringUtils.isNotBlank(startDate)){

			try {
				start = dateFormat.parse(startDate  +  " 00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		if(StringUtils.isNotBlank(endDate)){

			try {
				Calendar calendar =  Calendar.getInstance();
				end = dateFormat.parse(endDate  +  " 00:00:00");
				calendar.setTime(end);
				calendar.add(Calendar.DAY_OF_YEAR,1);
				end = calendar.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		int i = 1;
    	boolean isOver = false;
    	while(true){
			PendingProducts pendingProducts = pendingCommonService.getWaitHandleSpuPendingsWithPage(supplierId, start,end, i, pageSize);
			if(pendingProducts.getTotal()!=pageSize){
				isOver = true;
			}
			pendingProducts.getProduts().forEach(spuPendingVO->{
				//先查询hubspu 无的话 查询爬虫
				List<HubSpuDto> hubSpuDtos = spuCommonService.selectHubSpu(spuPendingVO.getSpuModel(), spuPendingVO.getHubBrandNo());
				if(null!=hubSpuDtos&&hubSpuDtos.size()>0) {
					pendingProductService.updatePendingProduct(spuPendingVO);
				}else{
					HubSpuPendingDto handleWebSpiderdSpuPending = pendingCommonService.getHandleWebSpiderdSpuPending(spuPendingVO.getHubBrandNo(), spuPendingVO.getSpuModel());
					if(null!=handleWebSpiderdSpuPending){
						pendingProductService.updatePendingProduct(spuPendingVO);
					}
				}
			});
			if(isOver) break;
			i++;

		}

//    	List<PendingProducts>  pendingProducts =
//		 pendingProductService.batchUpdatePendingProduct(pendingProducts);
		response.setMsg("完成");
		return response;
	}


	@RequestMapping(value="/select-hotboom",method=RequestMethod.POST)
	public HubResponse<?> selectHotboom(@RequestBody PendingProductDto pendingProductDto){
		return pendingProductService.selectHotBoom(pendingProductDto);
	}

	@RequestMapping(value="/batch-select-hotboom",method=RequestMethod.POST)
	public HubResponse<?> batchSelectHotboom(@RequestBody PendingProducts pendingProducts){
		return pendingProductService.batchSelectHotBoom(pendingProducts);
	}

	@RequestMapping(value="/save-hotboom",method=RequestMethod.POST)
	public HubResponse<?> saveHotboom(@RequestBody PendingProductDto pendingProductDto){
		return pendingProductService.saveHotBoom(pendingProductDto);
	}

	@RequestMapping(value="/save-webspider",method=RequestMethod.POST)
	public HubResponse<?> saveWebSpider(@RequestBody PendingProductDto pendingProductDto){
		return webSpiderService.saveWebSpider(pendingProductDto);
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
    	long start = System.currentTimeMillis();
    	PendingProducts products = pendingProductService.findPendingProducts(pendingQuryDto,true);
    	PendingProductDto pendingProduct = products.getProduts().get(0);
    	SupplierProductVo supplierProduct = pendingProductService.findSupplierProduct(pendingProduct.getSupplierSpuId());
    	HubBrandModelRuleDto brandModelRuleDto = pendingProductService.findHubBrandModelRule(pendingProduct.getHubBrandNo());
    	PendingOriginVo pendingOriginVo = new PendingOriginVo();
    	pendingOriginVo.setPendingProduct(pendingProduct);
    	pendingOriginVo.setSupplierProduct(supplierProduct);
    	pendingOriginVo.setBrandModelRuleDto(brandModelRuleDto); 
    	log.info("--->查看原始总耗时{}",System.currentTimeMillis()-start); 
    	return HubResponse.successResp(pendingOriginVo); 
    }
    @RequestMapping(value="/retry-pictures",method=RequestMethod.POST)
    public HubResponse<?> retryPictures(@RequestBody List<String> spPicUrl){
    	boolean bool = pendingPicService.retryPictures(spPicUrl);
    	if(bool){
    		return HubResponse.successResp("success");
    	}else{
    		return HubResponse.errorResp("error");
    	}
    }
    @RequestMapping(value="/list-all",method=RequestMethod.POST)
    public HubResponse<?> pendingListAll(@RequestBody PendingQuryDto pendingQuryDto){
    	PendingProducts pendingProducts = pendingProductService.findPendingProducts(pendingQuryDto,true);
        return HubResponse.successResp(pendingProducts);
    } 
    @RequestMapping(value="/update-to-infopeccable/{updateUser}",method=RequestMethod.POST)
    public HubResponse<?> updateProductToInfoPeccable(@PathVariable String updateUser, @RequestBody List<String> ids){
    	boolean bool = pendingProductService.updateProductToInfoPeccable(updateUser, ids);
    	if(bool){
    		return HubResponse.successResp("");
    	}else{
    		return HubResponse.errorResp("更新失败");
    	}
    }
    @RequestMapping(value="/unable-reason",method=RequestMethod.POST)
    public HubResponse<?> unableReason(@RequestBody Reasons reasons){
    	reasonService.addUnableReason(reasons);
    	return HubResponse.successResp("ok");
    }
    @RequestMapping(value="/notOnShelfList",method=RequestMethod.POST)
    public HubResponse<?> notOnShelfList(@RequestBody ReasonRequestDto reasons){
    	log.info("未上架商品请求参数：{}",reasons);
    	Page listResponse = reasonService.findOnShelfList(reasons);
    	if(listResponse!=null){
    		return HubResponse.successResp(listResponse);
    	}else{
    		return HubResponse.errorResp("No data");
    	}
    	
    }
    
}
