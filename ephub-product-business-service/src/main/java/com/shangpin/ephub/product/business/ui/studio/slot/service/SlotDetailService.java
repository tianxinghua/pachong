package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotSendState;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.SlotInfo;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: SlotDetailService</p>
 * <p>Description: 批次详情信息 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月4日 下午6:47:46
 *
 */
@Service
@Slf4j
public class SlotDetailService {
	
	@Autowired
	private OperationService operationService;
	
	public SlotInfo getSlotInfo(String slotNo){
		try {
			SlotInfo slotInfo = new SlotInfo();
			StudioSlotDto studioSlotDto = operationService.selectStudioSlot(slotNo);
			convert(slotInfo,studioSlotDto);
			slotInfo.setSupplierName(studioSlotDto.getApplySupplierId());
			StudioDto studioDto = operationService.getStudio(studioSlotDto.getStudioId());
			slotInfo.setStudioName(null != studioDto ? studioDto.getStudioName() : "");
			List<StudioSlotSpuSendDetailDto> detailList = operationService.selectDetail(slotNo);
			setQty(slotInfo,studioSlotDto,detailList);
			return slotInfo;
		} catch (Exception e) {
			log.error("查询批次明细异常："+e.getMessage(),e);
		}
		return null;
		
	}
	
	private void convert(SlotInfo slotInfo, StudioSlotDto studioSlotDto){
		slotInfo.setSlotNo(studioSlotDto.getSlotNo());
		slotInfo.setSlotStatus(studioSlotDto.getSlotStatus().intValue());
		slotInfo.setSlotDate(studioSlotDto.getSlotDate());
		slotInfo.setArriveTime(studioSlotDto.getArriveTime());
		slotInfo.setPlanShootTime(studioSlotDto.getPlanShootTime());
		slotInfo.setShootTime(studioSlotDto.getShootTime()); 
	}
	/**
	 * 计算设置一堆数量
	 * @param studioSlotDto
	 * @param detailList
	 * @return
	 */
	private void setQty(SlotInfo slotInfo, StudioSlotDto studioSlotDto, List<StudioSlotSpuSendDetailDto> detailList){
		int sendQty = 0;
		if(null != studioSlotDto.getSendState() && studioSlotDto.getSendState() == StudioSlotSendState.SEND.getIndex().byteValue() ){
			
		}
		
	}

}
