package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.StockUpdateLimitDTO;

public interface StockUpdateLimitService {

	  public void save(StockUpdateLimitDTO hubSku) throws ServiceException;
	  
	  
	  public void updateLimitNum(StockUpdateLimitDTO skuDTO) throws ServiceException;
	  
	  /**
	   * 按当天查询
	   * @param supplierId
	   * @return
	   */
	  public StockUpdateLimitDTO findBySupplierId(String supplierId);

}

