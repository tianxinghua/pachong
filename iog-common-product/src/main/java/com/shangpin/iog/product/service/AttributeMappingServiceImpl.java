package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.HubSupplierValueMappingDTO;
import com.shangpin.iog.product.dao.HubSupplierValueMappingMapper;
import com.shangpin.iog.service.AttributeMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * Created by lizhongren on 2016/9/14.
 */
@Service("attributeMappingServiceImpl")
public class AttributeMappingServiceImpl implements AttributeMappingService {


    @Autowired
    HubSupplierValueMappingMapper mappingMapperDAO;

    @Override
    public Boolean saveBrand(HubSupplierValueMappingDTO dto) throws ServiceException {
        if(null==dto) return false;
        HubSupplierValueMappingDTO tmp =mappingMapperDAO.getMappingBySupplierBrandName(dto.getSupplierValue());
        if(null==tmp){

            try {
                mappingMapperDAO.save(dto);
            } catch (Exception e) {
                return false;
            }
        }else{
            return  false;
        }
        return true;
    }
}
