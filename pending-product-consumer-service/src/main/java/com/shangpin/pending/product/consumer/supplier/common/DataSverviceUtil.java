package com.shangpin.pending.product.consumer.supplier.common;

import java.io.IOException;
import java.util.*;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.enumeration.SupplierValueMappingType;
import com.shangpin.pending.product.consumer.supplier.dto.SupplierSizeMappingDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by loyalty on 17/1/7.
 * 非数据库层的处理
 */
@Component
@Slf4j
public class DataSverviceUtil {
    //有顺序的 不能打乱顺序
    static Map<String,String> sizeMap = new LinkedHashMap<String,String>(){
        {
            put("½U",".5");
            put("½",".5");
            put("+",".5");
            put("2/3",".5");
            put("UNIQUE","均码");
            put("Unica","均码");
            put("One size","均码");
            put("UNI","均码");
            put("TU","均码");
            put("U","均码");
            put("Medium","M");
            put("Small","S");
            put("VIII","8");
            put("VI","6");
            put("III","3");
            put("II","2");
            put("I","1");
        }
    };

    @Autowired
    IShangpinRedis shangpinRedis;

    @Autowired
    DataServiceHandler dataServiceHandler;

    /**
     * 先进入redis查找 没有查找数据库
     * @param supplierId
     * @return   返回 尚品的尺码+"," +尚品的前端的选择尺码的ＩＤ
     */
    public Map<String,String> getSupplierSizeMapping(String supplierId){

        String supplierValueMapping = shangpinRedis.get(ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY+"_"+supplierId);
        List<SupplierSizeMappingDto> supplierSizeMappingDtos= null;
        ObjectMapper mapper=new ObjectMapper();
        if(StringUtils.isNotBlank(supplierValueMapping)){

            try {
                supplierSizeMappingDtos=  mapper.readValue(supplierValueMapping, new TypeReference<List<SupplierSizeMappingDto>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

            try {
                supplierSizeMappingDtos = new ArrayList<>();
                List<HubSupplierValueMappingDto> supplierValueMappingDtos = dataServiceHandler.getHubSupplierValueMappingBySupplierIdAndType(supplierId, SupplierValueMappingType.TYPE_SIZE.getIndex());
                if(null!=supplierValueMappingDtos&&supplierValueMappingDtos.size()>0){

                    for(HubSupplierValueMappingDto dto:supplierValueMappingDtos){
                        SupplierSizeMappingDto sizeMappingDto = new SupplierSizeMappingDto();
                        sizeMappingDto.setSpSize(dto.getHubVal());
                        sizeMappingDto.setSupplierSize(dto.getSupplierVal());
                        sizeMappingDto.setSpScreenSizeId(null==dto.getHubValNo()?"":dto.getHubValNo());
                        supplierSizeMappingDtos.add(sizeMappingDto);

                    }
                    shangpinRedis.setex(ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY+"_"+supplierId,1000*ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_TIME,mapper.writeValueAsString(supplierSizeMappingDtos));
                }
            } catch (Exception e) {
                log.error("handle size mapping error. reason :  "  + e.getMessage(),e);
                e.printStackTrace();
            }


        }

        Map<String,String> map = new HashMap<>();
        if(null!=supplierSizeMappingDtos){
            for(SupplierSizeMappingDto dto:supplierSizeMappingDtos){
                map.put(dto.getSupplierSize(),dto.getSpSize()+","+dto.getSpScreenSizeId());
            }
        }
        return map;

    }

    public Map<String,String> getCommonSupplierSizeMapping(){

        String supplierValueMapping = shangpinRedis.get(ConstantProperty.REDIS_EPHUB_SUPPLIER_COMMON_SIZE_MAPPING_KEY);
        List<SupplierSizeMappingDto> supplierSizeMappingDtos= null;
        ObjectMapper mapper=new ObjectMapper();
        if(StringUtils.isNotBlank(supplierValueMapping)){
            try {
                supplierSizeMappingDtos=  mapper.readValue(supplierValueMapping, new TypeReference<List<SupplierSizeMappingDto>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

            try {
                supplierSizeMappingDtos = new ArrayList<>();
                List<HubSupplierValueMappingDto> supplierValueMappingDtos = dataServiceHandler.getSupplierCommonSizeValueMapping();
                if(null!=supplierValueMappingDtos&&supplierValueMappingDtos.size()>0){

                    for(HubSupplierValueMappingDto dto:supplierValueMappingDtos){
                        SupplierSizeMappingDto sizeMappingDto = new SupplierSizeMappingDto();
                        sizeMappingDto.setSpSize(dto.getHubVal());
                        sizeMappingDto.setSupplierSize(dto.getSupplierVal());
                        supplierSizeMappingDtos.add(sizeMappingDto);

                    }
                    shangpinRedis.setex(ConstantProperty.REDIS_EPHUB_SUPPLIER_COMMON_SIZE_MAPPING_KEY,1000*ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_TIME,mapper.writeValueAsString(supplierSizeMappingDtos));
                }
            } catch (Exception e) {
                log.error("handle size mapping error. reason :  "  + e.getMessage(),e);
                e.printStackTrace();
            }
        }

        Map<String,String> map = new LinkedHashMap<>();
        if(null!=supplierSizeMappingDtos){
            for(SupplierSizeMappingDto dto:supplierSizeMappingDtos){
                map.put(dto.getSupplierSize(),dto.getSpSize());
            }
        }
        return map;

    }


    public String sizeCommonReplace(String size){
        Map<String,String> commonSizeMap = getCommonSupplierSizeMapping();
        if(size==null){
            return size;
        }
        if(null!=commonSizeMap&&commonSizeMap.size()>0){

            Set<String> sizeSet  = getCommonSupplierSizeMapping().keySet();//sizeMap.keySet();
            String replaceKey="";
            for(String sizeKey:sizeSet){
                if("++".equals(sizeKey)){
                    replaceKey = "\\++";
                }else  if("+".equals(sizeKey)){
                    replaceKey = "\\+";
                }else{
                    replaceKey = sizeKey;
                }
                if(size.indexOf(sizeKey)>=0){
                    size = size.replaceAll(replaceKey,sizeMap.get(sizeKey));
                }
            }
        }
        return size;
    }


    public  void updatePriceOrStock(PendingSku supplierSku){
        if(null!=supplierSku){
            HubSkuPendingDto originSkuPending =  dataServiceHandler.getHubSkuPending(supplierSku.getSupplierId(),supplierSku.getSupplierSkuNo());
            if(null!=originSkuPending){
                HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
                hubSkuPending.setSkuPendingId(originSkuPending.getSkuPendingId());
                if(null!=supplierSku.getStock()){
                    hubSkuPending.setStock(supplierSku.getStock());
                }
                if(null!=supplierSku.getMarketPrice()){
                    hubSkuPending.setMarketPrice(supplierSku.getMarketPrice());
                }
                if(null!=supplierSku.getSalesPrice()){
                    hubSkuPending.setSalesPrice(supplierSku.getSalesPrice());
                }
                if(null!=supplierSku.getSupplyPrice()){
                    hubSkuPending.setSupplyPrice(supplierSku.getSupplyPrice());
                }
                Date date = new Date();
                hubSkuPending.setUpdateTime(date);

                dataServiceHandler.updateSkuPengding(hubSkuPending);
            }

        }

    }


}
