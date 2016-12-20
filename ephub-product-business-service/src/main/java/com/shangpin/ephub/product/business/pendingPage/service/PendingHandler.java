package com.shangpin.ephub.product.business.pendingPage.service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.product.business.pendingPage.dto.SpSkuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 16/12/19.
 */
@Service
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class PendingHandler {

    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;

    public boolean updateSpSku(SpSkuDTO spSkuDTO){
        boolean result = true;


        HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
        criteria.createCriteria()
                .andSupplierIdEqualTo(spSkuDTO.getSupplierId()).andSupplierSkuNoEqualTo(spSkuDTO.getSupplierSkuNo());

        HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
        hubSkuPending.setSpSkuNo(spSkuDTO.getSkuNo());

        HubSkuPendingWithCriteriaDto dto = new HubSkuPendingWithCriteriaDto(hubSkuPending,criteria);

        hubSkuPendingGateWay.updateByCriteriaSelective(dto);

        return result;
    }

}
