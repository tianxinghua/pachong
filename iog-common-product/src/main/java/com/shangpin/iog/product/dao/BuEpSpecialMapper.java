package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.BuEpSpecialDTO;

@Mapper
public interface BuEpSpecialMapper extends IBaseDao<BuEpSpecialDTO> {
	
	public int findCount();
}
