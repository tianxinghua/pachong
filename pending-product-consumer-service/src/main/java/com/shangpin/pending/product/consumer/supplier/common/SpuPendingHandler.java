package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.pending.product.consumer.common.enumeration.SpuStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lizhongren on 2017/1/13.
 */
@Component
@Slf4j
public class SpuPendingHandler {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    public boolean updateSpuStateToWaitHandle(Long spuPendingId){
        boolean  result = true;
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
        HubSpuPendingWithCriteriaDto criteriaSpu = new HubSpuPendingWithCriteriaDto(hubSpuPending,criteria);
        spuPendingGateWay.updateByCriteriaSelective(criteriaSpu);
        return result;
    }
}
