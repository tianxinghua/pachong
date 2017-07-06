package com.shangpin.ephub.product.business.service.studio.slotspureturn.impl;

import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotStudioArriveState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotSupplierArriveState;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnDetailGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.product.business.service.studio.slotspureturn.SlotSpuReturnDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 17/7/5.
 */
@Slf4j
@Service
public class SlotSpuReturnDetailServiceImpl implements SlotSpuReturnDetailService {

    @Autowired
    StudioSlotReturnDetailGateWay returnDetailGateWay;




    @Override
    public int getReturnDetailCount(String slotNo) {
        StudioSlotReturnDetailCriteriaDto criteria = new StudioSlotReturnDetailCriteriaDto();
        criteria.createCriteria().andSlotNoEqualTo(slotNo);
        return returnDetailGateWay.countByCriteria(criteria);
    }

    @Override
    public int getArriveSpuDetailCount(String slotNo) {
        StudioSlotReturnDetailCriteriaDto criteria = new StudioSlotReturnDetailCriteriaDto();
        criteria.createCriteria().andSlotNoEqualTo(slotNo).andArriveStateNotEqualTo(StudioSlotSupplierArriveState.NOT_RECEIVE.getIndex().byteValue());
        return  returnDetailGateWay.countByCriteria(criteria);
    }
}
