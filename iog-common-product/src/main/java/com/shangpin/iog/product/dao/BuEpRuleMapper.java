package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.BuEpRuleDTO;

@Mapper
public interface BuEpRuleMapper extends IBaseDao<BuEpRuleDTO>{

	/**
	 * 根据选定的bu和supplierId查找BuEpRuleDTO集合
	 * @param bu
	 * @param supplierId
	 * @return
	 */
	public List<BuEpRuleDTO> findBuEpRuleListBySupplierId(@Param("bu")String bu,@Param("supplierId")String supplierId) throws ServiceException;
	
	/**
	 * 查找所有的bu
	 * @return
	 */
	public List<String> findAllBus() throws ServiceException;
	
	public List<String> findAllSuppliersByBu(@Param("bu") String bu);
		
}
