package com.shangpin.ephub.product.business.ui.studio.studio.service;

import com.shangpin.ephub.product.business.ui.studio.studio.vo.StudioQueryDto;
import com.shangpin.ephub.response.HubResponse;

/**
 * Created by Administrator on 2017/6/8.
 */
public interface IStudioService {
    public HubResponse<?> getPendingProductList(StudioQueryDto queryDto);
    public HubResponse<?> getSupplierSlotList(StudioQueryDto queryDto);
    public HubResponse<?> getSlotInfo(StudioQueryDto queryDto);
    public HubResponse<?> addProductIntoSlot(StudioQueryDto queryDto);
    public HubResponse<?> delProductFromSlot(StudioQueryDto queryDto);
    public HubResponse<?> checkProductAndSendSlot(StudioQueryDto queryDto);

}
