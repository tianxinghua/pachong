package com.shangpin.ephub.product.business.service.pic;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhongren on 2018/3/10.
 */
@Component
public class PicHandleService {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    HubSpuPendingPicGateWay spuPendingPicGateWay;

    @Autowired
    HubSpuPicGateWay  spuPicGateWay;

    /**
     *
     * @param spuNo
     * @param supplierId
     * @return
     */
     public List<HubSpuPendingPicDto> getSupplierPicByHubSpuNoAndSupplierId(String spuNo,String supplierId){


         List<HubSpuPendingPicDto> picDtoList = null;
         Long supplierSpuId = 0L;
         //获取hubSupplierSpuId
         HubSpuPendingCriteriaDto criterial = new HubSpuPendingCriteriaDto();

         criterial.createCriteria().andSupplierIdEqualTo(supplierId).andHubSpuNoEqualTo(spuNo);
         List<HubSpuPendingDto> spuPendingDtoList = spuPendingGateWay.selectByCriteria(criterial);
         if(null!=spuPendingDtoList&&spuPendingDtoList.size()>0){
             supplierSpuId = spuPendingDtoList.get(0).getSupplierSpuId();
         }else{
             return new ArrayList<HubSpuPendingPicDto>();
         }
         //获取图片
         HubSpuPendingPicCriteriaDto picCriterial = new HubSpuPendingPicCriteriaDto();
         picCriterial.setPageSize(10);
         picCriterial.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
         return  spuPendingPicGateWay.selectByCriteria(picCriterial);


     }


    public  void createSpuPic(List<HubSpuPendingPicDto> picVOs, Long spuId){
         if(null!=picVOs&&picVOs.size()>0){

             //删除老的
             HubSpuPicCriteriaDto criteria = new HubSpuPicCriteriaDto();
             criteria.createCriteria().andSpuIdEqualTo(spuId);
             spuPicGateWay.deleteByCriteria(criteria);

             //增加新的
             Date date = new Date();
             for(HubSpuPendingPicDto spuPendingPic:picVOs){
                 HubSpuPicDto hubSpuPic= new HubSpuPicDto();
                 hubSpuPic.setCreateTime(date);
                 hubSpuPic.setUpdateTime(date);
                 hubSpuPic.setSpPicUrl(spuPendingPic.getSpPicUrl());
                 hubSpuPic.setSpuId(spuId);
                 hubSpuPic.setDataState(DataState.NOT_DELETED.getIndex());
                 hubSpuPic.setPicId(spuPendingPic.getSpuPendingPicId());
                 spuPicGateWay.insert(hubSpuPic);

             }
         }



    }

}
