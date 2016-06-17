package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.CsvAttributeInfoDTO;

@Mapper
public interface CsvAttributeInfoMapper extends IBaseDao<CsvAttributeInfoDTO>{

	/**
	 * 根据supplierId查找该供货商所有属性值以及规则
	 * @param supplierId
	 * @return
	 */
	public List<CsvAttributeInfoDTO> findCsvAttributeBySupplierId(@Param("supplierId") String supplierId) throws ServiceException;
	
	
}
