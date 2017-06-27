package com.shangpin.ephub.product.business.ui.studio.studio.service.impl;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterDto;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnMasterGateWay;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IReturnSlotService;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
@Service
@Slf4j
public class ReturnSlotServiceImpl implements IReturnSlotService {

    @Autowired
    StudioSlotReturnMasterGateWay studioSlotReturnMasterGateWay;

    /**
     * 获取未收回的返回单
     * @param supplierId
     * @return
     */
    public List<StudioSlotReturnMasterDto> getReturnSlotList(Long supplierId){
        StudioSlotReturnMasterCriteriaDto dto = new StudioSlotReturnMasterCriteriaDto();
        dto.createCriteria().andSendStateEqualTo((byte)1).andArriveStateEqualTo((byte)0);
        return studioSlotReturnMasterGateWay.selectByCriteria(dto);
   }
}
