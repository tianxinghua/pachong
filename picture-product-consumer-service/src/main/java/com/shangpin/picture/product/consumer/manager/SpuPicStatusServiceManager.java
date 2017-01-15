package com.shangpin.picture.product.consumer.manager;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by lizhongren on 2017/1/15.
 *  更新SPUPENDING 中 图片状态
 */
@Component
@Slf4j
public class SpuPicStatusServiceManager {
     @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

     public void updatePicStatus(Long supplierSpuId,Byte picStatus){
         HubSpuPendingCriteriaDto criteria= new HubSpuPendingCriteriaDto();
         criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
         HubSpuPendingDto spuPending = new HubSpuPendingDto();
         spuPending.setPicState(picStatus);
         HubSpuPendingWithCriteriaDto criteriaDto = new HubSpuPendingWithCriteriaDto(spuPending,criteria);
         spuPendingGateWay.updateByCriteriaSelective(criteriaDto);
     }
}
