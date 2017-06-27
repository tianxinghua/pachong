package com.shangpin.ephub.data.schedule.service;

import com.shangpin.ephub.client.data.mysql.enumeration.HandleFromState;
import com.shangpin.ephub.client.data.mysql.enumeration.HandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        hubSpuPending.setSpuState(SpuState.INFO_PECCABLE.getIndex());
        hubSpuPending.setUpdateTime(new Date());
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId)
                .andSpuStateEqualTo(SpuState.INFO_IMPECCABLE.getIndex());
        HubSpuPendingWithCriteriaDto criteriaSpu = new HubSpuPendingWithCriteriaDto(hubSpuPending,criteria);
        spuPendingGateWay.updateByCriteriaSelective(criteriaSpu);
        return result;
    }


    public boolean updateSpuStateFromHandledToWaitHandle(Long spuPendingId){
        boolean  result = true;
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setSpuState(SpuState.INFO_PECCABLE.getIndex());
        hubSpuPending.setSpuPendingId(spuPendingId);
        hubSpuPending.setHandleFrom(HandleFromState.AUTOMATIC_HANDLE.getIndex());
        hubSpuPending.setHandleState(HandleState.HUB_EXIST.getIndex());
        hubSpuPending.setUpdateTime(new Date());
        spuPendingGateWay.updateByPrimaryKeySelective(hubSpuPending);
        return result;
    }


    public boolean updateSpuStateToWaitHandleIfSkuStateHaveWaitHandle(Long spuPendingId){
        boolean  result = true;
        HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
        criteriaDto.createCriteria().andSpuPendingIdEqualTo(spuPendingId)
                .andSkuStateNotEqualTo(SpuState.HANDLED.getIndex())
                .andSkuStateNotEqualTo(SpuState.HANDLING.getIndex())
                .andFilterFlagEqualTo(SpuState.INFO_IMPECCABLE.getIndex());//借用过滤枚举 为1的不过滤的

        int i= hubSkuPendingGateWay.countByCriteria(criteriaDto);
        if(i>0){
            HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
            hubSpuPending.setSpuState(SpuState.INFO_PECCABLE.getIndex());
            hubSpuPending.setSpuPendingId(spuPendingId);
            hubSpuPending.setUpdateTime(new Date());
            spuPendingGateWay.updateByPrimaryKeySelective(hubSpuPending);

        }
        return result;
    }


    public boolean updateSpuStateToHandle(Long spuPendingId){
        boolean  result = true;
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setSpuState(SpuState.HANDLED.getIndex());
        hubSpuPending.setSpuPendingId(spuPendingId);
        hubSpuPending.setUpdateTime(new Date());
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
        hubSpuPending.setUpdateTime(new Date());
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
        HubSpuPendingWithCriteriaDto criteriaSpu = new HubSpuPendingWithCriteriaDto(hubSpuPending,criteria);
        spuPendingGateWay.updateByCriteriaSelective(criteriaSpu);

    }


    public int getStockTotalBySpuPendingId(Long spuPendingId){

        return hubSkuPendingGateWay.sumStockBySpuPendingId(spuPendingId);

    }
}
