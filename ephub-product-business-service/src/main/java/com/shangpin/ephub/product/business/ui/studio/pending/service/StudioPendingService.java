package com.shangpin.ephub.product.business.ui.studio.pending.service;

import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingUpdatedVo;
import com.shangpin.ephub.response.HubResponse;

import java.util.List;

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


    /**
     * 更新单个pending数据，一个pending数据包括一个PengdingSpu和对应的多个PendingSku
     * @param pendingProductDto
     */
    public HubResponse<PendingUpdatedVo> updatePendingProduct(PendingProductDto pendingProductDto);
    /**
     * 批量更新pending数据
     * @param pendingProducts
     */
    public HubResponse<List<PendingUpdatedVo>> batchUpdatePendingProduct(PendingProducts pendingProducts);






}
