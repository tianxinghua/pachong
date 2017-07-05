package com.shangpin.ephub.product.business.service.studio.slotsendreturn.impl;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailDto;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnDetailGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by loyalty on 17/7/4.
 */
@Service
public class SlotSendReturnServiceImpl {

    @Autowired
    StudioSlotSpuSendDetailGateWay spuSendDetailGateWay;

    @Autowired
    StudioSlotReturnDetailGateWay  returnDetailGateWay;


    private List<StudioSlotSpuSendDetailDto> getSendDetailBySlotNo(String slotNo){
        StudioSlotSpuSendDetailCriteriaDto  criteria =new StudioSlotSpuSendDetailCriteriaDto();
        criteria.setPageSize(500);
        criteria.createCriteria().andSlotNoEqualTo(slotNo);
        List<StudioSlotSpuSendDetailDto> studioSlotSpuSendDetailDtos = spuSendDetailGateWay.selectByCriteria(criteria);
        return studioSlotSpuSendDetailDtos;
    }

    private List<StudioSlotReturnDetailDto> getReturnDetailByBarcode(String barcode){


        StudioSlotReturnDetailCriteriaDto criteria = new StudioSlotReturnDetailCriteriaDto();

        List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtos = returnDetailGateWay.selectByCriteria(criteria);
        return null;
    }
}
