package com.shangpin.ephub.product.business.service.studio.slotsendreturn.impl;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.gateway.HubSlotSpuGateWay;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotStudioArriveState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotStudioSendState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotSupplierArriveState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotSupplierSendState;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailDto;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnDetailGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.product.business.service.studio.slotsendreturn.SlotSendReturnService;
import com.shangpin.ephub.product.business.service.studio.slotsendreturn.dto.SlotSpuSendAndReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loyalty on 17/7/4.
 */
@Service
public class SlotSendReturnServiceImpl implements SlotSendReturnService {

    @Autowired
    StudioSlotSpuSendDetailGateWay spuSendDetailGateWay;

    @Autowired
    StudioSlotReturnDetailGateWay  returnDetailGateWay;

    @Autowired
    HubSlotSpuGateWay slotSpuGateWay;


    private List<StudioSlotSpuSendDetailDto> getSendDetailBySlotNo(String slotNo){
        StudioSlotSpuSendDetailCriteriaDto  criteria =new StudioSlotSpuSendDetailCriteriaDto();
        criteria.setPageSize(500);
        criteria.createCriteria().andSlotNoEqualTo(slotNo);
        List<StudioSlotSpuSendDetailDto> studioSlotSpuSendDetailDtos = spuSendDetailGateWay.selectByCriteria(criteria);
        return studioSlotSpuSendDetailDtos;
    }

    private List<StudioSlotReturnDetailDto> getReturnDetailByBarcode(String barcode){


        StudioSlotReturnDetailCriteriaDto criteria = new StudioSlotReturnDetailCriteriaDto();
        criteria.createCriteria().andBarcodeEqualTo(barcode);

        List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtos = returnDetailGateWay.selectByCriteria(criteria);
        return studioSlotReturnDetailDtos;
    }

    @Override
    public List<SlotSpuSendAndReturn> findSlotSpuSendAndReturnMsgBySlotNo(String slotNo) {
        List<SlotSpuSendAndReturn> returns = new ArrayList<>();
        List<StudioSlotSpuSendDetailDto> sendDetails = this.getSendDetailBySlotNo(slotNo);
        for(StudioSlotSpuSendDetailDto sendDetailDto:sendDetails){

            HubSlotSpuDto slotSpuDto = this.getSlotSpuBySpuNo(sendDetailDto.getSlotSpuNo());
            if(null== slotSpuDto) continue;

            List<StudioSlotReturnDetailDto> returnDetails = getReturnDetailByBarcode(sendDetailDto.getBarcode());
            if(null!=returnDetails&&returnDetails.size()>0){
               for(StudioSlotReturnDetailDto returnDetailDto:returnDetails){
                   SlotSpuSendAndReturn sendAndReturn = new SlotSpuSendAndReturn();
                   setSpuSendAndDetailMsg(sendDetailDto, slotSpuDto, sendAndReturn);
                   if(StudioSlotStudioSendState.SEND.getIndex()==returnDetailDto.getSendState().intValue()){
                       sendAndReturn.setReturned(true);
                   }else{
                       sendAndReturn.setReturned(false);
                   }
                   if(StudioSlotSupplierArriveState.RECEIVED.getIndex()==returnDetailDto.getArriveState().intValue()||
                           StudioSlotSupplierArriveState.NOT_ACCEPTANCE.getIndex()==returnDetailDto.getArriveState().intValue()){
                       sendAndReturn.setSupplierReceived(true);
                   }else{
                       sendAndReturn.setSupplierReceived(false);
                   }
                   returns.add(sendAndReturn);
               }
            }else{
                SlotSpuSendAndReturn sendAndReturn = new SlotSpuSendAndReturn();
                setSpuSendAndDetailMsg(sendDetailDto, slotSpuDto, sendAndReturn);
                sendAndReturn.setReturned(false);
                sendAndReturn.setSupplierReceived(false);
                returns.add(sendAndReturn);
            }
        }
        return returns;
    }

    private void setSpuSendAndDetailMsg(StudioSlotSpuSendDetailDto sendDetailDto, HubSlotSpuDto slotSpuDto, SlotSpuSendAndReturn sendAndReturn) {
        sendAndReturn.setHubBrandNo(slotSpuDto.getBrandNo());
        sendAndReturn.setHubCategoryNo(slotSpuDto.getCategoryNo());
        sendAndReturn.setSpuModel(slotSpuDto.getSpuModel());
        sendAndReturn.setSupplierSpuModel(sendDetailDto.getSupplierSpuModel());
        if(StudioSlotSupplierSendState.SEND.getIndex()==sendDetailDto.getSendState().intValue()){
            sendAndReturn.setSupplierSend(true);
        }else{
            sendAndReturn.setSupplierSend(false);
        }
        if(StudioSlotStudioArriveState.RECEIVED.getIndex()==sendDetailDto.getArriveState().intValue()||
                StudioSlotStudioArriveState.NOT_ACCEPTANCE.getIndex()==sendDetailDto.getArriveState().intValue()){
            sendAndReturn.setReceived(true);
        }else{
            sendAndReturn.setReceived(false);
        }
    }

    private HubSlotSpuDto getSlotSpuBySpuNo(String spuNo){

        HubSlotSpuCriteriaDto criteria = new HubSlotSpuCriteriaDto();
        criteria.createCriteria().andSlotSpuNoEqualTo(spuNo);
        List<HubSlotSpuDto> slotSpuDtos = slotSpuGateWay.selectByCriteria(criteria);
        if(null!=slotSpuDtos&&slotSpuDtos.size()>0){
            return  slotSpuDtos.get(0);
        }else{
            return null;
        }
    }
}
