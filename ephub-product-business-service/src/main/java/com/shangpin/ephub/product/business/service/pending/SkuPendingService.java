package com.shangpin.ephub.product.business.service.pending;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;

import java.util.List;

/**
 * Created by lizhongren on 2017/11/23.
 */
public interface SkuPendingService {

    /**
     * 设置SKU尺码
     * @param spuPendingId
     * @param hubBrand
     * @param hubCategory
     * @return  如果获取不到信息 返回false
     */
    public boolean setWaitHandleSkuPendingSize(Long spuPendingId,String hubBrand,String hubCategory);

    /**
     * 获取待处理的SKU
     * @param spuPendingId
     * @return
     */
    public List<HubSkuPendingDto> getWaitHandleSkuPending(Long spuPendingId);

}
