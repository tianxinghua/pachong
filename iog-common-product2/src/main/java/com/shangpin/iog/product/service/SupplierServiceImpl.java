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
public class SupplierServiceImpl implements SupplierService {


    @Autowired
    SupplierMapper supplierDAO;

    @Override
    public List<SupplierDTO> findByState(String state) throws ServiceException {
    	List<SupplierDTO> supplierDTOList =  supplierDAO.findByState(state);
        return supplierDTOList;
    }

    @Override
    public List<SupplierDTO> findAllWithAvailable() throws ServiceException {
        List<SupplierDTO> supplierDTOList =  supplierDAO.findByState("1");
        return supplierDTOList;
    }

	@Override
	public SupplierDTO findBysupplierId(String supplierId)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplierDTO> hkFindAllByState(String supplier_state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplierDTO> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplierDTO hkFindBysupplierId(String supplier)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
