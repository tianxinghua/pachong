package com.shangpin.ephub.data.schedule.service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lizhongren on 2017/5/23.
 */
@Component
public class ProductPullDataService {
    @Autowired
    HubSupplierSkuGateWay supplierSkuGateWay;

    public void findSupplierLastPullTime(){
        HubSupplierSkuCriteriaDto supplierSkuCriteriaDto = new HubSupplierSkuCriteriaDto();

        List<HubSupplierSkuDto> skuDtos = supplierSkuGateWay.selectByCriteria(supplierSkuCriteriaDto);
    }
}
