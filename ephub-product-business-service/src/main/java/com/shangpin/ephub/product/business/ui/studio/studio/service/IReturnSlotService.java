package com.shangpin.ephub.product.business.ui.studio.studio.service;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterDto;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.ReturnSlotQueryDto;
import com.shangpin.ephub.response.HubResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
public interface IReturnSlotService {

    List<StudioSlotReturnMasterDto> getReturnSlotList(ReturnSlotQueryDto queryDto);

    boolean ReceiveReturnSlot(Long supplierId,Long id,String userName);
}
