package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.util.List;

import com.shangpin.ephub.product.business.service.studio.slotspureturn.SlotSpuReturnDetailService;
import com.shangpin.ephub.product.business.service.studio.slotspusend.SlotSpuSendDetailService;
import com.shangpin.ephub.product.business.service.supplier.SupplierService;
import com.shangpin.ephub.product.business.service.supplier.dto.SupplierDto;
import org.apache.commons.lang.StringUtils;
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


	@Autowired
	private SupplierService supplierService;


	@Autowired
    private SlotSpuSendDetailService slotSpuSendDetailService;

	@Autowired
    private SlotSpuReturnDetailService slotSpuReturnDetailService;
	
	public SlotInfo getSlotInfo(String slotNo){
		try {
			SlotInfo slotInfo = new SlotInfo();
			StudioSlotDto studioSlotDto = operationService.selectStudioSlot(slotNo);
			if(null==studioSlotDto) return slotInfo;
			convert(slotInfo,studioSlotDto);



			StudioDto studioDto = operationService.getStudio(studioSlotDto.getStudioId());
			slotInfo.setStudioName(null != studioDto ? studioDto.getStudioName() : "");

			setQty(slotInfo,studioSlotDto);
			return slotInfo;
		} catch (Exception e) {
			log.error("查询批次明细异常："+e.getMessage(),e);
		}
		return null;
		
	}
	//包含供货商编号
	private void convert(SlotInfo slotInfo, StudioSlotDto studioSlotDto){
		slotInfo.setSlotNo(studioSlotDto.getSlotNo());
		slotInfo.setSlotStatus(studioSlotDto.getSlotStatus().intValue());
		slotInfo.setSlotDate(studioSlotDto.getPlanArriveTime());
		slotInfo.setArriveTime(studioSlotDto.getArriveTime());
		slotInfo.setPlanShootTime(studioSlotDto.getPlanShootTime());
		slotInfo.setShootTime(studioSlotDto.getShootTime());
		if(StringUtils.isNotBlank(studioSlotDto.getApplySupplierId())) {
            SupplierDto supplierDto = supplierService.getSupplierBySupplierId(studioSlotDto.getApplySupplierId());
            if(null!=supplierDto){
                slotInfo.setSupplierName(supplierDto.getSupplierNo());
            }
        }
	}
	/**
	 * 计算设置一堆数量
	 * @param studioSlotDto
	 * @param
	 * @return
	 */
	private void setQty(SlotInfo slotInfo, StudioSlotDto studioSlotDto){
	    if(null==studioSlotDto.getSlotNo()) {
	        log.error(" 查询批次下商品数量失败，因无批次 ");
	        return;
        }

        slotInfo.setSendQty(slotSpuSendDetailService.getSendDetailCount(studioSlotDto.getSlotNo()));
		if(null != studioSlotDto.getSendState() && studioSlotDto.getSendState() == StudioSlotSendState.SEND.getIndex().byteValue() ){
            slotInfo.setArriveQty(slotSpuSendDetailService.getArriveSpuDetailCount(studioSlotDto.getSlotNo()));
		}else{
		    slotInfo.setArriveQty(0);
            slotInfo.setActualQuantity(0);
        }

        int returnQty = slotSpuReturnDetailService.getReturnDetailCount(studioSlotDto.getSlotNo());
		if(0==returnQty){
            slotInfo.setReturnQty(0);
            slotInfo.setActualQuantity(0);
        }else{
            slotInfo.setReturnQty(returnQty);
            int supplierConfirmQty = slotSpuReturnDetailService.getArriveSpuDetailCount(studioSlotDto.getSlotNo());
            slotInfo.setActualQuantity(supplierConfirmQty);
        }
		
	}

}
