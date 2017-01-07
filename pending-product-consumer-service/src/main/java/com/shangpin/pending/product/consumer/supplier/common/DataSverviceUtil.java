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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 17/1/7.
 */
@Component
@Slf4j
public class DataSverviceUtil {
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


}
