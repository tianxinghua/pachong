package com.shangpin.ephub.product.business.service.studio.hubslot.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.SlotSpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuPendingStudioState;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuNoTypeDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.SpuNoGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.gateway.HubSlotSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.product.business.service.pending.PendingService;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 17/6/10.
 */
@Service
@Slf4j
public class HubSlotSpuServiceImpl implements HubSlotSpuService {

    @Autowired
    HubSlotSpuGateWay slotSpuGateWay;

//    @Autowired
//    SpuNoGateWay spuNoGateWay;
//
//    @Autowired
//    HubSlotSpuSupplierService hubSlotSpuSupplierService;
//
//
//    SpuNoTypeDto spuNoTypeDto = new SpuNoTypeDto();

    @Autowired
    HubSlotSpuSupplierService hubSlotSpuSupplierService;

    @Autowired
    PendingService pendingService;


    SpuNoTypeDto spuNoTypeDto = new SpuNoTypeDto();




    @Override
    public HubSlotSpuDto findHubSlotSpu(String brandNo, String spuModel) {

        HubSlotSpuCriteriaDto criteriaDto = new HubSlotSpuCriteriaDto();
        criteriaDto.createCriteria().andBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel);
        List<HubSlotSpuDto> slotSpuDtos = slotSpuGateWay.selectByCriteria(criteriaDto);
        if(null!=slotSpuDtos&&slotSpuDtos.size()>0){
          return  slotSpuDtos.get(0);
        }

        return null;
    }

    @Override
    public boolean addSlotSpuAndSupplier(PendingProductDto pendingProductDto) throws Exception {
        HubSlotSpuDto slotSpuDto = new HubSlotSpuDto();
        HubSlotSpuSupplierDto slotSpuSupplierDto  = new HubSlotSpuSupplierDto();

        try {
            //slotspu 处理
            HubSlotSpuDto originSlotSpu = this.findHubSlotSpu(pendingProductDto.getHubBrandNo(),pendingProductDto.getSpuModel());


            if(null==originSlotSpu){
                this.transPendingToSlot(pendingProductDto,slotSpuDto);
                try {
                    slotSpuGateWay.insert(slotSpuDto);
                } catch (Exception e) {
                    //一般情况下是唯一索引冲突，需要获取数据

                    e.printStackTrace();
                    handleInsertSlotSpuException(pendingProductDto,slotSpuDto);
                }
            }else{
                slotSpuDto.setSlotSpuId(originSlotSpu.getSlotSpuId());
                slotSpuDto.setSlotSpuNo(originSlotSpu.getSlotSpuNo());

            }

            //slotspusupplier 处理

            if(null==hubSlotSpuSupplierService.getSlotSpuSupplierOfValidBySpuNoAndSupplierId(slotSpuDto.getSlotSpuNo(),pendingProductDto.getSupplierId())){

                this.transPendingToSlotSupplier(pendingProductDto,slotSpuSupplierDto,slotSpuDto.getSlotSpuId(),slotSpuDto.getSlotSpuNo());

                hubSlotSpuSupplierService.addHubSloSpuSupplier(slotSpuSupplierDto);
            }

            //spupending 处理
            pendingService.updatePendingSlotState(slotSpuSupplierDto.getSpuPendingId(), SpuPendingStudioState.HANDLED);





            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("addSlotSpuAndSupplier  error. reasno:"+e.getMessage(),e);
        }
        return false;
    }

    @Override
    public boolean updateSlotSpu(PendingProductDto pendingProductDto) throws Exception {
        //获取老的数据
        HubSpuPendingDto originSpuPendingDto = pendingService.getSpuPendingByKey(pendingProductDto.getSpuPendingId());
        if(isNeedHandleSlotSpu(pendingProductDto,originSpuPendingDto)){
             //查找老数据
            try {
                HubSlotSpuDto slotSpuDto = this.findHubSlotSpu(originSpuPendingDto.getHubBrandNo(),originSpuPendingDto.getSpuModel());

                if(null!=slotSpuDto){
                    //查看是否有多个供货商 如果单个 直接修改老数据的SLOTSPU  如果多个 插入新的SLOTSPU
                    //并把老的SLOTSPUSUPPLIER 逻辑删除  ，并插入新的记录
                    List<HubSlotSpuSupplierDto> slotSpuSupplierDtos = hubSlotSpuSupplierService.findSlotSpuSupplierListBySlotSpuId(slotSpuDto.getSlotSpuId());
                    if(null==slotSpuSupplierDtos||0==slotSpuSupplierDtos.size()){

                        HubSlotSpuSupplierDto slotSpuSupplierDto = new HubSlotSpuSupplierDto();
                        this.transPendingToSlotSupplier(pendingProductDto,slotSpuSupplierDto,slotSpuDto.getSlotSpuId(),slotSpuDto.getSlotSpuNo());
                        hubSlotSpuSupplierService.addHubSloSpuSupplier(slotSpuSupplierDto);

                    }else{
                       if(slotSpuSupplierDtos.size()==1){//单个
                           this.updateSpuModelAndBrandNo(slotSpuSupplierDtos.get(0).getSlotSpuId(),pendingProductDto);
                       }else{
                           for(HubSlotSpuSupplierDto dto:slotSpuSupplierDtos){
                               if(dto.getSpuPendingId()==pendingProductDto.getSpuPendingId()){
                                   //跟新老的记录为逻辑删除
                                   hubSlotSpuSupplierService.deleteSlotSpuSupplierForLogic(dto.getSlotSpuSupplierId());
                                   //插入新记录
                                   this.addSlotSpuAndSupplier(pendingProductDto);
                               }
                           }

                       }
                    }
                }else{
                    //插入新记录
                    this.addSlotSpuAndSupplier(pendingProductDto);
                }
            } catch (Exception e) {

                e.printStackTrace();
                log.error("updateSlotSpu error.reason :" + e.getMessage(),e);
            }
            return true;


        }else{
            return true;
        }





    }

    @Override
    public boolean updateSpuModelAndBrandNo(Long slotSpuId,PendingProductDto pendingProductDto) throws Exception {

        HubSlotSpuDto slotSpuDto = new HubSlotSpuDto();
        slotSpuDto.setSlotSpuId(slotSpuId);
        slotSpuDto.setSpuModel(pendingProductDto.getSpuModel());
        slotSpuDto.setBrandNo(pendingProductDto.getHubBrandNo());
        slotSpuGateWay.updateByPrimaryKeySelective(slotSpuDto);
        return true;
    }


    private void transPendingToSlot(PendingProductDto resource,HubSlotSpuDto target){

        target.setSpuModel(resource.getSpuModel());
        target.setBrandNo(resource.getHubBrandNo());
        target.setCategoryNo(resource.getHubCategoryNo());

        target.setSecondCategoryNo(null!=resource.getHubCategoryNo()&&resource.getHubCategoryNo().indexOf("B")>0?resource.getHubCategoryNo().substring(0,6):resource.getHubCategoryNo());
        String hubSeason = resource.getHubSeason();
        if(StringUtils.isNotBlank(hubSeason)){
           if(hubSeason.indexOf("_")>0){
               target.setMarketTime(hubSeason.substring(0,hubSeason.indexOf("_")));
               target.setSeason(hubSeason.substring(hubSeason.indexOf("_")+1,hubSeason.length()));
               //TODO 英文季节
           }
        }
        target.setCreateTime(new Date());
        target.setCreateUser(target.getUpdateUser());
//        spuNoTypeDto.setType("slotspu");//常量在 中定义 只有此处用到 ，直接写死了
//        target.setSlotSpuNo(spuNoGateWay.getSpuNo(spuNoTypeDto));




    }

    private void transPendingToSlotSupplier(PendingProductDto resource,HubSlotSpuSupplierDto target,Long slotSpuId,String slotSpuNo){
        target.setSlotSpuId(slotSpuId);
        target.setSlotSpuNo(slotSpuNo);
        target.setSupplierId(resource.getSupplierId());
        target.setSupplierNo(resource.getSupplierNo());
        target.setSpuPendingId(resource.getSpuPendingId());
        target.setSupplierSpuId(resource.getSupplierSpuId());
        target.setState(SlotSpuState.WAIT_SEND.getIndex().byteValue());
        target.setDataState(DataState.NOT_DELETED.getIndex());
        target.setCreateTime(new Date());
        target.setCreateUser(resource.getCreateUser());

    }


    private void handleInsertSlotSpuException(PendingProductDto pendingProductDto,HubSlotSpuDto slotSpuDto){
        HubSlotSpuDto originSlotSpu = this.findHubSlotSpu(pendingProductDto.getHubBrandNo(),pendingProductDto.getSpuModel());
        BeanUtils.copyProperties(originSlotSpu,slotSpuDto);

    }

    /**
     * 当修改了待处理信息  是否需要处理SlotSpu
     * @param pendingProductDto
     * @param spuPendingDto
     * @return
     */
    private boolean isNeedHandleSlotSpu(PendingProductDto pendingProductDto,HubSpuPendingDto spuPendingDto){
        if(null==spuPendingDto.getSlotState()){
            return  false;
        }else{
            if(SpuPendingStudioState.SEND.getIndex()==spuPendingDto.getSlotState().intValue()){
                return false;
            }else if(SpuPendingStudioState.WAIT_HANDLED.getIndex()==spuPendingDto.getSlotState().intValue()) {
                return  false;
            }else{
                if(pendingProductDto.getHubBrandNo()!=spuPendingDto.getHubBrandNo()||
                        pendingProductDto.getSpuModel()!=spuPendingDto.getSpuModel()) {
                    return true;
                }else{
                    return false;
                }
            }
        }
    }
}
