package com.shangpin.ephub.price.consumer.util;

import com.shangpin.ephub.client.data.mysql.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;

import java.util.List;
import java.util.Map;

public class SupplierMappingHelper {
    private static Map<String,HubSupplierValueMappingDto> map;

    private static HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

    public static HubSupplierValueMappingDto getSupplierMapping(String supplierId){
        HubSupplierValueMappingDto tmp =map.get(supplierId);
        if(tmp!=null){
            return tmp;
        }
        HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
        criteria.createCriteria().andSupplierIdEqualTo(supplierId).andHubValTypeEqualTo(new Integer(6).byteValue()).andSupplierValEqualTo("1");
        List<HubSupplierValueMappingDto> list = hubSupplierValueMappingGateWay.selectByCriteria(criteria);
        if(list.size()>0){
            HubSupplierValueMappingDto tmp2 = list.get(0);
            map.put(supplierId,tmp2);
            return tmp2;
        }else{
            map.put(supplierId,null);
            return null;
        }
    }
}
