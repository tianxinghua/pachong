package com.shangpin.ephub.product.business.service.studio.hubslot.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.SlotSpuState;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuNoTypeDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.SpuNoGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.gateway.HubSlotSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    SpuNoGateWay spuNoGateWay;

    @Autowired
    HubSlotSpuSupplierService hubSlotSpuSupplierService;


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
            this.transPendingToSlot(pendingProductDto,slotSpuDto);

            if(null==this.findHubSlotSpu(pendingProductDto.getHubBrandNo(),pendingProductDto.getSpuModel())){
                slotSpuGateWay.insert(slotSpuDto);
            }





            this.transPendingToSlotSupplier(pendingProductDto,slotSpuSupplierDto,slotSpuDto.getSlotSpuId(),slotSpuDto.getSlotSpuNo());

            hubSlotSpuSupplierService.addHubSloSpuSupplier(slotSpuSupplierDto);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("inser slotspu error.");
        }
        return false;
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
        spuNoTypeDto.setType("slotspu");//常量在 中定义 只有此处用到 ，直接写死了
        target.setSlotSpuNo(spuNoGateWay.getSpuNo(spuNoTypeDto));




    }

    private void transPendingToSlotSupplier(PendingProductDto resource,HubSlotSpuSupplierDto target,Long slotSpuId,String slotSpuNo){
        target.setSlotSpuId(slotSpuId);
        target.setSupplierId(resource.getSupplierId());
        target.setSupplierNo(resource.getSupplierNo());
        target.setSpuPendingId(resource.getSpuPendingId());
        target.setSupplierSpuId(resource.getSupplierSpuId());
        target.setState(SlotSpuState.WAIT_SEND.getIndex().byteValue());
        target.setDataState(DataState.NOT_DELETED.getIndex());
        target.setCreateTime(new Date());
        target.setCreateUser(resource.getCreateUser());

    }
}
