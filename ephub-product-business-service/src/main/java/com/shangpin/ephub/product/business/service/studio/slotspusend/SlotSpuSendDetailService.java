package com.shangpin.ephub.product.business.service.studio.slotspusend;

/**
 * Created by loyalty on 17/7/5.
 * 供货商发货明细
 */
public interface SlotSpuSendDetailService {

    /**
     * 根据批次号获取总数量
     * @param slotNo
     * @return
     */
    public int getSendDetailCount(String slotNo);

    /**
     * 获取摄影棚收到的总数量
     * @param slotNo
     * @return
     */
    public int getArriveSpuDetailCount(String slotNo);
}
