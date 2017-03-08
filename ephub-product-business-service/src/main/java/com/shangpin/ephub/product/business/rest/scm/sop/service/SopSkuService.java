package com.shangpin.ephub.product.business.rest.scm.sop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.gms.dto.HubResponseDto;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuDto;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuQueryDto;

import lombok.extern.slf4j.Slf4j;
@Component("sopSkuServiceImpl")
@Slf4j
public class SopSkuService {
	
	@Autowired
    private RestTemplate restTemplate;
	@Autowired
    private ApiAddressProperties apiAddressProperties;

	 public HubResponseDto<SopSkuDto> querySpSkuNoFromScm(SopSkuQueryDto queryDto) throws JsonProcessingException {
	        HttpEntity<SopSkuQueryDto> requestEntity = new HttpEntity<SopSkuQueryDto>(queryDto);
	        ObjectMapper mapper = new ObjectMapper();
	        log.info("send spSku query parameter: " + mapper.writeValueAsString(queryDto));

	        ResponseEntity<HubResponseDto<SopSkuDto>> entity = restTemplate.exchange(apiAddressProperties.getSopSkuListBySupplierSkuNoUrl(), HttpMethod.POST,
	                requestEntity, new ParameterizedTypeReference<HubResponseDto<SopSkuDto>>() {
	                });
	        return entity.getBody();
	    }
}
