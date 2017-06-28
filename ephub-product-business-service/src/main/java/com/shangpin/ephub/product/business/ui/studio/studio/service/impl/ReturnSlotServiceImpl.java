package com.shangpin.ephub.product.business.ui.studio.studio.service.impl;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.*;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnDetailGateWay;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnMasterGateWay;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.ReturnSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IReturnSlotService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.ReturnSlotInfo;
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

    @Autowired
    StudioSlotReturnDetailGateWay studioSlotReturnDetailGateWay;
    /**
     * 获取未收回的返回单
     * @param queryDto
     * @return
     */
    public List<StudioSlotReturnMasterDto> getReturnSlotList(ReturnSlotQueryDto queryDto){
        StudioSlotReturnMasterCriteriaDto dto = new StudioSlotReturnMasterCriteriaDto();
        StudioSlotReturnMasterCriteriaDto.Criteria  criteria = dto.createCriteria().andSendStateEqualTo((byte)1);
        if(StringUtils.isEmpty(queryDto.getArriveState())){
            criteria.andArriveStateEqualTo((byte)0);
        }else{
            criteria.andArriveStateEqualTo((byte)queryDto.getArriveState());
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
   public  boolean ReceiveReturnSlot(String supplierId,Long id,String userName){
       StudioSlotReturnMasterDto dto = new StudioSlotReturnMasterDto();
       dto.setStudioSlotReturnMasterId(id);
       dto.setArriveUser(userName);
       dto.setArriveTime(new Date());
       dto.setArriveState((byte)1);
       dto.setState((byte)1);
       return studioSlotReturnMasterGateWay.updateByPrimaryKeySelective(dto)>0;
   }

    /**
     * 获取返货单详情
     * @param supplierId
     * @param id
     * @return
     */
   public ReturnSlotInfo getReceivedSlotInfo(String supplierId, Long id){
       ReturnSlotInfo result = new ReturnSlotInfo();

       StudioSlotReturnMasterDto studioSlot = studioSlotReturnMasterGateWay.selectByPrimaryKey(id);

       result.setStudioSlotReturnMasterId(studioSlot.getStudioSlotReturnMasterId());
       result.setStudioSendNo(studioSlot.getStudioSendNo());
       result.setQuantity(studioSlot.getQuantity());
       result.setActualQuantity(studioSlot.getActualQuantity());
       result.setTrackNo(studioSlot.getTrackNo());

       StudioSlotReturnDetailCriteriaDto  dto = new StudioSlotReturnDetailCriteriaDto();
       dto.createCriteria().andStudioSlotReturnMasterIdEqualTo(id);

       List<StudioSlotReturnDetailDto> detailDtoList = studioSlotReturnDetailGateWay.selectByCriteria(dto);
       result.setDetailDtoList(detailDtoList);

      return result;

   }

    /**
     *
     * @param supplierId
     * @param id
     * @param spuNo
     * @param userName
     * @return
     */
   public StudioSlotReturnDetailDto addProductFromScan(String supplierId,Long id,String spuNo,String userName){

       StudioSlotReturnDetailCriteriaDto  dto = new StudioSlotReturnDetailCriteriaDto();
       dto.createCriteria().andSupplierIdEqualTo(supplierId).andStudioSlotReturnMasterIdEqualTo(id).andSlotSpuNoEqualTo(spuNo);

       List<StudioSlotReturnDetailDto> detailDtoList = studioSlotReturnDetailGateWay.selectByCriteria(dto);
       StudioSlotReturnDetailDto returnDetailDto =null;
       int i = 0;
       if (detailDtoList!=null && detailDtoList.size()>0){
           returnDetailDto = detailDtoList.get(0);
           StudioSlotReturnDetailDto detailDto = new StudioSlotReturnDetailDto();
           detailDto.setArriveState((byte)1);
           detailDto.setArriveUser(userName);
           detailDto.setArriveTime(new Date());
           detailDto.setStudioSlotReturnDetailId(returnDetailDto.getStudioSlotReturnDetailId());
           i = studioSlotReturnDetailGateWay.updateByPrimaryKeySelective(detailDto);
           if(i>0){
               returnDetailDto.setArriveState((byte)1);
           }
       }
       return returnDetailDto;
   }

    /**
     * 拣货结果确认
     * @param supplierId
     * @param id
     * @return
     */
   public ReturnSlotInfo confirmSlotInfo(String supplierId, Long id){
       ReturnSlotInfo result = new ReturnSlotInfo();

       StudioSlotReturnMasterDto studioSlot = studioSlotReturnMasterGateWay.selectByPrimaryKey(id);

       result.setStudioSlotReturnMasterId(studioSlot.getStudioSlotReturnMasterId());
       result.setStudioSendNo(studioSlot.getStudioSendNo());
       result.setQuantity(studioSlot.getQuantity());
       result.setActualQuantity(studioSlot.getActualQuantity());
       result.setTrackNo(studioSlot.getTrackNo());

       StudioSlotReturnDetailCriteriaDto  dto = new StudioSlotReturnDetailCriteriaDto();
       dto.createCriteria().andStudioSlotReturnMasterIdEqualTo(id).andArriveStateEqualTo((byte)0);

       List<StudioSlotReturnDetailDto> detailDtoList = studioSlotReturnDetailGateWay.selectByCriteria(dto);
       result.setDetailDtoList(detailDtoList);

       return  result;
   }
}
