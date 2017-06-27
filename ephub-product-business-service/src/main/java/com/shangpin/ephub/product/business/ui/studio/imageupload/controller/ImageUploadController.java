package com.shangpin.ephub.product.business.ui.studio.imageupload.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.imageupload.service.ImageUploadService;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title: ImageUploadController</p>
 * <p>Description: 图片上传页面所有的api </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月27日 上午11:07:49
 *
 */
@RestController
@RequestMapping("/api/airstudio/image-upload")
public class ImageUploadController {
	
	@Autowired
	private ImageUploadService imageUploadService;
	
	@RequestMapping(value = "/list" ,method = RequestMethod.POST)
	public HubResponse<?> slotList(@RequestBody OperationQuery operationQuery){
		List<StudioSlotVo> list = imageUploadService.list(operationQuery);
		if(null != list){
			return HubResponse.successResp(list);
		}else{
			return HubResponse.errorResp("error");
		}
	} 
	
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public HubResponse<?> detail(@RequestBody String slotNo){
		return HubResponse.successResp(imageUploadService.slotDetail(slotNo));
	}
}
