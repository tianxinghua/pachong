package com.shangpin.ephub.product.business.service.studio.hubslot.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.gateway.HubSlotSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.gateway.HubSlotSpuSupplierGateway;
import com.shangpin.ephub.product.business.common.dto.CommonResult;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuSendDetailCheckDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    HubSlotSpuGateWay slotSpuGateWay;

    @Autowired
    HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;



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

                    if(SlotSpuSupplierState.NO_NEED_HANDLE.getIndex()==slotSpuState){
                        tmp.setState(SlotSpuSupplierState.NO_NEED_HANDLE.getIndex().byteValue());
                        tmp.setSupplierOperateSign(SlotSpuSupplierOperateSign.NO_NEED_HANDLE.getIndex().byteValue());
                    }else if(SlotSpuSupplierState.SEND.getIndex()==slotSpuState){
                        //
                        tmp.setState(SlotSpuSupplierState.NO_NEED_HANDLE.getIndex().byteValue());
                        tmp.setSupplierOperateSign(SlotSpuSupplierOperateSign.OTHER_SEND.getIndex().byteValue());

                    }else if(SlotSpuSupplierState.WAIT_SEND.getIndex()==slotSpuState){
                        //取消发送
                        tmp.setSupplierOperateSign(SlotSpuSupplierOperateSign.NO_HANDLE.getIndex().byteValue());

                    }

                    tmp.setRepeatMarker(SlotSpuSupplierRepeatMarker.MULTI.getIndex().byteValue());
                    tmp.setUpdateTime(new Date());

                    spuSupplierGateway.updateByPrimaryKeySelective(tmp);
                    log.info("update other spu supplier " + tmp.getSlotSpuSupplierId() + " state success");



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
    public List<HubSlotSpuSupplierDto> newFindSlotSpuSupplierListBySlotSpuId(Long slotSpuId,String supplierNo) {
        HubSlotSpuSupplierCriteriaDto criteria = new HubSlotSpuSupplierCriteriaDto();
        criteria.createCriteria().andSlotSpuIdEqualTo(slotSpuId).andSupplierNoEqualTo(supplierNo)
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
    public boolean updateSlotSpuSupplierWhenRemoveFromSlot(List<Long> slotSpuSupplierIds) {
        Date  date = new Date();
        for(Long slotSpuSupplierId :slotSpuSupplierIds){
            HubSlotSpuSupplierDto supplierDto = spuSupplierGateway.selectByPrimaryKey(slotSpuSupplierId);

            if(supplierDto.getState().intValue()==SlotSpuSupplierState.NO_NEED_HANDLE.getIndex()||
                    supplierDto.getState().intValue()==SlotSpuSupplierState.SEND.getIndex()){
                //不处理或者已发货不做修改
                log.info(" slotSpuSupplierId :" + supplierDto.getSlotSpuSupplierId() +" don't neet to update state when remove from slot");
            }else{

                HubSlotSpuSupplierDto supplierTmp = new HubSlotSpuSupplierDto();
                supplierTmp.setSlotSpuSupplierId(supplierDto.getSlotSpuSupplierId());
                supplierTmp.setState(SlotSpuSupplierState.WAIT_SEND.getIndex().byteValue());

                supplierTmp.setUpdateTime(date);
                spuSupplierGateway.updateByPrimaryKeySelective(supplierTmp);
                log.info(" slotSpuSupplierId :" + supplierDto.getSlotSpuSupplierId() +" remove from slot  success");
                //更新其它slotspusupplier状态
                List<HubSlotSpuSupplierDto> slotSpuSupplierDtos = this.findSlotSpuSupplierListOfOtherSupplierValidBySpuNoAndSupplierId(supplierDto.getSlotSpuNo(),supplierDto.getSupplierId());
                this.updateOtherSupplierSignWhenHaveSomeSupplier(slotSpuSupplierDtos,SlotSpuSupplierState.WAIT_SEND.getIndex());

                //更新slotspu
                HubSlotSpuDto spuTmp = new HubSlotSpuDto();
                spuTmp.setSpuState(SlotSpuState.WAIT_SEND.getIndex().byteValue());
                spuTmp.setSlotSpuId(supplierDto.getSlotSpuId());
                spuTmp.setUpdateTime(new Date());
                slotSpuGateWay.updateByPrimaryKeySelective(spuTmp);
                log.info(" slotSpuId  :" + supplierDto.getSlotSpuId() +" update spu success");
            }




        }

        return true;
    }

    @Override
    public CommonResult updateSlotSpuSupplierWhenSupplierSelectProduct(SlotSpuSendDetailCheckDto dto) {
        CommonResult result = new CommonResult(true,"");

        if(null!=dto){
            if(null!=dto.getSlotSpuSupplierId()){
                HubSlotSpuSupplierDto originDto = spuSupplierGateway.selectByPrimaryKey(dto.getSlotSpuSupplierId());
                if(null!=originDto){
                    if(originDto.getState().intValue()==SlotSpuSupplierState.NO_NEED_HANDLE.getIndex()||
                            originDto.getState().intValue()==SlotSpuSupplierState.SEND.getIndex()){
                        result.setSuccess(false);
                        result.setErrorReason("no need handle");
                        return result;
                    }else{
                        Date date  = new Date();
                        //更新供货商信息
                        HubSlotSpuSupplierDto tmp = new HubSlotSpuSupplierDto();
                        tmp.setSlotSpuSupplierId(originDto.getSlotSpuSupplierId());
                        tmp.setState(SlotSpuSupplierState.ADD_INVOICE.getIndex().byteValue());
                        tmp.setUpdateTime(date);
                        tmp.setUpdateUser(StringUtils.isNotBlank(dto.getUserName())?dto.getUserName():"");
                        spuSupplierGateway.updateByPrimaryKeySelective(tmp);
                        //更新slotspu信息
                       // HubSlotSpuDto slotSpuDto = slotSpuGateWay.selectByPrimaryKey(originDto.getSlotSpuId());
                        HubSlotSpuDto slotSpuTmp = new HubSlotSpuDto();
                        slotSpuTmp.setUpdateTime(date);
                        slotSpuTmp.setUpdateUser(StringUtils.isNotBlank(dto.getUserName())?dto.getUserName():"");
                        slotSpuTmp.setSpuState(SlotSpuState.ADD_INVOICE.getIndex().byteValue());
                        slotSpuTmp.setSlotSpuId(originDto.getSlotSpuId());
                        slotSpuGateWay.updateByPrimaryKeySelective(slotSpuTmp);

                        return result;
                    }
                }else{
                    result.setSuccess(false);
                    result.setErrorReason("no origin source");
                    return result;
                }
            }else{
                result.setSuccess(false);
                result.setErrorReason("request error,no parameter value");
                return result;
            }
        }else{
            result.setSuccess(false);
            result.setErrorReason("request error,no parameter value");
            return result;
        }

    }

    @Override
    public List<SlotSpuSendDetailCheckDto> updateSlotSpuSupplierWhenSupplierSend(List<SlotSpuSendDetailCheckDto> dtos) {
        List<SlotSpuSendDetailCheckDto> errorReturnList = null ;


        List<HubSlotSpuSupplierDto> supplierDtos = new ArrayList<>();
       //判断是否可以发货
        errorReturnList = this.judgeSlotSpuSupplierWhenSupplierSend(dtos);


        //可发货更新库存
        if(null==errorReturnList||errorReturnList.size()==0){
            Date date = new Date();
            for(SlotSpuSendDetailCheckDto dto:dtos){
                HubSlotSpuSupplierDto originDto = spuSupplierGateway.selectByPrimaryKey(dto.getSlotSpuSupplierId());

                //region 更新自己

                HubSlotSpuSupplierDto supplierTmp = new HubSlotSpuSupplierDto();
                supplierTmp.setSlotSpuSupplierId(originDto.getSlotSpuSupplierId());
                supplierTmp.setState(SlotSpuSupplierState.SEND.getIndex().byteValue());
                supplierTmp.setSlotNo(dto.getSlotNo());
                supplierTmp.setUpdateTime(date);
                spuSupplierGateway.updateByPrimaryKeySelective(supplierTmp);
                log.info(" slotSpuSupplierId :" + originDto.getSlotSpuSupplierId() +" update owner success");
                //更新slotspu
                HubSlotSpuDto spuTmp = new HubSlotSpuDto();
                spuTmp.setSpuState(SlotSpuState.SEND.getIndex().byteValue());
                spuTmp.setSlotSpuId(originDto.getSlotSpuId());
                spuTmp.setUpdateTime(new Date());
                slotSpuGateWay.updateByPrimaryKeySelective(spuTmp);
                log.info(" slotSpuId  :" + originDto.getSlotSpuId() +" update spu success");
                //更新其它slotspusupplier状态
                List<HubSlotSpuSupplierDto> slotSpuSupplierDtos = this.findSlotSpuSupplierListOfOtherSupplierValidBySpuNoAndSupplierId(originDto.getSlotSpuNo(),originDto.getSupplierId());
                this.updateOtherSupplierSignWhenHaveSomeSupplier(slotSpuSupplierDtos,SlotSpuState.SEND.getIndex());
                //endregion

            }


        }

        return errorReturnList;
    }

    @Override
    public List<SlotSpuSendDetailCheckDto> judgeSlotSpuSupplierWhenSupplierSend(List<SlotSpuSendDetailCheckDto> dtos) {
        List<SlotSpuSendDetailCheckDto> returnList = new ArrayList<>();
        for(SlotSpuSendDetailCheckDto dto:dtos){
            HubSlotSpuSupplierDto originDto = spuSupplierGateway.selectByPrimaryKey(dto.getSlotSpuSupplierId());
            if(null!=originDto){


                if(originDto.getState().intValue()==SlotSpuSupplierState.NO_NEED_HANDLE.getIndex()){
                    SlotSpuSendDetailCheckDto errDto = new SlotSpuSendDetailCheckDto();
                    errDto.setStudioSlotSpuSendDetailId(dto.getStudioSlotSpuSendDetailId());
                    errDto.setResultSign(false);
                    errDto.setMemo("no need send");
                    returnList.add(errDto);

                }else if(originDto.getState().intValue()==SlotSpuSupplierState.SEND.getIndex()){
                    SlotSpuSendDetailCheckDto errDto = new SlotSpuSendDetailCheckDto();
                    errDto.setStudioSlotSpuSendDetailId(dto.getStudioSlotSpuSendDetailId());
                    errDto.setResultSign(false);
                    errDto.setMemo("had been sent ,no need send");
                    returnList.add(errDto);

                }else{

                    HubSpuPendingDto hubSpuPendingDto = spuPendingGateWay.selectByPrimaryKey(originDto.getSpuPendingId());
                    if(null!=hubSpuPendingDto){
                        if(PicState.HANDLED.getIndex()==hubSpuPendingDto.getPicState()){
                            SlotSpuSendDetailCheckDto errDto = new SlotSpuSendDetailCheckDto();
                            errDto.setStudioSlotSpuSendDetailId(dto.getStudioSlotSpuSendDetailId());
                            errDto.setResultSign(false);
                            errDto.setMemo("have pic ,no need send");
                            returnList.add(errDto);
                        }
                    }
                }

            }


        }
        return returnList;
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
            }else if(slotSpuDto.getSpuState().intValue()==SlotSpuState.WAIT_SEND.getIndex()){
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
    
    @Override
    public List<HubSlotSpuSupplierDto> getSlotSpuSupplierBySlotNo(String slotNo){
       
    	HubSlotSpuSupplierCriteriaDto dto = new HubSlotSpuSupplierCriteriaDto();
    	dto.createCriteria().andSlotNoEqualTo(slotNo);
    	List<HubSlotSpuSupplierDto> hubSlotSpuSupplierDtoList = spuSupplierGateway.selectByCriteria(dto);
        return hubSlotSpuSupplierDtoList;
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
