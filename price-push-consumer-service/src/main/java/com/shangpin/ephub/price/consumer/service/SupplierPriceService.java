package com.shangpin.ephub.price.consumer.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.product.business.gms.dto.SupplierDTO;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import com.shangpin.iog.ice.dto.SupplierMessageDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/3/30.
 */
@Service
@Slf4j

public class SupplierPriceService {




    @Autowired
    PriceSendService priceSendService;



    @Autowired
    PriceChangeRecordDataService  priceChangeRecordDataService;

    @Autowired
    HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

    @Autowired
    SupplierService supplierService;

    ObjectMapper mapper = new ObjectMapper();


    public Boolean  sendPriceMessageToScm(ProductPriceDTO productPriceDTO,Map<String, Object> headers ) throws Exception {

        if(null==productPriceDTO) return true;
        //获取供货商信息


        try {
            SupplierMessageDTO supplierMessageDTO = this.getSupplierMsg(productPriceDTO.getSupplierNo());
            if(null!=supplierMessageDTO){
                if (!inspectPropertyValue(productPriceDTO)) return true;


                String supplierType = supplierMessageDTO.getQuoteMode();;
//                log.info("supplier type ="+ supplierType);
                if("PurchasePrice".equals(supplierType)||"1".equals(supplierType)){       //供货架

                    Map<String,String> supplierMap = this.getValidSupplier();
                    if(supplierMap.containsKey(productPriceDTO.getSopUserNo())){
                        if(this.isNeedPushForSupplyPrice(productPriceDTO)){
                        	if(StringUtils.isNotBlank(productPriceDTO.getMarketPrice())&&StringUtils.isNotBlank(productPriceDTO.getPurchasePrice())){
                        		//重新计算价格
                                String originSupplyPrice = productPriceDTO.getPurchasePrice();
                                String originMarketPrice = productPriceDTO.getMarketPrice();
                                //市场价税率
                                String rateForMarket = supplierMap.get(productPriceDTO.getSopUserNo());
                                if(StringUtils.isBlank(rateForMarket)){
                                    rateForMarket = "1";
                                }
                                reSetPrice(supplierMessageDTO,productPriceDTO,rateForMarket);

                                productPriceDTO.setCurrency(supplierMessageDTO.getCurrency());
                                handSupplyPrice(productPriceDTO,originSupplyPrice,originMarketPrice);
                        	}else{
                        		priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                                        productPriceDTO.getSkuNo(), PriceHandleState.HANDLED_SUCCESS.getIndex(),"市场价为空或供价为空不能推送");
                        	}
                        }else{
                            priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                                    productPriceDTO.getSkuNo(), PriceHandleState.HANDLED_SUCCESS.getIndex(),"供价制非供价发生变化，不需要推送");
                        }
                    }else{
                        priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                                productPriceDTO.getSkuNo(), PriceHandleState.HANDLED_SUCCESS.getIndex(),"暂不处理");

                    }

                }else if("3".equals(supplierType)||"MarketDiscount".equals(supplierType)){ // 市场价 (原来定义的是3）
                    if(this.isNeedPushForMarketPrice(productPriceDTO)){
                        handleMarketPrice(productPriceDTO);
                    }else{
                        priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                                productPriceDTO.getSkuNo(), PriceHandleState.HANDLED_SUCCESS.getIndex(),"市场价折扣制非市场价发生变化，不需要推送");
                    }

                }else{
                    //无类型

                    priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                            productPriceDTO.getSkuNo(), PriceHandleState.PUSHED_ERROR.getIndex(),"无供货商信息");
                    return false;
                }
            }else{

                supplierService.sendMail(productPriceDTO.getSupplierNo());
            }


            return true;
        } catch (Exception e) {

            String season = "推送SCM时，发生异常错误.reason : "+e.getMessage();
            if(season.length()>1500){
                season = season.substring(0,1500);
            }
            priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),productPriceDTO.getSkuNo(), PriceHandleState.PUSHED_ERROR.getIndex(),season);
            throw e;

        }


    }

    private boolean inspectPropertyValue(ProductPriceDTO productPriceDTO) throws Exception {
        if(StringUtils.isBlank(productPriceDTO.getMarketPrice())&&StringUtils.isBlank(productPriceDTO.getPurchasePrice())){
            priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSupplierPriceChangeRecordId(),productPriceDTO.getSopUserNo(),
                    productPriceDTO.getSkuNo(), PriceHandleState.HANDLE_ERROR.getIndex(),"供价和市场价不能同时为空");
            return false;
        }
        return true;
    }

    private void handSupplyPrice(ProductPriceDTO productPriceDTO,String originPurchasePrice,String originMarketPrice) throws Exception {
        if(priceSendService.sendSupplyPriceMsgToScm(productPriceDTO,originPurchasePrice,originMarketPrice)){
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


    private String getSupplierPriceType(String suppplierNo) throws Exception{

        SupplierMessageDTO supplieDTO = this.getSupplierMsg(suppplierNo);

        if(null!=supplieDTO){
            return  supplieDTO.getQuoteMode();
        }else{
            return "";
        }
    }

    /**
     * ServiceRate  :服务费率
     * @param suppplierNo
     * @return
     * @throws Exception
     */
    public  SupplierMessageDTO getSupplierMsg(String suppplierNo) throws Exception{

        SupplierDTO supplierDTO= supplierService.getSupplier(suppplierNo);// openapiSupplier.getSupplierMessage(suppplierId);
        if(null==supplierDTO) return null;
        if(null==supplierDTO.getSupplierContract()||supplierDTO.getSupplierContract().size()<1) return null;
        SupplierMessageDTO supplierMessageDTO = new SupplierMessageDTO();
        supplierMessageDTO.setQuoteMode(supplierDTO.getSupplierContract().get(0).getQuoteMode().toString());
        supplierMessageDTO.setCurrency(supplierDTO.getCurrency());
        supplierMessageDTO.setSopUserNo(Long.valueOf(supplierDTO.getSopUserNo()));
        supplierMessageDTO.setSupplierNo(suppplierNo);
        supplierMessageDTO.setServiceRate(String.valueOf((supplierDTO.getSupplierContract().get(0).getServiceRate())));
        log.info("supplier message  = " + mapper.writeValueAsString(supplierMessageDTO));
        return supplierMessageDTO;


    }

    private void reSetPrice( SupplierMessageDTO supplierMessageDTO,ProductPriceDTO productPriceDTO,String rateForMarket) throws  Exception{
        BigDecimal supplyPrice = null;
        if(StringUtils.isNotBlank(productPriceDTO.getPurchasePrice())){
            BigDecimal serviceRate = new BigDecimal(1);
            if (StringUtils.isNotBlank(supplierMessageDTO.getServiceRate())){
                serviceRate = serviceRate.add(new BigDecimal(supplierMessageDTO.getServiceRate())) ;
            }
            BigDecimal feight =new BigDecimal(0);

            productPriceDTO.setPurchasePrice(new BigDecimal(productPriceDTO.getPurchasePrice())
                    .multiply(serviceRate).add(feight).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }
        if(StringUtils.isNotBlank(rateForMarket)){
            if(!"1".equals(rateForMarket)){
                BigDecimal marketRate = new BigDecimal(rateForMarket);
                productPriceDTO.setMarketPrice(new BigDecimal(productPriceDTO.getMarketPrice())
                        .multiply(marketRate).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            }

        }

    }

    private Map<String,String> getValidSupplier(){
        Map<String,String> supplierMap = new HashMap<>();
        HubSupplierValueMappingCriteriaDto criteriaDto  = new HubSupplierValueMappingCriteriaDto();
        criteriaDto.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SUPPLIER.getIndex().byteValue())
                .andMappingStateEqualTo(DataState.NOT_DELETED.getIndex());
        List<HubSupplierValueMappingDto> hubSupplierValueMappingDtos = hubSupplierValueMappingGateWay.selectByCriteria(criteriaDto);
        if(null!=hubSupplierValueMappingDtos&&hubSupplierValueMappingDtos.size()>0){
            for(HubSupplierValueMappingDto supplier:hubSupplierValueMappingDtos){
                supplierMap.put(supplier.getSupplierId(),supplier.getSupplierVal());
            }
        }
        return supplierMap;
    }


    private boolean isNeedPushForMarketPrice(ProductPriceDTO productPriceDTO){
        if(PriceHandleType.NEW_DEFAULT.getIndex()==productPriceDTO.getPriceHandleType().byteValue()||
                PriceHandleType.MARKET_PRICE_CHANGED.getIndex()==productPriceDTO.getPriceHandleType().byteValue()||
                PriceHandleType.MARKET_SEASON_CHANGED.getIndex()==productPriceDTO.getPriceHandleType().byteValue()||
                PriceHandleType.MARKET_SUPPLY_CHANGED.getIndex()==productPriceDTO.getPriceHandleType().byteValue()||
                PriceHandleType.MARKET_SUPPLY_SEASON_CHANGED.getIndex()==productPriceDTO.getPriceHandleType().byteValue()||
                PriceHandleType.SEASON_CHANGED.getIndex()==productPriceDTO.getPriceHandleType().byteValue()){
            return  true;
        }else{
            return false;
        }

    }


    private boolean isNeedPushForSupplyPrice(ProductPriceDTO productPriceDTO){
        if(PriceHandleType.NEW_DEFAULT.getIndex()==productPriceDTO.getPriceHandleType().byteValue()||
                PriceHandleType.MARKET_SUPPLY_CHANGED.getIndex()==productPriceDTO.getPriceHandleType().byteValue()||
                PriceHandleType.SUPPLY_PRICE_CHANGED.getIndex()==productPriceDTO.getPriceHandleType().byteValue()||
                PriceHandleType.SUPPLY_SEASON_CHANGED.getIndex()==productPriceDTO.getPriceHandleType().byteValue()){
            return  true;
        }else{
            return false;
        }
    }

}
