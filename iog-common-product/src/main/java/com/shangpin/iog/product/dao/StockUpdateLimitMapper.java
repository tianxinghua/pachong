package com.shangpin.iog.product.dao;

import org.apache.ibatis.annotations.Param;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.StockUpdateLimitDTO;

@Mapper
public interface StockUpdateLimitMapper  extends IBaseDao<StockUpdateLimitDTO>{
	
	  public void updateLimitNum(StockUpdateLimitDTO skuDTO) throws ServiceException;
	  public StockUpdateLimitDTO findBySupplierId(@Param("supplierId") String supplierId,@Param("nowDate") String nowDate);

}
