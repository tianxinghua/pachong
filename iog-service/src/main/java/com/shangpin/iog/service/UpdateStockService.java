package com.shangpin.iog.service;

import java.sql.SQLException;
import java.util.List;

import com.shangpin.iog.dto.StockUpdateDTO;


/**
 * @author monkey
 *
 */
public interface UpdateStockService {
	/**
	 * 更新updateStock的时间
	 */
	public void updateTime(String supplierId) throws SQLException;
	
	/**
	 * 获取所有的库存更新状态
	 */
	public List<StockUpdateDTO> getAll() throws SQLException;
	
}
