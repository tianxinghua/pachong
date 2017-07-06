package com.shangpin.ephub.product.business.service.studio.slotspureturn;

/**
 * Created by loyalty on 17/7/5.
 * 供货商发货明细
 */
public interface SlotSpuReturnDetailService {

    /**
     * 根据批次号获取返货总数量
     * @param slotNo
     * @return
     */
    public int getReturnDetailCount(String slotNo);

    /**
     * 获取供货商收到到的总数量
     * @param slotNo
     * @return
     */
    public int getArriveSpuDetailCount(String slotNo);
}
