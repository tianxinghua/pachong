package com.shangpin.ephub.price.consumer.service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierPriceChangeRecordGateWay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by lizhongren on 2017/3/31.
 */
@Service
@Slf4j
public class PriceChangeRecordDataService {
   @Autowired
   HubSupplierPriceChangeRecordGateWay hubSupplierPriceChangeRecordGateWay;

   /**
    * 更新推送状态状态
    * @param supplierId
    * @param spSkuNos  ：尚品的SKUNO列表
    * @param state
    * @param memo
    * @return
    */
   public boolean updatePriceSendState(String supplierId, List<String> spSkuNos, Byte state,String memo) throws Exception{
      boolean result = false;
      HubSupplierPriceChangeRecordDto hubSupplierPriceChangeRecordDto = new HubSupplierPriceChangeRecordDto();
      hubSupplierPriceChangeRecordDto.setState(state);
      hubSupplierPriceChangeRecordDto.setMemo(memo);
      hubSupplierPriceChangeRecordDto.setUpdateTime(new Date());
      HubSupplierPriceChangeRecordCriteriaDto criteriaDto  = new HubSupplierPriceChangeRecordCriteriaDto();
      criteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andSpSkuNoIn(spSkuNos);
      HubSupplierPriceChangeRecordWithCriteriaDto withCriteriaDto = new HubSupplierPriceChangeRecordWithCriteriaDto(hubSupplierPriceChangeRecordDto,criteriaDto);
      int i = hubSupplierPriceChangeRecordGateWay.updateByCriteriaSelective(withCriteriaDto);
      result = true;
      return result;
   }


   /**
    *  更新推送状态状态
    * @param supplierId
    * @param spSkuNo  ：单个尚品SKUNO
    * @param state
    * @param memo
    * @return
    * @throws Exception
    */
   public boolean updatePriceSendState(String supplierId, String spSkuNo, Byte state,String memo) throws Exception{
      boolean result = false;
      HubSupplierPriceChangeRecordDto hubSupplierPriceChangeRecordDto = new HubSupplierPriceChangeRecordDto();
      hubSupplierPriceChangeRecordDto.setState(state);
      hubSupplierPriceChangeRecordDto.setMemo(memo);
      hubSupplierPriceChangeRecordDto.setUpdateTime(new Date());
      HubSupplierPriceChangeRecordCriteriaDto criteriaDto  = new HubSupplierPriceChangeRecordCriteriaDto();
      criteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andSpSkuNoEqualTo(spSkuNo);
      HubSupplierPriceChangeRecordWithCriteriaDto withCriteriaDto = new HubSupplierPriceChangeRecordWithCriteriaDto(hubSupplierPriceChangeRecordDto,criteriaDto);
      int i = hubSupplierPriceChangeRecordGateWay.updateByCriteriaSelective(withCriteriaDto);
      result = true;
      return result;
   }
}
