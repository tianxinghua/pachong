package com.shangpin.ephub.price.consumer.service;

import IceUtilInternal.StringUtil;
import com.shangpin.commons.redis.IShangpinRedis;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.product.business.gms.dto.HubResponseDto;
import com.shangpin.ephub.client.product.business.gms.dto.SupplierDTO;
import com.shangpin.ephub.price.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;


import com.shangpin.iog.ice.dto.SupplierMessageDTO;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    PriceChangeRecordDataService  priceChangeRecordDataService;

    @Autowired
    HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

    @Autowired
    SupplierService supplierService;


    public Boolean  sendPriceMessageToScm(ProductPriceDTO productPriceDTO,Map<String, Object> headers ) throws Exception {

        if(null==productPriceDTO) return true;
        //获取供货商信息

        List<String> spSkus  = new ArrayList<>();
        spSkus.add(productPriceDTO.getSkuNo());
        try {
            SupplierMessageDTO supplierMessageDTO = this.getSupplierMsg(productPriceDTO.getSupplierNo());
            if(null!=supplierMessageDTO){

                String supplierType = supplierMessageDTO.getQuoteMode();;
                log.info("supplier type ="+ supplierType);
                if("PurchasePrice".equals(supplierType)||"1".equals(supplierType)){       //供货架
                    Map<String,String> supplierMap = this.getValidSupplier();
                    if(supplierMap.containsKey(productPriceDTO.getSopUserNo())){
                        //重新计算价格
                        reSetPrice(supplierMessageDTO,productPriceDTO);

                        handSupplyPrice(productPriceDTO);
                    }else{
                        priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSopUserNo(),spSkus, PriceHandleState.HANDLED_SUCCESS.getIndex(),"暂不处理");

                    }

                }else if("3".equals(supplierType)||"MarketDiscount".equals(supplierType)){ // 市场价 (原来定义的是3）
                    handleMarketPrice(productPriceDTO);
                }else{
                    //无类型

                    priceChangeRecordDataService.updatePriceSendState(productPriceDTO.getSopUserNo(),spSkus, PriceHandleState.PUSHED_ERROR.getIndex(),"无供货商信息");
                    return false;
                }
            }else{
                //TODO 发邮件通知
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
    private SupplierMessageDTO getSupplierMsg(String suppplierNo) throws Exception{

        SupplierDTO supplierDTO= supplierService.getSupplier(suppplierNo);// openapiSupplier.getSupplierMessage(suppplierId);
        if(null==supplierDTO) return null;
        SupplierMessageDTO supplierMessageDTO = new SupplierMessageDTO();
        supplierMessageDTO.setQuoteMode(supplierDTO.getSupplierContract().get(0).getQuoteMode().toString());
        supplierMessageDTO.setCurrency(supplierDTO.getCurrency());
        supplierMessageDTO.setSopUserNo(Long.valueOf(supplierDTO.getSopUserNo().toString()));
        return supplierMessageDTO;


    }

    private void reSetPrice( SupplierMessageDTO supplierMessageDTO,ProductPriceDTO productPriceDTO) throws  Exception{
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

    }

    private Map<String,String> getValidSupplier(){
        Map<String,String> supplierMap = new HashMap<>();
        HubSupplierValueMappingCriteriaDto criteriaDto  = new HubSupplierValueMappingCriteriaDto();
        criteriaDto.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SUPPLIER.getIndex().byteValue())
                .andMappingStateEqualTo(DataState.NOT_DELETED.getIndex());
        List<HubSupplierValueMappingDto> hubSupplierValueMappingDtos = hubSupplierValueMappingGateWay.selectByCriteria(criteriaDto);
        if(null!=hubSupplierValueMappingDtos&&hubSupplierValueMappingDtos.size()>0){
            for(HubSupplierValueMappingDto supplier:hubSupplierValueMappingDtos){
                supplierMap.put(supplier.getSupplierId(),"");
            }
        }
        return supplierMap;
    }

}
