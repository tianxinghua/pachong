package com.shangpin.iog.product.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.CsvAttributeInfoDTO;
import com.shangpin.iog.dto.CsvSupplierInfoDTO;
import com.shangpin.iog.product.dao.CsvAttributeInfoMapper;
import com.shangpin.iog.product.dao.CsvSupplierInfoMapper;
import com.shangpin.iog.service.CsvSupplierService;

@Service
public class CsvSupplierServiceImpl implements CsvSupplierService {

	@Autowired
	CsvAttributeInfoMapper csvAttributeInfoDao;
	@Autowired
	CsvSupplierInfoMapper csvSupplierInfoDao;
	
	@Override
	public void saveCsvSupplierInfo(CsvSupplierInfoDTO csvSupplierInfoDTO) throws SQLException {
		csvSupplierInfoDao.save(csvSupplierInfoDTO);
		
	}

	@Override
	public void saveCsvAttributeInfo(CsvAttributeInfoDTO csvAttributeInfoDTO) throws SQLException {
		csvAttributeInfoDao.save(csvAttributeInfoDTO);
		
	}

	@Override
	public List<CsvSupplierInfoDTO> findAllCsvSuppliers() throws ServiceException{
		return csvSupplierInfoDao.findAllCsvSuppliers();
	}
	
	@Override
	public List<CsvSupplierInfoDTO> findCsvSuppliersByState(String state) throws ServiceException{
		return csvSupplierInfoDao.findCsvSuppliersByState(state);
	}

	@Override
	public List<CsvAttributeInfoDTO> findCsvAttributeBySupplierId(
			String supplierId) throws ServiceException {
		return csvAttributeInfoDao.findCsvAttributeBySupplierId(supplierId);
	}

	@Override
	public void updateCsvAttributeInfo(CsvAttributeInfoDTO csvAttributeInfoDTO) throws SQLException{
		csvAttributeInfoDao.update(csvAttributeInfoDTO);
	}
	
	@Override
	public void delete(String id) throws SQLException{ 
		csvAttributeInfoDao.delete(id);
	}

	@Override
	public void updateCsvSupplierInfo(CsvSupplierInfoDTO csvSupplierInfoDTO) throws SQLException {
		csvSupplierInfoDao.update(csvSupplierInfoDTO);
		
	}

	@Override
	public void deleteCsvSupplierInfo(String supplierId) throws ServiceException {  
		csvSupplierInfoDao.deleteBySupplierId(supplierId);
		
	}

}
