package com.shangpin.ephub.product.business.ui.studio.slot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.SlotInfo;
/**
 * <p>Title: SlotDetailService</p>
 * <p>Description: 批次详情信息 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月4日 下午6:47:46
 *
 */
@Service
public class SlotDetailService {
	
	@Autowired
	private OperationService operationService;
	
	public SlotInfo getSlotInfo(String slotNo){
		SlotInfo slotInfo = new SlotInfo();
		StudioSlotDto studioSlotDto = operationService.selectStudioSlot(slotNo);
		convert(slotInfo,studioSlotDto);
		slotInfo.setSupplierName(studioSlotDto.getApplySupplierId());
		StudioDto studioDto = operationService.getStudio(studioSlotDto.getStudioId());
		slotInfo.setStudioName(null != studioDto ? studioDto.getStudioName() : "");
		
		return slotInfo;
	}
	
	private void convert(SlotInfo slotInfo, StudioSlotDto studioSlotDto){
		slotInfo.setSlotNo(studioSlotDto.getSlotNo());
		slotInfo.setSlotStatus(studioSlotDto.getSlotStatus().toString());
		slotInfo.setSlotDate(studioSlotDto.getSlotDate());
		slotInfo.setArriveTime(studioSlotDto.getArriveTime());
		slotInfo.setPlanShootTime(studioSlotDto.getPlanShootTime());
		slotInfo.setShootTime(studioSlotDto.getShootTime()); 
	}

}
