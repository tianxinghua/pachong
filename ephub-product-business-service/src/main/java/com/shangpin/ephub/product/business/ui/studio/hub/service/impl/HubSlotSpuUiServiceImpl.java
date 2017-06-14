package com.shangpin.ephub.product.business.ui.studio.hub.service.impl;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import com.shangpin.ephub.product.business.ui.studio.hub.service.HubSlotSpuUiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 17/6/10.
 */
@Service
@Slf4j
public class HubSlotSpuUiServiceImpl implements HubSlotSpuUiService
{

    @Autowired
    HubSlotSpuService slotSpuService;

    @Autowired
    HubSlotSpuSupplierService slotSpuSupplierService;

    @Override
    public boolean addHubSlotSpu(HubSlotSpuDto hubSlotSpuDto, HubSlotSpuSupplierDto hubSlotSpuSupplierDto) throws Exception {
        //验证传入数据的正确性

        //查询slotspu是否存在


        return false;
    }
}
