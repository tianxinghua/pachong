package com.shangpin.ephub.product.business.ui.studio.imageupload.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.enumeration.OperationQueryType;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;
import com.shangpin.ephub.product.business.ui.studio.imageupload.service.ImageUploadService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageUploadServiceImpl implements  ImageUploadService{
	
	@Autowired
	private OperationService operationService;

	@Override
	public List<StudioSlotVo> list(OperationQuery operationQuery) {
		try {
			operationQuery.setOperationQueryType(OperationQueryType.IMAGE_UPLOAD.getIndex());
			log.info("图片上传页面接收到的参数："+JsonUtil.serialize(operationQuery)); 
			List<StudioSlotDto>  list = operationService.slotList(operationQuery);
			
			List<StudioSlotVo> vos = new ArrayList<StudioSlotVo>();			
			if(CollectionUtils.isNotEmpty(list)){
				for(StudioSlotDto dto : list){
					vos.add(operationService.formatDto(dto));
				}
			}
			return vos;
		} catch (Exception e) {
			log.error("图片上传列表页异常："+e.getMessage(),e); 
		}
		return null;
	}

	@Override
	public List<StudioSlotSpuSendDetailVo> slotDetail(String slotNo) {
		return operationService.slotDetail(slotNo);
	}

	
}
