package com.shangpin.ephub.product.business.service.studio.hubslot.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.gateway.HubSlotSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.gateway.HubSlotSpuSupplierGateway;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import com.shangpin.ephub.product.business.service.studio.studio.StudioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 17/6/10.
 */
@Service
@Slf4j
public class HubSlotSpuSupplierServiceImpl implements HubSlotSpuSupplierService {

    @Autowired
    HubSlotSpuSupplierGateway spuSupplierGateway;

    @Autowired
    StudioService studioService;

    @Autowired
    HubSlotSpuGateWay slotSpuGateWay;

    @Autowired
    HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;



    @Override
    public boolean addHubSlotSpuSupplier(HubSlotSpuSupplierDto dto, Integer slotSpuState) throws Exception {

        try {
            List<HubSlotSpuSupplierDto> slotSpuSupplierDtos = this.findSlotSpuSupplierListOfOtherSupplierValidBySpuNoAndSupplierId(dto.getSlotSpuNo(),dto.getSupplierId());
            if(null!=slotSpuSupplierDtos&&slotSpuSupplierDtos.size()>0){
                this.updateOtherSupplierSignWhenHaveSomeSupplier(slotSpuSupplierDtos,slotSpuState);
                dto.setRepeatMarker(SlotSpuSupplierRepeatMarker.MULTI.getIndex().byteValue());
            }else{
                if(slotSpuState==SlotSpuState.NO_NEED_HANDLE.getIndex()){
                    dto.setSupplierOperateSign(SlotSpuSupplierOperateSign.NO_NEED_HANDLE.getIndex().byteValue());
                }else{
                    dto.setSupplierOperateSign(SlotSpuSupplierOperateSign.NO_HANDLE.getIndex().byteValue());
                }

                dto.setRepeatMarker(SlotSpuSupplierRepeatMarker.SINGLE.getIndex().byteValue());

            }
            spuSupplierGateway.insert(dto);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("save slotspusupplier error .reason :" +e.getMessage(),e);
        }
        return false;

    }


    @Override
    public HubSlotSpuSupplierDto getSlotSpuSupplierOfValidBySpuNoAndSupplierId(String slotSpuNo, String supplierId) {
        HubSlotSpuSupplierCriteriaDto criteria = new HubSlotSpuSupplierCriteriaDto();
        criteria.createCriteria().andSlotSpuNoEqualTo(slotSpuNo).andSupplierIdEqualTo(supplierId)
                .andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
        List<HubSlotSpuSupplierDto> hubSlotSpuSupplierDtos = spuSupplierGateway.selectByCriteria(criteria);
        if(null!=hubSlotSpuSupplierDtos&&hubSlotSpuSupplierDtos.size()>0){
            return hubSlotSpuSupplierDtos.get(0);
        }
        return null;
    }

    @Override
    public List<HubSlotSpuSupplierDto> findSlotSpuSupplierListOfOtherSupplierValidBySpuNoAndSupplierId(String slotSpuNo, String supplierId) {
        HubSlotSpuSupplierCriteriaDto criteria = new HubSlotSpuSupplierCriteriaDto();
        criteria.createCriteria().andSlotSpuNoEqualTo(slotSpuNo).andSupplierIdNotEqualTo(supplierId)
                .andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
        List<HubSlotSpuSupplierDto> hubSlotSpuSupplierDtos = spuSupplierGateway.selectByCriteria(criteria);
        return  hubSlotSpuSupplierDtos;
    }

    @Override
    public void updateOtherSupplierSignWhenHaveSomeSupplier(List<HubSlotSpuSupplierDto> slotSpuSupplierDtos,Integer slotSpuState) {
        if(null!=slotSpuSupplierDtos){
            Long slotSpuSupplierId = 0L;
            for(HubSlotSpuSupplierDto dto:slotSpuSupplierDtos){
                HubSlotSpuSupplierDto tmp = new HubSlotSpuSupplierDto();
                tmp.setSlotSpuSupplierId(dto.getSlotSpuSupplierId());
                if(dto.getState().intValue()== SlotSpuSupplierState.SEND.getIndex()){

                }else if(dto.getState().intValue()== SlotSpuSupplierState.NO_NEED_HANDLE.getIndex()){

                }else {

                    if(SlotSpuState.NO_NEED_HANDLE.getIndex()==slotSpuState){
                        tmp.setState(SlotSpuSupplierState.NO_NEED_HANDLE.getIndex().byteValue());
                    }else if(SlotSpuState.SEND.getIndex()==slotSpuState){
                        //
                        tmp.setSupplierOperateSign(SlotSpuSupplierOperateSign.OTHER_SEND.getIndex().byteValue());

                    }

                    tmp.setRepeatMarker(SlotSpuSupplierRepeatMarker.MULTI.getIndex().byteValue());
                    tmp.setUpdateTime(new Date());

                    spuSupplierGateway.updateByPrimaryKeySelective(tmp);



                }

            }
        }
    }

    @Override
    public void resumeRepeatMarker(Long slotSpuSupplierId) {
        HubSlotSpuSupplierDto tmp = new HubSlotSpuSupplierDto();
        tmp.setSlotSpuSupplierId(slotSpuSupplierId);
        tmp.setRepeatMarker(SlotSpuSupplierRepeatMarker.SINGLE.getIndex().byteValue());
        tmp.setUpdateTime(new Date());
        spuSupplierGateway.updateByPrimaryKeySelective(tmp);
    }


    @Override
    public List<HubSlotSpuSupplierDto> findSlotSpuSupplierListBySlotSpuId(Long slotSpuId) {
        HubSlotSpuSupplierCriteriaDto criteria = new HubSlotSpuSupplierCriteriaDto();
        criteria.createCriteria().andSlotSpuIdEqualTo(slotSpuId)
                .andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
        return  spuSupplierGateway.selectByCriteria(criteria);
    }

    @Override
    public void deleteSlotSpuSupplierForLogic(Long id) {
        HubSlotSpuSupplierDto dto = new HubSlotSpuSupplierDto();
        dto.setSlotSpuSupplierId(id);
        dto.setDataState(DataState.DELETED.getIndex());
        spuSupplierGateway.updateByPrimaryKeySelective(dto);
    }

    @Override
    public boolean updateSlotSpuSupplierWhenSupplierHandle(Long slotSpuSupplierId,Integer supplierHandleState) throws Exception {


        return false;
    }

    @Override
    public boolean updateSlotSpuSupplierStateWhenModifyHubData(List<HubSlotSpuSupplierDto> slotSpuSupplierDtos,HubSlotSpuDto slotSpuDto) throws Exception {

        boolean  isNeedShoot = true;
        for(HubSlotSpuSupplierDto dto:slotSpuSupplierDtos){
            if(!isShootSupplier(dto.getSupplierId())) {
                isNeedShoot = false;
                break;
            }
        }
        //更新原来的SLOTSPU状态
        if(!isNeedShoot){
            if(slotSpuDto.getSpuState().intValue()==SlotSpuState.NO_NEED_HANDLE.getIndex()){
                //不需要处理
            }else if(slotSpuDto.getSpuState().intValue()==SlotSpuState.SEND.getIndex()){
                //已发货不需要处理
            }else{
               this.updateSlotSpuState(slotSpuDto.getSlotSpuId(),SlotSpuState.NO_NEED_HANDLE.getIndex());
            }
        }else{
            if(slotSpuDto.getSpuState().intValue()==SlotSpuState.SEND.getIndex()){
                //已发货不需要处理
            }else if(slotSpuDto.getSpuState().intValue()==SlotSpuState.ADD_INVOICE.getIndex()){
                //已加入发货单 不处理
            }else if(slotSpuDto.getSpuState().intValue()==SlotSpuState.ADD_INVOICE.getIndex()){
                //原来就是等待处理  不做处理
            }else{
                this.updateSlotSpuState(slotSpuDto.getSlotSpuId(),SlotSpuState.WAIT_SEND.getIndex());
            }
        }
        //更新明细
        boolean isNeedUpdateMultSupplierSign = false;
        if(slotSpuSupplierDtos.size() ==1)  isNeedUpdateMultSupplierSign = true;
        for(HubSlotSpuSupplierDto dto:slotSpuSupplierDtos){

           this.updateSlotSpuSupplierState(dto,isNeedUpdateMultSupplierSign,isNeedShoot);



        }
        return true;

    }


    private boolean  isShootSupplier(String supplierId){


        HubSupplierValueMappingCriteriaDto criteriaDto  = new HubSupplierValueMappingCriteriaDto();
        criteriaDto.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SUPPLIER.getIndex().byteValue())
                .andSupplierValEqualTo(String.valueOf(DataState.NOT_DELETED.getIndex())).andSupplierIdEqualTo(supplierId);
        List<HubSupplierValueMappingDto> hubSupplierValueMappingDtos = hubSupplierValueMappingGateWay.selectByCriteria(criteriaDto);
        if(null!=hubSupplierValueMappingDtos&&hubSupplierValueMappingDtos.size()>0){
            return false;
        }else{
            return true;
        }

    }


    private boolean updateSlotSpuState(Long slotSpuId,Integer slotspuState){
        HubSlotSpuDto slotSpuDto = new HubSlotSpuDto();
        slotSpuDto.setSlotSpuId(slotSpuId);
        slotSpuDto.setSpuState(slotspuState.byteValue());
        slotSpuDto.setUpdateTime(new Date());
        slotSpuGateWay.updateByPrimaryKeySelective(slotSpuDto);
        return true;
    }


    private boolean updateSlotSpuSupplierState(HubSlotSpuSupplierDto dto,boolean isNeedUpdateMultSupplierSign,boolean isNeedShoot){
        HubSlotSpuSupplierDto tmp = new HubSlotSpuSupplierDto();
        tmp.setUpdateTime(new Date());
        if(isNeedUpdateMultSupplierSign){
            tmp.setRepeatMarker(SlotSpuSupplierRepeatMarker.SINGLE.getIndex().byteValue());
        }
        if(!isNeedShoot){
            if(dto.getState().intValue()!=SlotSpuSupplierState.NO_NEED_HANDLE.getIndex()){
                tmp.setSlotSpuSupplierId(dto.getSlotSpuSupplierId());
                tmp.setState(SlotSpuSupplierState.NO_NEED_HANDLE.getIndex().byteValue());
                tmp.setSupplierOperateSign(SlotSpuSupplierOperateSign.NO_NEED_HANDLE.getIndex().byteValue());
                spuSupplierGateway.updateByPrimaryKeySelective(tmp);
            }
        }else{
            if(dto.getState().intValue()==SlotSpuSupplierState.NO_NEED_HANDLE.getIndex()){
                tmp.setSlotSpuSupplierId(dto.getSlotSpuSupplierId());
                tmp.setState(SlotSpuSupplierState.WAIT_SEND.getIndex().byteValue());
                tmp.setSupplierOperateSign(SlotSpuSupplierOperateSign.NO_HANDLE.getIndex().byteValue());
                spuSupplierGateway.updateByPrimaryKeySelective(tmp);
            }
        }
        return true;
    }

}
