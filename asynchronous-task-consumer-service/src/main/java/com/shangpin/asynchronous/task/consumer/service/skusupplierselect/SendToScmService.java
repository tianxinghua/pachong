package com.shangpin.asynchronous.task.consumer.service.skusupplierselect;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.asynchronous.task.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ApiSkuOrgDom;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.client.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/5/4.
 */
@Service
@Slf4j
public class SendToScmService {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ApiAddressProperties apiAddressProperties;



    @Autowired
    HubSkuSupplierMappingGateWay skuSupplierMappingGateWay;

    ObjectMapper mapper = new ObjectMapper();
    public void handleDate(ProductMessageDto productDto,Map<String,Object> headers) {


        HubResponseDto<String> responseDto = null;
        try {
            log.info("推送scm参数:"+JsonUtil.serialize(productDto));
            responseDto = sendToScm(productDto);
            log.info("推送scm返回结果:"+JsonUtil.serialize(responseDto));
        } catch (Exception e) {
            try {
                log.error("推送SCM发生异常：推送请求"+ mapper.writeValueAsString(productDto) + " reason :"
                + e.getMessage(),e);
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }

        }

        List<ApiSkuOrgDom> skuList = productDto.getSkuList();
        ApiSkuOrgDom skuOrg = null;
        if(null!=skuList&&skuList.size()>0){
            skuOrg = skuList.get(0);
            if(null==responseDto){
                updateSkuMappingStatus(Long.valueOf(skuOrg.getSkuOrginalFromId()), SupplierSelectState.SELECTE_FAIL,"推送SCM时失败");
            }else{

                if(responseDto.getIsSuccess()){  //创建成功
                    updateSkuMappingStatus(Long.valueOf(skuOrg.getSkuOrginalFromId()), SupplierSelectState.WAIT_SCM_AUDIT,"");

                }else{ //创建失败
                    updateSkuMappingStatus(Long.valueOf(skuOrg.getSkuOrginalFromId()),SupplierSelectState.SELECTE_FAIL,responseDto.getResMsg());

                }
            }
        }else{
            try {
                log.error("推送SCM发生异常：推送请求"+ mapper.writeValueAsString(productDto) + " reason : 无SKU信息");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }


    }
    private void updateSkuMappingStatus(Long id,SupplierSelectState status,String reason){
        HubSkuSupplierMappingDto skuSupplierMapping = new HubSkuSupplierMappingDto();
        skuSupplierMapping.setSkuSupplierMappingId(id);
        skuSupplierMapping.setSupplierSelectState(Integer.valueOf(status.getIndex()).byteValue());
        skuSupplierMapping.setUpdateTime(new Date());
        skuSupplierMapping.setMemo(reason);

        skuSupplierMappingGateWay.updateByPrimaryKeySelective(skuSupplierMapping);
    }


    private HubResponseDto<String> sendToScm(ProductMessageDto productDto) throws JsonProcessingException {
        HttpEntity<ProductMessageDto> requestEntity = new HttpEntity<ProductMessageDto>(productDto);
        ResponseEntity<HubResponseDto<String>> entity = restTemplate.exchange(apiAddressProperties.getGmsAddProductUrl(), HttpMethod.POST,
                requestEntity, new ParameterizedTypeReference<HubResponseDto<String>>() {
                });
        return entity.getBody();
    }
}
