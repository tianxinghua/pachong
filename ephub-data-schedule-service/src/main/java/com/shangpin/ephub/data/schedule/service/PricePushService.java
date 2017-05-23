package com.shangpin.ephub.data.schedule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import com.shangpin.ephub.client.consumer.price.gateway.PriceMqGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    ObjectMapper mapper = new ObjectMapper();

    public void handleErrorPush(){
        List<HubSupplierPriceChangeRecordDto> pushMqErrorRecordList = pricePushDataService.findPushMqErrorRecordList();
        List<HubSupplierPriceChangeRecordDto> needHandleRecords = pricePushDataService.findNeedHandleRecord(pushMqErrorRecordList);
        for(HubSupplierPriceChangeRecordDto tryDao:needHandleRecords){
            ProductPriceDTO productPriceDTO = new ProductPriceDTO();
            this.transObject(tryDao,productPriceDTO);
            try {
                log.info("retry send mq:"+ mapper.writeValueAsString(productPriceDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            priceMqGateWay.transPrice(productPriceDTO);
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
    }
}
