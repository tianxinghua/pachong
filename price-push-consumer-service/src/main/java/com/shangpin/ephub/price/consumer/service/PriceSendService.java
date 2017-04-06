package com.shangpin.ephub.price.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import com.shangpin.ephub.price.consumer.conf.stream.source.sender.RetryPriceStreamSender;
import com.shangpin.ephub.price.consumer.service.dto.PriceHandleMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import com.shangpin.iog.ice.service.*;

import java.text.SimpleDateFormat;
import java.util.*;

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
        boolean  sendResult =false;
        String content ="";
        try {
            long start = System.currentTimeMillis();
            ObjectMapper om = new ObjectMapper();
            om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            List<ProductPriceDTO> productDTOList = new ArrayList<>();
            productDTOList.add(productPriceDTO);
            content = om.writeValueAsString(productDTOList);


            sendResult=openapiPriceHandleService.pushMarketPriceMessage(productPriceDTO.getSopUserNo(),content);
            if(!sendResult){
                log.error("send price message error: message boday= "+ content  );
                for(ProductPriceDTO failPriceDto: productDTOList) {

                    retryPriceStreamSender.supplierPictureProductStream(failPriceDto,null);

                }

                return sendResult;
            }
            long end = System.currentTimeMillis();
            log.info("Successfully handling of message = "+ content +"  , and spend time : "+(end-start)+" milliseconds");
        } catch (Exception e) {

            log.error("send price message error: message= "+ content+" reason : " + e.getMessage(),e);
            throw e;
        }
        return sendResult;
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
        PriceHandleMessageDTO  dto = new PriceHandleMessageDTO();
        String  content ="";
        List<String> successSkuList = new ArrayList<>();
        try {
            long start = System.currentTimeMillis();
            content = om.writeValueAsString(productPriceDTO);
            purchasePriceMap.put(productPriceDTO.getSkuNo(),productPriceDTO.getPurchasePrice());

            allProductMap.put(productPriceDTO.getSkuNo(),productPriceDTO);

            Map<String,String> map = null;
            //返回更新失败的sku map
            map = openapiPriceHandleService.pushPurchasePriceMessage(productPriceDTO.getSopUserNo(),purchasePriceMap);

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
            log.info("Successfully handling of message 【 "+content+" 】 , and spend time : "+(end-start)+" milliseconds");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("send suply price message error: message= "+ content+" reason : " + e.getMessage(),e);
            throw e;
        }
        return  true;

    }

}
