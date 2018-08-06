package com.shangpin.asynchronous.task.consumer.service.pending;

import com.shangpin.ephub.client.data.mysql.enumeration.SourceFromEnum;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PendingCommonService {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    public HubSpuPendingDto getHandleWebSpiderdSpuPending(String brandNo, String spuModel){
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andHubBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel)
                .andSpuStateEqualTo(SpuState.HANDLED.getIndex()).andSourceFromEqualTo(SourceFromEnum.TYPE_WEBSPIDER.getIndex().byteValue());
        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);
        if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0) return hubSpuPendingDtos.get(0);
        return null;
    }

}
