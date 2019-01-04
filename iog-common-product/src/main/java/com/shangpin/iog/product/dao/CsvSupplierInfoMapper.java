package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.CsvSupplierInfoDTO;

@Mapper
public interface CsvSupplierInfoMapper extends IBaseDao<CsvSupplierInfoDTO>{

	/**
	 * 查找所有csv供货商信息列表
	 * @return
	 */
	public List<CsvSupplierInfoDTO> findAllCsvSuppliers() throws ServiceException;
	
	/**
	 * 根据supplierId删除
	 * @param supplierId
	 * @throws ServiceException
	 */
	public void deleteBySupplierId(@Param("supplierId") String supplierId) throws ServiceException;
	
	/**
	 * 根据状态查询数据库
	 * @return
	 * @throws ServiceException
	 */
	public List<CsvSupplierInfoDTO> findCsvSuppliersByState(@Param("state") String state) throws ServiceException;
	
}
