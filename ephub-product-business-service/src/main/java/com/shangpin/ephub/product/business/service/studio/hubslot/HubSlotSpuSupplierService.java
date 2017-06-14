package com.shangpin.ephub.product.business.service.studio.hubslot;

import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;

/**
 * Created by loyalty on 17/6/10.
 */
public interface HubSlotSpuSupplierService {

    /**
     * 添加
     * @param dto
     * @return
     * @throws Exception
     */
    public  boolean addHubSloSpuSupplier(HubSlotSpuSupplierDto dto) throws  Exception;
}
