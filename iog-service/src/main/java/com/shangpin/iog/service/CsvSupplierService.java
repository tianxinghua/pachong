package com.shangpin.iog.service;

import java.sql.SQLException;
import java.util.List;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.CsvAttributeInfoDTO;
import com.shangpin.iog.dto.CsvSupplierInfoDTO;

/**
 *处理csv的接口
 * @author sunny
 *
 */
public interface CsvSupplierService {
	
	/**
	 * 保存csv供货商信息
	 * @param csvSupplierInfoDTO
	 */
	public void saveCsvSupplierInfo(CsvSupplierInfoDTO csvSupplierInfoDTO) throws SQLException;
	
	/**
	 * 保存csv属性及对应值、规则
	 * @param csvAttributeInfoDTO
	 */
	public void saveCsvAttributeInfo(CsvAttributeInfoDTO csvAttributeInfoDTO) throws SQLException;
	
	/**
	 * 查找所有csv供货商信息列表
	 * @return
	 */
	public List<CsvSupplierInfoDTO> findAllCsvSuppliers() throws ServiceException;
	
	/**
	 * 根据状态查询数据库
	 * @return
	 * @throws ServiceException
	 */
	public List<CsvSupplierInfoDTO> findCsvSuppliersByState(String state) throws ServiceException;
	
	/**
	 * 根据supplierId查找该供货商所有属性值以及规则
	 * @param supplierId
	 * @return
	 */
	public List<CsvAttributeInfoDTO> findCsvAttributeBySupplierId(String supplierId) throws ServiceException;
	
	/**
	 * 更新属性表
	 * @param csvAttributeInfoDTO
	 */
	public void updateCsvAttributeInfo(CsvAttributeInfoDTO csvAttributeInfoDTO) throws SQLException;
	
	/**
	 * 根据id删除CSV_ATTRIBUTE_INFO表某条记录
	 * @param id
	 */
	public void delete(String id) throws SQLException;
	
	/**
	 * 更新
	 * @param csvSupplierInfoDTO
	 */
	public void updateCsvSupplierInfo(CsvSupplierInfoDTO csvSupplierInfoDTO) throws SQLException ;
	
	/**
	 * 根据supplierId删除CSV_SUPPLIER_INFO表的记录
	 * @param supplierId
	 */
	public void deleteCsvSupplierInfo(String supplierId) throws ServiceException ;
	
}
