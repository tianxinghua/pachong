package com.shangpin.asynchronous.task.consumer.service.pending;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.asynchronous.task.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ApiSkuOrgDom;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.product.business.gms.dto.HubResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2017/5/4.
 */
@Service
@Slf4j
public class SpuPendingAuditService {




    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    HubSkuPendingGateWay skuPendingGateWay;




    @Autowired
    PengdingToHubGateWay pengdingToHubGateWay;

    ObjectMapper mapper = new ObjectMapper();
    public void auditSpuPending(SpuModelDto dto,Map<String,Object> header) {

        boolean result= pengdingToHubGateWay.auditPending(dto);
        if(result){
            this.updateHubSpuState(dto, SpuState.HANDLED.getIndex());
        }else{
            this.updateHubSpuState(dto,SpuState.INFO_IMPECCABLE.getIndex());
        }
    }

    private void updateHubSpuState(SpuModelDto spuModelDto,byte spuStatus) {
        Date date = new Date();
        //获取所有的审核中的spupending数据
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andSpuStateEqualTo(SpuState.HANDLING.getIndex())
                .andHubBrandNoEqualTo(spuModelDto.getBrandNo()).andSpuModelEqualTo(spuModelDto.getSpuModel());

        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setSpuState(spuStatus);
        hubSpuPending.setUpdateTime(date);


        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);

        HubSpuPendingWithCriteriaDto criteriaDto = new HubSpuPendingWithCriteriaDto(hubSpuPending, criteria);

        spuPendingGateWay.updateByCriteriaSelective(criteriaDto);

        //操作所有的审核中的skupending
        if (null != hubSpuPendingDtos && hubSpuPendingDtos.size() > 0) {

            List<Long> spuIdList = new ArrayList<>();
            for (HubSpuPendingDto spuDto : hubSpuPendingDtos) {
                spuIdList.add(spuDto.getSpuPendingId());
            }
            HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();

            hubSkuPending.setSkuState(spuStatus);
            hubSkuPending.setUpdateTime(date);

            HubSkuPendingCriteriaDto criteriaSku = new HubSkuPendingCriteriaDto();
            criteriaSku.createCriteria().andSpuPendingIdIn(spuIdList).andSkuStateEqualTo(SpuState.HANDLING.getIndex());

            HubSkuPendingWithCriteriaDto criteriaSkuDto = new HubSkuPendingWithCriteriaDto(hubSkuPending, criteriaSku);
            skuPendingGateWay.updateByCriteriaSelective(criteriaSkuDto);
        }
    }
}
