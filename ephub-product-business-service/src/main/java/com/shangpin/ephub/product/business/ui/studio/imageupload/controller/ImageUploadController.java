package com.shangpin.ephub.product.business.ui.studio.imageupload.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.enumeration.OperationQueryType;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/airstudio/image-upload")
@Slf4j
public class ImageUploadController {
	
	@Autowired
	private OperationService operationService;

	@RequestMapping(value = "/list" ,method = RequestMethod.POST)
	public HubResponse<?> slotList(@RequestBody OperationQuery operationQuery){
		try {
			List<StudioSlotVo> vos = new ArrayList<StudioSlotVo>();
			operationQuery.setOperationQueryType(OperationQueryType.IMAGE_UPLOAD.getIndex());
			log.info("图片上传页面接收到的参数："+JsonUtil.serialize(operationQuery)); 
			List<StudioSlotDto>  list = operationService.slotList(operationQuery);
			if(CollectionUtils.isNotEmpty(list)){
				for(StudioSlotDto dto : list){
					vos.add(operationService.formatDto(dto));
				}
			}
			return HubResponse.successResp(vos);
		} catch (Exception e) {
			log.error("图片上传列表页异常："+e.getMessage(),e); 
		}
		return HubResponse.errorResp("error");
	} 
}
