package com.shangpin.ephub.product.business.ui.studio.studio.service;

import com.shangpin.ephub.product.business.ui.studio.studio.vo.*;
import com.shangpin.ephub.response.HubResponse;

/**
 * Created by Administrator on 2017/6/8.
 */
public interface IStudioService {

    public StudioPendingProductVo getPendingProductList(String supplierId);

    public SlotsVo getSupplierSlotList(String supplierId);
    public SlotInfoExtends getSlotInfo( String supplierId ,String slotNo);
    public HubResponse<?> addProductIntoSlot(String supplierId ,String slotNo,Long slotSSId,String createUser);
    public HubResponse<?> delProductFromSlot(String supplierId ,String slotNo,Long slotSSId,Long slotSSDId,String createUser);
    public HubResponse<?> checkProductAndSendSlot(String supplierId ,String slotNo);

}
