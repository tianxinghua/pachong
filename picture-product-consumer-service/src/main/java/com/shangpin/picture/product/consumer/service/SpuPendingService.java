package com.shangpin.picture.product.consumer.service;

import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuPendingService {


    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    /**
     * 更新SpuPendingPic 状态
     * @param supplierId  ：供货商ID
     * @param supplierSpuNo :SPU编号
     */
    public void updateSpuPendingPicState(String  supplierId,String supplierSpuNo){

        HubSpuPendingDto spuPendingVO = new  HubSpuPendingDto();
        spuPendingVO.setPicState(PicState.HANDLED.getIndex());
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);

        HubSpuPendingWithCriteriaDto withCriteria = new HubSpuPendingWithCriteriaDto(spuPendingVO,criteria);
        spuPendingGateWay.updateByCriteriaSelective(withCriteria);
    }
}
