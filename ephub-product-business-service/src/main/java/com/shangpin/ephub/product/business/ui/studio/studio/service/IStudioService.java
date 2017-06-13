package com.shangpin.ephub.product.business.ui.studio.studio.service;

import com.shangpin.ephub.product.business.ui.studio.studio.vo.*;
import com.shangpin.ephub.response.HubResponse;

/**
 * Created by Administrator on 2017/6/8.
 */
public interface IStudioService {

    public StudioPendingProductVo getPendingProductList(StudioQueryDto queryDto);

    public SlotsVo getSupplierSlotList(StudioQueryDto queryDto);
    public SlotInfoExtends getSlotInfo(StudioQueryDto queryDto);
    public HubResponse<?> addProductIntoSlot(StudioQueryDto queryDto);
    public HubResponse<?> delProductFromSlot(StudioQueryDto queryDto);
    public HubResponse<?> checkProductAndSendSlot(StudioQueryDto queryDto);

}
