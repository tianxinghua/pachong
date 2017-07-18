package com.shangpin.ephub.product.business.service.supplier.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.product.business.service.supplier.SupplierService;
import com.shangpin.ephub.product.business.service.supplier.dto.SupplierDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by loyalty on 17/7/5.
 */
@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

    @Override
    public SupplierDto getSupplierBySupplierId(String supplierId) {
        SupplierDto supplierDto = null;
        HubSupplierValueMappingCriteriaDto criteriaDto  = new HubSupplierValueMappingCriteriaDto();
        criteriaDto.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SUPPLIER.getIndex().byteValue())
                .andSupplierIdEqualTo(supplierId);
        List<HubSupplierValueMappingDto> hubSupplierValueMappingDtos = hubSupplierValueMappingGateWay.selectByCriteria(criteriaDto);
        if(null!=hubSupplierValueMappingDtos&&hubSupplierValueMappingDtos.size()>0){
            supplierDto = new SupplierDto();
            HubSupplierValueMappingDto dto = hubSupplierValueMappingDtos.get(0);
            supplierDto.setSupplierId(dto.getSupplierId());
            supplierDto.setSupplierNo(dto.getHubValNo());
            supplierDto.setSupplierName(dto.getHubVal());
            if(null!=dto.getMappingState()&&"1".equals(dto.getMappingState().toString())){
                supplierDto.setSupplyPrice(true);
            }else{
                supplierDto.setSupplyPrice(false);
            }
            if(null!=dto.getSupplierValNo()&&"1".equals(dto.getSupplierValNo())){
                supplierDto.setStudio(true);
            }else{
                supplierDto.setStudio(false);
            }
            if(StringUtils.isNotBlank(dto.getSupplierVal())){
                supplierDto.setSupplierRate(dto.getSupplierVal());
            }else{
                supplierDto.setSupplierRate("1");
            }
        }
        return supplierDto;
    }
}
