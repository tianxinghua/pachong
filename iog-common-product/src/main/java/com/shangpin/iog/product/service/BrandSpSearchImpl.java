package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by loyalty on 15/6/9.
 */
@Service
public class BrandSpSearchImpl implements SupplierService {


    @Autowired
    SupplierMapper supplierDAO;

    @Override
    public List<SupplierDTO> findByState(String state) throws ServiceException {
        return null;
    }

    @Override
    public List<SupplierDTO> findAllWithAvailable() throws ServiceException {
        List<SupplierDTO> supplierDTOList =  supplierDAO.findByState("1");
        return supplierDTOList;
    }
}
