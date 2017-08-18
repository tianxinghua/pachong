package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.enumeration.MsgMissHandleState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingNohandleReasonGateWay;
import com.shangpin.pending.product.consumer.common.enumeration.DataStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2017/8/15.
 *  供货商错误数据处理
 */
@Service
@Slf4j
public class SpuPendingMsgHandleService {

    @Autowired
    HubSpuPendingNohandleReasonGateWay  spuPendingNohandleReasonGateWay;

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    public Map<Byte,List<HubSpuPendingNohandleReasonDto>> findSpuErrorMsgBySupplierIdAndSpuPendingId(String supplierId, Long spuPendingId){
        HubSpuPendingNohandleReasonCriteriaDto criteriaDto = new HubSpuPendingNohandleReasonCriteriaDto();
        criteriaDto.setPageSize(100);
        criteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andSpuPendingIdEqualTo(spuPendingId).andDataStateEqualTo(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
        List<HubSpuPendingNohandleReasonDto> hubSpuPendingNohandleReasonDtos = spuPendingNohandleReasonGateWay.selectByCriteria(criteriaDto);
        Map<Byte,List<HubSpuPendingNohandleReasonDto>> map = new HashMap<>();
        for(HubSpuPendingNohandleReasonDto dto:hubSpuPendingNohandleReasonDtos){
            if(map.containsKey(dto.getErrorType())){
                map.get(dto.getErrorType()).add(dto);
            }else{
                List<HubSpuPendingNohandleReasonDto> list =  new ArrayList<>();
                list.add(dto);
                map.put(dto.getErrorType(),list);
            }

        }
        return map;
    }

    public void updateSpuErrorMsgDateState(List<HubSpuPendingNohandleReasonDto> dtos){
        for(HubSpuPendingNohandleReasonDto dto:dtos){
            HubSpuPendingNohandleReasonDto tmp = new HubSpuPendingNohandleReasonDto();
            tmp.setSpuPendingNohandleReasonId(dto.getSpuPendingNohandleReasonId());
            tmp.setDataState(DataStatus.DATA_STATUS_DELETE.getIndex().byteValue());
            spuPendingNohandleReasonGateWay.updateByPrimaryKey(tmp) ;
        }

    }

    public void updateSpuMsgMissHandleState(Long spuPendingId){
        HubSpuPendingDto spuPending = new HubSpuPendingDto();
        spuPending.setSpuPendingId(spuPendingId);
        spuPending.setMsgMissHandleState(MsgMissHandleState.SUPPLIER_HAVE_HANDLED.getIndex());
        spuPendingGateWay.updateByPrimaryKeySelective(spuPending);
    }

}
