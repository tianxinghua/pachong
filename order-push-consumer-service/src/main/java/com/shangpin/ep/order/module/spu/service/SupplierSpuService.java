package com.shangpin.ep.order.module.spu.service;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.dto.SizeDto;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.response.HubResponse;
import com.shangpin.ep.order.module.spu.bean.HubSupplierSpu;
import com.shangpin.ep.order.module.spu.bean.SpuQueryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lizhongren on 2018/3/14.
 */
@Component
public class SupplierSpuService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SupplierProperties supplierProperties;

    public HubSupplierSpu getSupplierSpuBySupplierSpuNo(String supplierId, String supplierSpuNo){
        SpuQueryVO spuQueryVO = new SpuQueryVO();
        spuQueryVO.setSupplierId(supplierId);
        spuQueryVO.setSupplierSpuNo(supplierSpuNo);
        String supplierSpuUrl = supplierProperties.getSupplier().getProductSpuUrl();

        HttpEntity<SpuQueryVO> requestEntity = new HttpEntity<SpuQueryVO>(spuQueryVO);
        ResponseEntity<HubResponse<HubSupplierSpu>> entity = restTemplate.exchange(
                supplierSpuUrl, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<HubResponse<HubSupplierSpu>>() {
                });
        HubResponse<HubSupplierSpu> response = entity.getBody();
        if("0".equals(response.getCode())){
            return response.getContent();
        }else{
            return null;
        }
    }

    public static void  main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        SpuQueryVO spuQueryVO = new SpuQueryVO();
        spuQueryVO.setSupplierId("2016030701799");
        spuQueryVO.setSupplierSpuNo("8468565");
        String supplierSpuUrl = "http://localhost:8003/order/supplier-spu";

        HttpEntity<SpuQueryVO> requestEntity = new HttpEntity<SpuQueryVO>(spuQueryVO);
        ResponseEntity<HubResponse<HubSupplierSpu>> entity = restTemplate.exchange(
                supplierSpuUrl, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<HubResponse<HubSupplierSpu>>() {
                });
        HubResponse<HubSupplierSpu> response = entity.getBody();

//        HubResponse<HubSupplierSpu> response = restTemplate.postForObject(supplierSpuUrl, spuQueryVO, HubResponse.class);
        HubSupplierSpu spu = null;

        if("0".equals(response.getCode())){
            spu =  response.getContent();

        }else{

        }
        System.out.print("123");
    }
}
