package com.shangpin.ephub.product.business.service.studio.hubslotpic.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.gateway.HubSlotSpuPicGateway;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.gateway.HubSlotSpuGateWay;
import com.shangpin.ephub.product.business.service.studio.hubslotpic.SlotPicService;
import com.shangpin.ephub.product.business.service.studio.hubslotpic.dto.Pic;
import com.shangpin.ephub.product.business.service.studio.hubslotpic.dto.SlotSpuPic;
import com.shangpin.ephub.product.business.service.studio.hubslotpic.dto.SupplierPic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 17/7/11.
 */
@Service
@Slf4j
public class SlotPicServiceImpl implements SlotPicService {

    @Autowired
    HubSlotSpuGateWay  slotSpuGateWay;


    @Autowired
    HubSlotSpuPicGateway picGateway;


    @Override
    public SlotSpuPic findSpuPicByBrandNoAndSpuModel(String brandNo, String spuModel) {

        HubSlotSpuCriteriaDto criteria = new HubSlotSpuCriteriaDto();
        criteria.createCriteria().andBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel);
        List<HubSlotSpuDto> slotSpuDtos = slotSpuGateWay.selectByCriteria(criteria);
        if(null!=slotSpuDtos&&slotSpuDtos.size()>0){
            HubSlotSpuDto slotSpuDto = slotSpuDtos.get(0);
            if(slotSpuDto.getPicSign()== PicState.HANDLED.getIndex()){//有图片
                SlotSpuPic slotSpuPic= new SlotSpuPic();
                slotSpuPic.setBrandNo(brandNo);
                slotSpuPic.setSpuModel(spuModel);
                List<SupplierPic> supplierPics = new ArrayList<>();
                slotSpuPic.setSupplierPic(supplierPics);
                 //获取具体图片信息
                HubSlotSpuPicCriteriaDto picCriteria = new HubSlotSpuPicCriteriaDto();
                picCriteria.createCriteria().andSlotSpuIdEqualTo(slotSpuDto.getSlotSpuId());
                List<HubSlotSpuPicDto> hubSlotSpuPicDtos = picGateway.selectByCriteria(picCriteria);
                Map<String,SupplierPic>  suppliericMap = new HashMap<>();
                for(HubSlotSpuPicDto slotSpuPicDto:hubSlotSpuPicDtos){
                    if(suppliericMap.containsKey(slotSpuPicDto.getSupplierId())){
                        Pic pic = new Pic();
                        pic.setSpPicUrl(slotSpuPicDto.getSpPicUrl());
                        pic.setIndex(null==slotSpuPicDto.getSortValue()?0:slotSpuPicDto.getSortValue());
                        suppliericMap.get(slotSpuPicDto.getSupplierId()).getSupplierPic().add(pic);
                    }else{
                        SupplierPic  supplierPic = new SupplierPic();
                        supplierPic.setSupplierId(slotSpuPicDto.getSupplierId());
                        List<Pic> pics = new ArrayList<>();
                        supplierPic.setSupplierPic(pics);
                        suppliericMap.put(slotSpuPicDto.getSupplierId(),supplierPic);
                        supplierPics.add(supplierPic);
                    }

                }
                return  slotSpuPic;

            }
        }

        return null;
    }
}
