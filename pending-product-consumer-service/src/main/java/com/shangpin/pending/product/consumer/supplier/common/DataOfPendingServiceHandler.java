package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lizhongren on 2017/2/13.
 */
@Component
public class DataOfPendingServiceHandler {
    @Autowired
    private HubSpuPendingGateWay hubSpuPendingGateWay;

    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;


    public int getStockTotalBySpuPendingId(Long spuPendingId){

       return hubSkuPendingGateWay.sumStockBySpuPendingId(spuPendingId);

    }
}
