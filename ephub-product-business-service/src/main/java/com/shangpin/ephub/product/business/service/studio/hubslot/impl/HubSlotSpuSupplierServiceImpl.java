package com.shangpin.ephub.product.business.service.studio.hubslot.impl;

import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.gateway.HubSlotSpuSupplierGateway;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 17/6/10.
 */
@Service
@Slf4j
public class HubSlotSpuSupplierServiceImpl implements HubSlotSpuSupplierService {

    @Autowired
    HubSlotSpuSupplierGateway spuSupplierGateway;

    @Override
    public boolean addHubSloSpuSupplier(HubSlotSpuSupplierDto dto) throws Exception {

        try {
            spuSupplierGateway.insert(dto);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("save slotspusupplier error .reason :" +e.getMessage(),e);
        }
        return false;
    }
}
