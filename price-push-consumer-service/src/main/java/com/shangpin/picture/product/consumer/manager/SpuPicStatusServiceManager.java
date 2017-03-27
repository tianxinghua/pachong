package com.shangpin.picture.product.consumer.manager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.picture.product.consumer.e.PicHandleState;

/**
 * Created by lizhongren on 2017/1/15.
 *  更新SPUPENDING 中 图片状态
 */
@Component
public class SpuPicStatusServiceManager {
    
	@Autowired
    private HubSpuPendingGateWay spuPendingGateWay;
	@Autowired
	private SupplierProductPictureManager supplierProductPictureManager;

     /**
      * 更新hub_spu_pending表的pic_state
      * @param supplierSpuId 供应商原始spu表id
      * @param picStatus 图片状态
      */
     private void updatePicStatus(Long supplierSpuId,Byte picStatus){
         HubSpuPendingCriteriaDto criteria= new HubSpuPendingCriteriaDto();
         criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
         HubSpuPendingDto spuPending = new HubSpuPendingDto();
         spuPending.setPicState(picStatus);
         HubSpuPendingWithCriteriaDto criteriaDto = new HubSpuPendingWithCriteriaDto(spuPending,criteria);
         spuPendingGateWay.updateByCriteriaSelective(criteriaDto);
     }
     /**
      * 根据supplierSpuId判断并设置整个spu图片状态。
      * @param supplierSpuId
      */
     public void judgeSpuPicState(Long supplierSpuId){
    	 List<HubSpuPendingPicDto> lists = supplierProductPictureManager.selectHubSpuPendingPic(supplierSpuId);
    	 if(CollectionUtils.isNotEmpty(lists)){
    		 boolean flag = false;
    		 for(HubSpuPendingPicDto dto : lists){
    			 if(null != dto.getPicHandleState() && PicHandleState.HANDLED.getIndex() == dto.getPicHandleState()){
    				 flag = true;
    				 break;
    			 }
    		 }
    		 if(flag){
    			 updatePicStatus(supplierSpuId,PicState.HANDLED.getIndex());
    		 }else{
    			 updatePicStatus(supplierSpuId,PicState.HANDLE_ERROR.getIndex());
    		 }
    	 }else{
    		 updatePicStatus(supplierSpuId,PicState.UNHANDLED.getIndex());
    	 }
     }
}
