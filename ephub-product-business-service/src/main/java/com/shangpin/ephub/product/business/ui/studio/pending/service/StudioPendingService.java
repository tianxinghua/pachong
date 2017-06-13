package com.shangpin.ephub.product.business.ui.studio.pending.service;

import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;

/**
 * Created by loyalty on 17/6/8.
 */
public interface StudioPendingService {

    /**
     * 获取HUB_SPU_PENDING  信息
     * @param pendingQuryDto
     * @param flag
     * @return
     */
    public PendingProducts findPendingProducts(PendingQuryDto pendingQuryDto, boolean flag);





}
