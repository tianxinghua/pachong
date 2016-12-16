package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.enumeration.DataStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 16/12/16.
 */
@Service
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class DataServiceHandler {

    @Autowired
    private HubBrandDicGateway brandDicGateway;

    @Autowired
    private HubSupplierBrandDicGateWay supplierBrandDicGateWay;


    public void saveBrand(String supplierId,String supplierBrandName) throws Exception{
        HubBrandDicDto brandDicDto = new HubBrandDicDto();
        brandDicDto.setCreateTime(new Date());
        brandDicDto.setSupplierBrand(supplierBrandName);
        brandDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
        brandDicDto.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
        int insert = brandDicGateway.insert(brandDicDto);

        HubSupplierBrandDicDto supplierBrandDicDto = new HubSupplierBrandDicDto();
        supplierBrandDicDto.setSupplierId(supplierId);
        supplierBrandDicDto.setSupplierBrand(supplierBrandName);
        supplierBrandDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
        supplierBrandDicDto.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
        supplierBrandDicGateWay.insert(supplierBrandDicDto);

    }

    public List<HubBrandDicDto> getBrand() throws Exception{
        HubBrandDicCriteriaDto criteria = new HubBrandDicCriteriaDto();
        HubBrandDicCriteriaDto.Criteria criterion =criteria.createCriteria();

        return brandDicGateway.selectByCriteria(criteria);

    }
}
