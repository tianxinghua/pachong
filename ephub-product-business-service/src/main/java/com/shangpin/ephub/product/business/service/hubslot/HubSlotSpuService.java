package com.shangpin.ephub.product.business.service.hubslot;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;

/**
 * Created by loyalty on 17/6/10.
 */
public interface HubSlotSpuService {
    /**
     * 获取hubslotspu 对象
     * @param brandNo
     * @param spuModel
     * @return
     */
    public HubSlotSpuDto findHubSlotSpu(String brandNo,String spuModel);

    /**
     * 添加slo_spu
     * @param hubSlotSpuDto
     * @return
     * @throws Exception
     */
    public boolean addHubSlotSpu(HubSlotSpuDto hubSlotSpuDto) throws Exception;

}
