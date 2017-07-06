package com.shangpin.ephub.product.business.service.studio.slotspusend.impl;

import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotStudioArriveState;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.product.business.service.studio.slotspusend.SlotSpuSendDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 17/7/5.
 */
@Slf4j
@Service
public class SlotSpuSendDetailServiceImpl implements SlotSpuSendDetailService {

    @Autowired
    StudioSlotSpuSendDetailGateWay sendDetailGateWay;

    @Override
    public int getSendDetailCount(String slotNo) {
        StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
        criteria.createCriteria().andSlotNoEqualTo(slotNo);
        return  sendDetailGateWay.countByCriteria(criteria);

    }

    @Override
    public int getArriveSpuDetailCount(String slotNo) {
        StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
        criteria.createCriteria().andSlotNoEqualTo(slotNo).andArriveStateNotEqualTo(StudioSlotStudioArriveState.NOT_ARRIVE.getIndex().byteValue());
        return  sendDetailGateWay.countByCriteria(criteria);
    }
}
