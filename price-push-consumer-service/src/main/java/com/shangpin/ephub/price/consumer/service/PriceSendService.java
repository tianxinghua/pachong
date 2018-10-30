package com.shangpin.ephub.price.consumer.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.price.consumer.service.dto.PriceParamDTO;
import com.shangpin.ephub.price.consumer.service.dto.ResMessage;
import com.shangpin.ephub.price.consumer.util.HttpClientUtil;
import com.shangpin.iog.ice.service.PriceHandleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import com.shangpin.ephub.price.consumer.conf.stream.source.sender.RetryPriceStreamSender;
import com.shangpin.ephub.price.consumer.service.dto.PriceHandleMessageDTO;


import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/3/30.
 */
@Service
@Slf4j

public class PriceSendService {
    @Autowired
    PriceHandleService openapiPriceHandleService;

    @Autowired
    RetryPriceStreamSender retryPriceStreamSender;

    @Autowired
    HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

    @Autowired
    HubSupplierSkuGateWay hubSupplierSkuGateWay;

    @Autowired
    HubSupplierSpuGateWay hubSupplierSpuGateWay;
    @Autowired
    HttpClientUtil httpClientUtil;

    ObjectMapper om = new ObjectMapper();

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

            String supplierId=productPriceDTO.getSopUserNo();
            String skuNo=productPriceDTO.getSupplierSkuNo();
            PriceParamDTO priceparam = copyValue(productPriceDTO);
            List<PriceParamDTO> productDTOList = new ArrayList<>();

            //获取sku----拿市场价跟特价
            HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
            criteriaDto.createCriteria().andSupplierSkuNoEqualTo(skuNo);
            List<HubSupplierSkuDto> skulist = hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
            HubSupplierSkuDto sku = null;
            if(skulist.size()>0){
                 sku = skulist.get(0);
                priceparam.setMarketPrice(sku.getMarketPrice());
                BigDecimal saleprice = sku.getSalesPrice();
                priceparam.setSpecialMarketPrice(saleprice==null?"0":saleprice.toString());
            }else{
                log.info("供应商："+supplierId+"skuNo:"+skuNo+"不存在！");
            }
            if(getSupplierMapping(supplierId)!=null){//爬虫
            //如果是爬虫，市场价跟特价在spu里面拿
                HubSupplierSpuDto spu= hubSupplierSpuGateWay.selectByPrimaryKey(sku.getSupplierSpuId());
                if(spu!=null){
                    priceparam.setMarketPrice(spu.getMarketPrice());
                    BigDecimal saleprice = spu.getSalePrice();
                    priceparam.setSpecialMarketPrice(saleprice==null?"0":saleprice.toString());
                }else{
                    log.info("供应商："+supplierId+"spuId:"+sku.getSupplierSpuId()+"不存在！");
                }

            }
            productDTOList.add(priceparam);
            om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            content = om.writeValueAsString(productDTOList);

            sendResult =   httpClientUtil.sendRequest(content);
            if(sendResult!=null){
                ResMessage r = JSONObject.parseObject(sendResult,ResMessage.class);
                if(!r.IsSuccess){
                    log.info("Product/ModifyProductMarketPrice failed! content:"+content+",msg:"+r.MessageRes);
                    retryPriceStreamSender.supplierPictureProductStream(productPriceDTO,null);
                }else{
                    log.info("Product/ModifyProductMarketPrice sucess! content:"+content);
                }
            }

          /*  List<ProductPriceDTO> productDTOList = new ArrayList<>();
            om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
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

            }*/
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
    public boolean sendSupplyPriceMsgToScm(ProductPriceDTO productPriceDTO,String originPurchasePrice,String originMarketPrice) throws  Exception{

        String  content ="";
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        List<ProductPriceDTO> productDTOList = new ArrayList<>();
        try {
            long start = System.currentTimeMillis();
            productPriceDTO.setMemo(productPriceDTO.getSupplierPriceChangeRecordId().toString());
            productPriceDTO.setCreateUserName("openAPI");
            productDTOList.add(productPriceDTO);

            content = om.writeValueAsString(productDTOList);
            //返回更新失败的sku map
            String result  = openapiPriceHandleService.pushPurchasePriceMessage(productPriceDTO.getSopUserNo(),content);
            log.info("call api result of supply price = " + result);
            //失败的推送到消息队列
            if(StringUtils.isNotBlank(result)){
                productPriceDTO.setPurchasePrice(originPurchasePrice);
                productPriceDTO.setMarketPrice(originMarketPrice);
                retryPriceStreamSender.supplierPictureProductStream(productPriceDTO,null);
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


    private  HubSupplierValueMappingDto getSupplierMapping(String supplierId){

        HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
        criteria.createCriteria().andSupplierIdEqualTo(supplierId).andHubValTypeEqualTo(new Integer(6).byteValue()).andSupplierValEqualTo("1");
        List<HubSupplierValueMappingDto> list = hubSupplierValueMappingGateWay.selectByCriteria(criteria);
        if(list.size()>0){
            HubSupplierValueMappingDto tmp2 = list.get(0);
            return tmp2;
        }else{
            return null;
        }
    }

    private PriceParamDTO copyValue(ProductPriceDTO productPriceDTO){
        PriceParamDTO priceParamDTO = new PriceParamDTO();
        priceParamDTO.setSopUserNo(Long.parseLong(productPriceDTO.getSopUserNo()));
        priceParamDTO.setSkuNo(productPriceDTO.getSkuNo());
        priceParamDTO.setCurrency(productPriceDTO.getCurrency());
        priceParamDTO.setMarketSeason(productPriceDTO.getMarketSeason());
        priceParamDTO.setMemo(productPriceDTO.getMemo());
        priceParamDTO.setMarketYear(productPriceDTO.getMarketYear());
        return priceParamDTO;
    }
}
