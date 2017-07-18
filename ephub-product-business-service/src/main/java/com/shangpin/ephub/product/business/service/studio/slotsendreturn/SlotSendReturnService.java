package com.shangpin.ephub.product.business.service.studio.slotsendreturn;

import com.shangpin.ephub.product.business.service.studio.slotsendreturn.dto.SlotSpuSendAndReturn;

import java.util.List;

/**
 * Created by loyalty on 17/7/4.
 */
public interface SlotSendReturnService {

    /**
     * 获取批次下的产品信息
     * @param slotNo
     * @return
     */
    public List<SlotSpuSendAndReturn> findSlotSpuSendAndReturnMsgBySlotNo(String slotNo);
}
