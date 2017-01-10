package com.shangpin.pending.product.consumer.supplier.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.enumeration.SupplierValueMappingType;
import com.shangpin.pending.product.consumer.supplier.dto.SupplierSizeMappingDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by loyalty on 17/1/7.
 */
@Component
@Slf4j
public class DataSverviceUtil {
    //有顺序的 不能打乱顺序
    static Map<String,String> sizeMap = new LinkedHashMap<String,String>(){
        {
            put("½U",".5");
            put("½",".5");
            put("VIII","8");
            put("VI","6");
            put("III","3");
            put("II","2");
            put("I","1");
            put("2/3",".5");
            put("UNIQUE","均码");
            put("Unica","均码");
            put("One size","均码");
            put("UNI","均码");
            put("TU","均码");
            put("U","均码");
            put("Medium","M");
            put("Small","S");




        }
    };

    @Autowired
    IShangpinRedis shangpinRedis;

    @Autowired
    DataServiceHandler dataServiceHandler;

    /**
     * 先进入redis查找 没有查找数据库
     * @param supplierId
     * @return
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
                if(null!=supplierSizeMappingDtos&&supplierValueMappingDtos.size()>0){

                    for(HubSupplierValueMappingDto dto:supplierValueMappingDtos){
                        SupplierSizeMappingDto sizeMappingDto = new SupplierSizeMappingDto();
                        supplierSizeMappingDtos.add(sizeMappingDto);

                    }
                    shangpinRedis.setex(ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY+"_"+supplierId,1000*ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_TIME,mapper.writeValueAsString(supplierValueMappingDtos));
                }
            } catch (Exception e) {
                log.error("handle size mapping error. reason :  "  + e.getMessage(),e);
                e.printStackTrace();
            }


        }

        Map<String,String> map = new HashMap<>();
        if(null!=supplierSizeMappingDtos){
            for(SupplierSizeMappingDto dto:supplierSizeMappingDtos){
                map.put(dto.getSupplierSize(),dto.getSpSize());
            }
        }
        return map;

    }

    public String sizeCommonReplace(String size){
        Set<String> sizeSet  = sizeMap.keySet();
        for(String sizeKey:sizeSet){
            if(size.indexOf(sizeKey)>=0){
                size = size.replaceAll(sizeKey,sizeMap.get(sizeKey));
            }
        }
        return size;
    }


}
