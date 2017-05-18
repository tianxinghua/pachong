package com.shangpin.ephub.price.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.product.business.gms.dto.HubResponseDto;
import com.shangpin.ephub.price.consumer.common.GlobalConstant;
import com.shangpin.ephub.price.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.price.consumer.service.dto.SupplierDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhongren on 2017/5/9.
 * 供货商信息获取服务
 */
@Service
@Slf4j

public class SupplierService {

    @Autowired
    private IShangpinRedis shangpinRedis;

    @Autowired
    private ApiAddressProperties apiAddressProperties;
    @Autowired
    private RestTemplate restTemplate;

    ObjectMapper om = new ObjectMapper();

    public SupplierDTO getSupplier(String supplierNo) {
        SupplierDTO dto = null;
        //先获取缓存中的数据
        String supplierMsg = shangpinRedis.get(GlobalConstant.REDIS_PRICE_PUSH_CONSUMER_SERVICE_SUPPLIER_KEY+"_"+supplierNo);

        if(StringUtils.isNotBlank(supplierMsg)){
            try {
                dto = om.readValue(supplierMsg, SupplierDTO.class);
                return dto;
            } catch (Exception e) {
                log.error("供货商"+supplierNo + "从redis中获取信息后，转化对象失败");
            }
        }

        //调用接口获取供货商信息

        try {
        	
//        	Map<String, String> paraMap = new HashMap<>();
//			paraMap.put("supplierNo", supplierNo);
//			  String supplierUrl =apiAddressProperties.getScmsSupplierInfoUrl()+supplierNo;
//			log.info("supplierNo:"+supplierNo);
//			String reSupplierMsg = restTemplate.getForObject(supplierUrl, String.class);
//        	
//        	
        	
        	
            String supplierUrl =apiAddressProperties.getScmsSupplierInfoUrl()+supplierNo;
            ResponseEntity<SupplierDTO> entity = restTemplate.exchange(supplierUrl, HttpMethod.POST, null, new ParameterizedTypeReference<SupplierDTO>() {});

            SupplierDTO  supplierDTOHubResponseDto = entity.getBody();
//            dto = supplierDTOHubResponseDto.getResDatas().get(0);
            //记录到REDIS缓存中
            shangpinRedis.setex(GlobalConstant.REDIS_PRICE_PUSH_CONSUMER_SERVICE_SUPPLIER_KEY+"_"+supplierNo,1000*60*5,om.writeValueAsString(dto));

        } catch (Exception e) {
            log.error("未获取到供货商信息");

        }

        return  dto;
    }
}
