package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.enumeration.HandleFromState;
import com.shangpin.ephub.client.data.mysql.enumeration.HandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
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

    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;

    public boolean updateSpuStateFromWaitAuditToWaitHandle(Long spuPendingId){
        boolean  result = true;
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId)
                .andSpuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        HubSpuPendingWithCriteriaDto criteriaSpu = new HubSpuPendingWithCriteriaDto(hubSpuPending,criteria);
        spuPendingGateWay.updateByCriteriaSelective(criteriaSpu);
        return result;
    }


    public boolean updateSpuStateFromHandledToWaitHandle(Long spuPendingId){
        boolean  result = true;
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
        hubSpuPending.setSpuPendingId(spuPendingId);
        hubSpuPending.setHandleFrom(HandleFromState.AUTOMATIC_HANDLE.getIndex());
        hubSpuPending.setHandleState(HandleState.HUB_EXIST.getIndex());
        spuPendingGateWay.updateByPrimaryKeySelective(hubSpuPending);
        return result;
    }


    public boolean updateSpuStateToWaitHandleIfSkuStateHaveWaitHandle(Long spuPendingId){
        boolean  result = true;
        HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
        criteriaDto.createCriteria().andSpuPendingIdEqualTo(spuPendingId)
                .andSkuStateNotEqualTo(SpuStatus.SPU_HANDLED.getIndex().byteValue())
                .andSkuStateNotEqualTo(SpuStatus.SPU_HANDLING.getIndex().byteValue());
        int i= hubSkuPendingGateWay.countByCriteria(criteriaDto);
        if(i>0){
            HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
            hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
            hubSpuPending.setSpuPendingId(spuPendingId);

            spuPendingGateWay.updateByPrimaryKeySelective(hubSpuPending);

        }
        return result;
    }


    public boolean updateSpuStateToHandle(Long spuPendingId){
        boolean  result = true;
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setSpuState(SpuStatus.SPU_HANDLED.getIndex().byteValue());
        hubSpuPending.setSpuPendingId(spuPendingId);
        spuPendingGateWay.updateByPrimaryKeySelective(hubSpuPending);
        return result;
    }


    public void updateStotckState(Long spuPendingId,Integer stockState){
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        if(stockState>0){
            hubSpuPending.setStockState(StockState.HANDLED.getIndex());
        }else if(stockState<=0){
            hubSpuPending.setStockState(StockState.NOSTOCK.getIndex());
        }
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
        HubSpuPendingWithCriteriaDto criteriaSpu = new HubSpuPendingWithCriteriaDto(hubSpuPending,criteria);
        spuPendingGateWay.updateByCriteriaSelective(criteriaSpu);

    }
}
