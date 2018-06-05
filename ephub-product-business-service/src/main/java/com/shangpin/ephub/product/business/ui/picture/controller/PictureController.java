package com.shangpin.ephub.product.business.ui.picture.controller;

import com.shangpin.ephub.product.business.service.pending.PendingService;
import com.shangpin.ephub.product.business.service.pic.PicHandleService;
import com.shangpin.ephub.product.business.ui.pending.vo.*;
import com.shangpin.ephub.product.business.ui.picture.dto.QueryPicDto;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pic")
@Slf4j
public class PictureController {
	
	@Autowired
	PicHandleService picHandleService;







	@RequestMapping(value="spupendingpic-insert-spupic",method=RequestMethod.POST)
	public HubResponse<?> insertSpuPic(@RequestBody QueryPicDto queryPicDto){

		try {
			picHandleService.createSpuPic(picHandleService.getSupplierPicByHubSpuNoAndSupplierId(queryPicDto.getSpuNo(),queryPicDto.getSupplierId()),queryPicDto.getSpuId());
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("从原始图片到hubspu图片处理失败 ：" + " reason :" +  e.getMessage(),e);
			return HubResponse.errorResp(e.getMessage());
		}
		return HubResponse.successResp(true);
	}


	@RequestMapping(value="/retry-pictures/{supplierSpuId}",method=RequestMethod.POST)
	public HubResponse<?> updateProductToUnableToProcess(@PathVariable Long  supplierSpuId){
		boolean bool = picHandleService.retryPictures(supplierSpuId);
		if(bool){
			return HubResponse.successResp("success");
		}else{
			return HubResponse.errorResp("error");
		}

	}

}
