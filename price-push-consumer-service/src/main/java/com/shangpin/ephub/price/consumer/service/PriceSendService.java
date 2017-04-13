package com.shangpin.ephub.price.consumer.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import com.shangpin.ephub.price.consumer.conf.stream.source.sender.RetryPriceStreamSender;
import com.shangpin.ephub.price.consumer.service.dto.PriceHandleMessageDTO;
import com.shangpin.iog.ice.service.PirceHandleService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/3/30.
 */
@Service
@Slf4j

public class PriceSendService {
    @Autowired
    PirceHandleService openapiPriceHandleService;

    @Autowired
    RetryPriceStreamSender retryPriceStreamSender;

    /**
     * 推送市场价  若失败推送到重试队列
     * @param productPriceDTO
     * @return
     */
    public boolean sendMarketPriceMsgToScm(ProductPriceDTO productPriceDTO) throws Exception{
        boolean result=false;
        String   sendResult ="";
        String content ="";
        try {
            long start = System.currentTimeMillis();
            ObjectMapper om = new ObjectMapper();
            om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            List<ProductPriceDTO> productDTOList = new ArrayList<>();
            productPriceDTO.setMemo(productPriceDTO.getSupplierPriceChangeRecordId().toString());
            productPriceDTO.setCurrency("-1");
            productPriceDTO.setCreateUserName("openAPI");
            productDTOList.add(productPriceDTO);
            content = om.writeValueAsString(productDTOList);


            sendResult=openapiPriceHandleService.pushMarketPriceMessage(productPriceDTO.getSopUserNo(),content);
            log.info("call api result of marker price = " +sendResult);
            if(sendResult.startsWith("error")){
                log.error("send price message error: message boday= "+ content  );
                for(ProductPriceDTO failPriceDto: productDTOList) {

                    retryPriceStreamSender.supplierPictureProductStream(failPriceDto,null);

                }


            }
            long end = System.currentTimeMillis();
            log.info("Successfully handled of market price  message = "+ content +"  , and spend time : "+(end-start)+" milliseconds");
            result =true;
        } catch (Exception e) {

            log.error("send price message error: message= "+ content+" reason : " + e.getMessage(),e);
            throw e;
        }
        return result;
    }

    /**
     * 推送供价  若失败推送到重试队列
     * @param productPriceDTO
     * @return
     */
    public boolean sendSupplyPriceMsgToScm(ProductPriceDTO productPriceDTO) throws  Exception{

        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Map<String,String> purchasePriceMap= new HashMap<>();
        Map<String,ProductPriceDTO>  allProductMap  = new HashMap<>();
      //  PriceHandleMessageDTO  dto = new PriceHandleMessageDTO();
        String  content ="";
        List<String> successSkuList = new ArrayList<>();
        try {
            long start = System.currentTimeMillis();
            productPriceDTO.setMemo(productPriceDTO.getSupplierPriceChangeRecordId().toString());
            productPriceDTO.setCurrency("-1");
            productPriceDTO.setCreateUserName("openAPI");
            content = om.writeValueAsString(productPriceDTO);
            purchasePriceMap.put(productPriceDTO.getSkuNo(),productPriceDTO.getPurchasePrice());

            allProductMap.put(productPriceDTO.getSkuNo(),productPriceDTO);

            Map<String,String> map = null;
            //返回更新失败的sku map
            map = openapiPriceHandleService.pushPurchasePriceMessage(productPriceDTO.getSopUserNo(),purchasePriceMap);
            log.info("call api result of supply price = " + map.toString());
            //获取失败的数据  继续推送 消息队列  成功的 更新状态
            List<ProductPriceDTO> failList = new ArrayList<>();

            Set<String> skuSet = allProductMap.keySet();
            for(String sku:skuSet){
                if(map.containsKey(sku)){
                    failList.add(allProductMap.get(sku));
                }else{
                    successSkuList.add(sku);
                }
            }
            //失败的推送到消息队列
            if(failList.size()>0){
                for(ProductPriceDTO failPriceDto:failList){
                    retryPriceStreamSender.supplierPictureProductStream(failPriceDto,null);
                }
                return false;
            }

            long end = System.currentTimeMillis();
            log.info("Successfully handled of supply price  message 【 "+content+" 】 , and spend time : "+(end-start)+" milliseconds");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("send suply price message error: message= "+ content+" reason : " + e.getMessage(),e);
            throw e;
        }
        return  true;

    }

}
