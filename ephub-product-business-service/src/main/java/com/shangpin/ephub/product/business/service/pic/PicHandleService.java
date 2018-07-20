package com.shangpin.ephub.product.business.service.pic;

import com.shangpin.ephub.client.consumer.picture.dto.RetryPictureDto;
import com.shangpin.ephub.client.consumer.picture.gateway.PictureGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.picture.dto.*;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhongren on 2018/3/10.
 */
@Component
@Slf4j
public class PicHandleService {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    HubSpuPendingPicGateWay spuPendingPicGateWay;

    @Autowired
    HubSpuPicGateWay  spuPicGateWay;

    @Autowired
    private PictureGateWay pictureGateWay;

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


    public boolean retryPictures(Long supplierSpuId) {
        try {
            List<HubSpuPendingPicDto> lists = findPendingPics(supplierSpuId);
            if(CollectionUtils.isNotEmpty(lists)){
                List<Long> ids = new ArrayList<Long>();
                for(HubSpuPendingPicDto dto : lists){
                    ids.add(dto.getSpuPendingPicId());
                }
                updatePicHandleState(ids,PicHandleState.HANDLE_ERROR);
                RetryPictureDto pictureDto = new RetryPictureDto();
                pictureDto.setIds(ids);
                pictureGateWay.retry(pictureDto);
                return true;
            }
        } catch (Exception e) {
            log.error("重新拉去图片出错："+e.getMessage(),e);
        }
        return false;
    }

    private   List<HubSpuPendingPicDto> findPendingPics(Long supplierSpuId){
        HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
        criteria.setFields("spu_pending_pic_id");
        criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
        return  spuPendingPicGateWay.selectByCriteria(criteria);

    }



    private  void updatePicHandleState(List<Long> spuPendingPicIds, PicHandleState picHandleState) {
        HubSpuPendingPicWithCriteriaDto criteriaDto = new  HubSpuPendingPicWithCriteriaDto();
        HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
        criteria.createCriteria().andSpuPendingPicIdIn(spuPendingPicIds);
        criteriaDto.setCriteria(criteria);
        HubSpuPendingPicDto hubSpuPendingPic = new HubSpuPendingPicDto();
        hubSpuPendingPic.setPicHandleState(picHandleState.getIndex());
        criteriaDto.setHubSpuPendingPic(hubSpuPendingPic);
        spuPendingPicGateWay.updateByCriteriaSelective(criteriaDto);
    }

    public List<HubSpuPendingPicDto>  getProductAvailablePic(Long supplierSpuId){
        HubSpuPendingPicCriteriaDto criteriaPic = new HubSpuPendingPicCriteriaDto();
        criteriaPic.setPageNo(1);
        criteriaPic.setPageSize(100);
        criteriaPic.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId)
                .andPicHandleStateEqualTo(PicState.HANDLED.getIndex()).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
        return  spuPendingPicGateWay.selectByCriteria(criteriaPic);
    }

}
