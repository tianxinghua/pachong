package com.shangpin.ephub.data.schedule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import com.shangpin.ephub.client.consumer.price.gateway.PriceMqGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/5/23.
 */
@Service
@Slf4j
public class PricePushService {
    @Autowired
    PricePushDataService pricePushDataService;
    @Autowired
    PriceMqGateWay priceMqGateWay;

    public void handleErrorPush() throws Exception{
        List<HubSupplierPriceChangeRecordDto> pushMqErrorRecordList = pricePushDataService.findPushMqErrorRecordList();
        List<HubSupplierPriceChangeRecordDto> needHandleRecords = pricePushDataService.findNeedHandleRecord(pushMqErrorRecordList);
        for(HubSupplierPriceChangeRecordDto tryDao:needHandleRecords){
            ProductPriceDTO productPriceDTO = new ProductPriceDTO();
            this.transObject(tryDao,productPriceDTO);
            log.info("重推价格消息体："+ JsonUtil.serialize(productPriceDTO));
            priceMqGateWay.transPrice(productPriceDTO);
            log.info(productPriceDTO.getSopUserNo()+" "+productPriceDTO.getSupplierSkuNo()+" 发送队列成功。");
        }
    }

    private void  transObject(HubSupplierPriceChangeRecordDto sourceObj,ProductPriceDTO targetObj){

        targetObj.setSupplierPriceChangeRecordId(sourceObj.getSupplierPriceChangeRecordId());
        targetObj.setMarketPrice(sourceObj.getMarketPrice().toString());
        targetObj.setPurchasePrice(sourceObj.getSupplyPrice().toString());
        targetObj.setMarketSeason(sourceObj.getMarketSeason());
        targetObj.setMarketYear(sourceObj.getMarketYear());
        targetObj.setSkuNo(sourceObj.getSpSkuNo());
        targetObj.setSupplierSkuNo(sourceObj.getSupplierSkuNo());
        targetObj.setSopUserNo(sourceObj.getSupplierId());
        targetObj.setSupplierNo(sourceObj.getSupplierNo()); 
        targetObj.setPriceHandleType(PriceHandleType.NEW_DEFAULT.getIndex());
    }
}
