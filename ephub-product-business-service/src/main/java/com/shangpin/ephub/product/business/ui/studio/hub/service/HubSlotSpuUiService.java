package com.shangpin.ephub.product.business.ui.studio.hub.service;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;

/**
 * Created by loyalty on 17/6/10.
 */
public interface HubSlotSpuUiService {


    /**
     * 添加slo_spu
     * @param hubSlotSpuDto
     * @param hubSlotSpuSupplierDto
     * @return
     * @throws Exception
     */
    public boolean addHubSlotSpu(HubSlotSpuDto hubSlotSpuDto, HubSlotSpuSupplierDto hubSlotSpuSupplierDto) throws Exception;

}
