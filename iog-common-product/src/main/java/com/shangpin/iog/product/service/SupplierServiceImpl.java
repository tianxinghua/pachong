package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.service.SupplierService;

import org.apache.ibatis.annotations.Param;
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
		SupplierDTO supplierDTO = supplierDAO.findBysupplierId(supplierId);
		return supplierDTO;
	}
	
	public List<SupplierDTO> hkFindAllByState(String supplier_state) {
		return supplierDAO.hkFindAllByState(supplier_state);
	}
	
	public List<SupplierDTO> findAll() throws Exception{
		return supplierDAO.findAll();
	}
	
	public SupplierDTO hkFindBysupplierId(String supplier)throws ServiceException{
		return supplierDAO.hkFindBysupplierId(supplier);
	}
}
