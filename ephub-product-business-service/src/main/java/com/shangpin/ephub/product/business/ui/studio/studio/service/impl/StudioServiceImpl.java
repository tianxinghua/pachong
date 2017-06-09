package com.shangpin.ephub.product.business.ui.studio.studio.service.impl;

import com.shangpin.ephub.client.data.mysql.studio.gateway.HubStudioGateWay;
import com.shangpin.ephub.client.data.mysql.studio.gateway.StudioGateWay;
import com.shangpin.ephub.product.business.ui.studio.service.IStudioService;
import com.shangpin.ephub.product.business.ui.studio.vo.StudioQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IStudioService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.StudioQueryDto;
import com.shangpin.ephub.response.HubResponse;

/**
 * Created by Administrator on 2017/6/8.
 */
public class StudioServiceImpl implements IStudioService {

    private StudioGateWay studioGateWay;
    private HubStudioGateWay hubStudioGateWay;

    public HubResponse<?> getPendingProductList(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> getSupplierSlotList(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> getSlotInfo(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> addProductIntoSlot(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> delProductFromSlot(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> checkProductAndSendSlot(StudioQueryDto queryDto){
        return null;
    }
}
