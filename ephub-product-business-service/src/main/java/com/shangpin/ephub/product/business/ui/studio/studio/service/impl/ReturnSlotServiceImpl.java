package com.shangpin.ephub.product.business.ui.studio.studio.service.impl;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterDto;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnMasterGateWay;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.ReturnSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IReturnSlotService;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
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
     * @param queryDto
     * @return
     */
    public List<StudioSlotReturnMasterDto> getReturnSlotList(ReturnSlotQueryDto queryDto){
        StudioSlotReturnMasterCriteriaDto dto = new StudioSlotReturnMasterCriteriaDto();
        StudioSlotReturnMasterCriteriaDto.Criteria  criteria = dto.createCriteria().andSendStateEqualTo((byte)1);
        if(StringUtils.isEmpty(queryDto.getArriveState())){
            criteria.andSendStateEqualTo((byte)0);
        }else{
            criteria.andSendStateEqualTo((byte)queryDto.getArriveState());
        }
        return studioSlotReturnMasterGateWay.selectByCriteria(dto);
   }

    /**
     * 供货商接收返回单
     * @param supplierId
     * @param id
     * @param userName
     * @return
     */
   public  boolean ReceiveReturnSlot(Long supplierId,Long id,String userName){
       StudioSlotReturnMasterDto dto = new StudioSlotReturnMasterDto();
       dto.setStudioSlotReturnMasterId(id);
       dto.setArriveUser(userName);
       dto.setArriveTime(new Date());
       dto.setArriveState((byte)1);
       dto.setState((byte)1);
       return studioSlotReturnMasterGateWay.updateByPrimaryKeySelective(dto)>0;
   }

}
