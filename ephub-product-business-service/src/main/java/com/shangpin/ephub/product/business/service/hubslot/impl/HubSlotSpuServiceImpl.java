package com.shangpin.ephub.product.business.service.hubslot.impl;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.gateway.HubSlotSpuGateWay;
import com.shangpin.ephub.product.business.service.hubslot.HubSlotSpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 17/6/10.
 */
@Service
@Slf4j
public class HubSlotSpuServiceImpl implements HubSlotSpuService {

    @Autowired
    HubSlotSpuGateWay slotSpuGateWay;

    @Override
    public HubSlotSpuDto findHubSlotSpu(String brandNo, String spuModel) {
        return null;
    }

    @Override
    public boolean addHubSlotSpu(HubSlotSpuDto hubSlotSpuDto) throws Exception {
        return false;
    }
}
