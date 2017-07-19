package com.shangpin.ephub.product.business.service.supplier.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.product.business.service.ServiceConstant;
import com.shangpin.ephub.product.business.service.supplier.SupplierInHubService;
import com.shangpin.ephub.product.business.service.supplier.dto.SupplierDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loyalty on 17/7/5.
 */
@Service
@Slf4j
public class SupplierInHubServiceImpl implements SupplierInHubService {

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

    @Override
    public List<HubSupplierValueMappingDto> getNeedShootSupplier() {
        HubSupplierValueMappingCriteriaDto criteriaDto  = new HubSupplierValueMappingCriteriaDto();
        criteriaDto.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SUPPLIER.getIndex().byteValue())
                .andSupplierValParentNoEqualTo(ServiceConstant.HUB_SLOT_NEED_SHOOT_SUPPLIERVALPARENTNO);
        return  hubSupplierValueMappingGateWay.selectByCriteria(criteriaDto);

    }

    @Override
    public List<String> getNeedShootSupplierId() {
        List<HubSupplierValueMappingDto> supplierDtos =this.getNeedShootSupplier();
        List<String> supplierIds = new ArrayList<>();
        for(HubSupplierValueMappingDto dto :supplierDtos){
            if(StringUtils.isNotBlank(dto.getSupplierId())){
                supplierIds.add(dto.getSupplierId());
            }
        }
        return supplierIds;
    }

    @Override
    public Boolean isShootSupplier(String supplierId) {
        HubSupplierValueMappingCriteriaDto criteriaDto  = new HubSupplierValueMappingCriteriaDto();
        criteriaDto.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SUPPLIER.getIndex().byteValue())
                .andSupplierIdEqualTo(supplierId)
                .andSupplierValParentNoEqualTo(ServiceConstant.HUB_SLOT_NEED_SHOOT_SUPPLIERVALPARENTNO)
                .andSupplierValNoEqualTo(ServiceConstant.HUB_SLOT_NOT_NEED_SEND_SUPPLIERVALNO);
        List<HubSupplierValueMappingDto> hubSupplierValueMappingDtos = hubSupplierValueMappingGateWay.selectByCriteria(criteriaDto);
        if(null!=hubSupplierValueMappingDtos&&hubSupplierValueMappingDtos.size()>0){
            return true;
        }
        return false;
    }


}
