package com.shangpin.ephub.price.consumer.service;

import com.shangpin.commons.redis.IShangpinRedis;

import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;

import com.shangpin.iog.ice.dto.SupplierMessageDTO;
import com.shangpin.iog.ice.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2017/3/30.
 */
@Service
@Slf4j

public class SupplierPriceService {



    @Autowired
    PriceSendService priceSendService;

    @Autowired
    SupplierService openapiSupplier;

    @Autowired
    PriceChangeRecordDataService  priceChangeRecordDataService;

    @Autowired
    private IShangpinRedis shangpinRedis;


    public Boolean  sendPriceMessageToScm(ProductPriceDTO productPriceDTO,Map<String, Object> headers ) throws Exception {

        if(null==productPriceDTO) return true;
        //获取供货商信息

        List<String> spSkus  = new ArrayList<>();
        spSkus.add(productPriceDTO.getSkuNo());
        try {
            String supplierType = this.getSupplierPriceType(productPriceDTO.getSopUserNo());
            log.info("supplier type ="+ supplierType);
            if("PurchasePrice".equals(supplierType)){       //供货架
                handSupplyPrice(productPriceDTO);
            }else if("3".equals(supplierType)){ // 市场价
                handleMarketPrice(productPriceDTO);
            }else if("MarketDiscount".equals(supplierType)){ // 市场价
                handleMarketPrice(productPriceDTO);
            }else{
                //无类型

                priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSopUserNo(),spSkus, PriceHandleState.PUSHED_ERROR.getIndex(),"无供货商信息");
                return false;
            }


            return true;
        } catch (Exception e) {

            String season = "推送SCM时，发生异常错误.reason : "+e.getMessage();
            if(season.length()>1500){
                season = season.substring(0,1500);
            }
            priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSopUserNo(),spSkus, PriceHandleState.PUSHED_ERROR.getIndex(),season);
            throw e;

        }


    }

    private void handSupplyPrice(ProductPriceDTO productPriceDTO) throws Exception {
        if(priceSendService.sendSupplyPriceMsgToScm(productPriceDTO)){
            priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                    productPriceDTO.getSkuNo(), PriceHandleState.PUSHED_OPENAPI_SUCCESS.getIndex(),""  );
        }else{
            priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                    productPriceDTO.getSkuNo(),PriceHandleState.PUSHED_OPENAPI_ERROR
                            .getIndex(),"供价制调用OPENAPI 返回失败"  );
        }
    }

    private void handleMarketPrice(ProductPriceDTO productPriceDTO) throws Exception {
        if(priceSendService.sendMarketPriceMsgToScm(productPriceDTO)){
            priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                    productPriceDTO.getSkuNo(), PriceHandleState.PUSHED_OPENAPI_SUCCESS.getIndex(),""  );
        }else{
            priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                    productPriceDTO.getSkuNo(),PriceHandleState.PUSHED_OPENAPI_ERROR
                            .getIndex(),"市场制调用OPENAPI 返回失败"  );
        }
    }

    private String getSupplierPriceType(String suppplierId) throws Exception{

        SupplierMessageDTO supplieDTO = openapiSupplier.getSupplierMessage(suppplierId);

        if(null!=supplieDTO){
            return  supplieDTO.getQuoteMode();
        }else{
            return "";
        }
    }
}
